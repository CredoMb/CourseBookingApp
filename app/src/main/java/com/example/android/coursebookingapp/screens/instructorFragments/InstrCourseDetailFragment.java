package com.example.android.coursebookingapp.screens.instructorFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.android.coursebookingapp.AppUtils;
import com.example.android.coursebookingapp.R;
import com.example.android.coursebookingapp.database.Course;
import com.example.android.coursebookingapp.database.CourseBookingDataBase;
import com.example.android.coursebookingapp.database.CourseDAO;
import com.example.android.coursebookingapp.database.Instructor;
import com.example.android.coursebookingapp.database.InstructorDAO;
import com.example.android.coursebookingapp.databinding.InstrCourseDetailFragmentBinding;

import java.util.Arrays;

import static android.text.InputType.TYPE_NULL;

public class InstrCourseDetailFragment extends Fragment {

    private String courseFullName_;
    private String courseCode_;
    private String courseName_;

    private Course currentCourse_;
    private boolean isAssigned_;

    private CourseDAO courseDAO;
    private InstructorDAO instructorDAO;

    private CourseBookingDataBase db;
    private InstrCourseDetailFragmentBinding binding;

    private String loggedInstructorName_;
    private Instructor currentCourseInstructor_;
    private Instructor loggedInstructor_;

    private ArrayAdapter<String> daysAdapter_;

    //
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         binding = DataBindingUtil.inflate(
                inflater,
                R.layout.instr_course_detail_fragment,
                container,
                false);

        setHasOptionsMenu(true);

        courseFullName_ = InstrCourseDetailFragmentArgs.fromBundle(getArguments()).getCourseFullName();
        loggedInstructorName_ = InstrCourseDetailFragmentArgs.fromBundle(getArguments()).getInstructorName();
        isAssigned_ = InstrCourseDetailFragmentArgs.fromBundle(getArguments()).getIsAssigned();
        // is "assigned" will be used to know if we should open
        // the extra or not.

        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        // Initialize the DAOs
        courseDAO = db.courseDao();
        instructorDAO = db.instructorDao();

        //android.R.layout.simple_list_item_1
        daysAdapter_ = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, Arrays.asList(AppUtils.daysArray));

        //
        binding.day1AutoComplete.setAdapter(daysAdapter_);
        binding.day2AutoComplete.setAdapter(daysAdapter_);

        // Initialize the course and instructor variables
        currentCourse_ = new Course();
        currentCourseInstructor_ = new Instructor();
        loggedInstructor_ = new Instructor();

        if(courseFullName_ !=null){
            int nameSeparatorIndex = -1;
            nameSeparatorIndex = courseFullName_.indexOf("|");

            int parantheseIndex = courseFullName_.contains("(")
                    ?courseFullName_.indexOf("("):courseFullName_.length();

            // Extract the course name and code from the
            // full name passed as an argument
            courseName_ = courseFullName_.substring(0,nameSeparatorIndex).trim();
            courseCode_ = courseFullName_.substring(nameSeparatorIndex+1,parantheseIndex).trim();

            // Get the entire course info from the db
            // and put it in the currentCourse_ and the editText
            GetCourseTask getCourseTask = new GetCourseTask();
            getCourseTask.execute();

        }

        binding.teachThisSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //binding.teachThisSwitch.setText(AppUtils.STOP_TEACHING);
                    binding.courseExtraGroup.setVisibility(View.VISIBLE);
                    binding.saveButton.setEnabled(true);

                }else{
                    //binding.teachThisSwitch.setText(AppUtils.TEACH_THIS);
                    binding.courseExtraGroup.setVisibility(View.GONE);

                    binding.editCourseName.setText(courseName_);
                    binding.editCourseName.setInputType(TYPE_NULL);

                    binding.editCourseCode.setText(courseCode_);
                    binding.editCourseCode.setInputType(TYPE_NULL);

                    binding.editCourseDescription.setText("");
                    binding.day1AutoComplete.setText("");
                    binding.day2AutoComplete.setText("");
                    binding.editCapacity.setText("");

                    binding.saveButton.setEnabled(true);
                }
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.editCourseName.getText().toString().isEmpty() ||
                        binding.editCourseCode.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Enter the name and the code",Toast.LENGTH_LONG).show();
                }else{

                    currentCourse_.courseName = binding.editCourseName.getText().toString();
                    currentCourse_.courseCode = binding.editCourseCode.getText().toString();

                    // The teacher decided to teach the course
                    currentCourse_.courseDescription = binding.editCourseDescription.getText().toString();

                    // Take the first and second period
                    currentCourse_.day1 = binding.day1AutoComplete.getText().toString();
                    currentCourse_.hour1 = binding.editCourseHour1.getText().toString();

                    currentCourse_.day2 = binding.day2AutoComplete.getText().toString();
                    currentCourse_.hour2 = binding.editCourseHour2.getText().toString();

                    // Take the capacity and change it into an int
                    currentCourse_.capacity = !(binding.editCapacity.getText().toString().isEmpty())
                            ?Integer.valueOf(binding.editCapacity.getText().toString().trim()) :0;

                    // Update the teacher's id
                    currentCourse_.teacher_id = binding.teachThisSwitch.isChecked() ? loggedInstructor_.id:-1;

                    // Save the course
                    SaveCourseTask saveCourseTask = new SaveCourseTask();
                    saveCourseTask.execute();
                }
            }
        });
        // Get the class from the database
        // and put it inside the "currentClass_" variable
            /*GetCourseTask  getCourseTask = new GetCourseTask();
            getCourseTask.execute();*/
        return binding.getRoot();
    }

    private class GetCourseTask extends AsyncTask<Integer,Void, String> {
        @Override
        protected String doInBackground(Integer... operation) {

            // The current course we are dealing with
            currentCourse_ = courseDAO.findByName(courseName_);

            // Get the instructor associated with this course
            currentCourseInstructor_ = instructorDAO.findById(currentCourse_.teacher_id);

            // Get the instructor loggedIn
            loggedInstructor_ = instructorDAO.findByName(loggedInstructorName_.trim());

            if(currentCourseInstructor_!=null){
                return currentCourseInstructor_.name_;
            }

            // Null is always returned!!
            return null;
        }

        @Override
        protected void onPostExecute(String instrName) {

            // Place the name and code onto
            // the corresponding edit text
            binding.editCourseName.setText(currentCourse_.courseName);
            binding.editCourseCode.setText(currentCourse_.courseCode);

            if(instrName != null) {
                if(!isAssigned_) {

                    // Make the switch invisble loggedInstructorName_.trim() !=instrName.trim()
                    binding.teachThisSwitch.setVisibility(View.GONE);
                    // If another person is teaching this, display its info
                    binding.instructorText.setText("Instructor : "+instrName);
                    binding.instructorText.setVisibility(View.VISIBLE);

                    // Make the extra group desappear
                    binding.courseExtraGroup.setVisibility(View.GONE);

                    // Make the save button disabled
                    binding.saveButton.setEnabled(false);
                }
                else {
                    // The

                    binding.teachThisSwitch.setChecked(true);

                    //Put the course description
                    binding.editCourseDescription.setText(currentCourse_.courseDescription);

                    // Put the day 1 and 2 getDayIndex(currentCourse_.day1)
                    binding.day1AutoComplete.setText(currentCourse_.day1);
                    binding.editCourseHour1.setText(currentCourse_.hour1);

                    // Put the hour 1 and 2
                    binding.day2AutoComplete.setText(currentCourse_.day2);
                    binding.editCourseHour2.setText(currentCourse_.hour2);

                    // Put the capacity
                    binding.editCapacity.setText(String.valueOf(currentCourse_.capacity));

                    // Now, make the extra infos visible
                    binding.courseExtraGroup.setVisibility(View.VISIBLE);

                    // Display the save button
                    binding.saveButton.setEnabled(true);
                }

            }else{
                //Toast.makeText(getContext())
                // You can be the teacher
            }

            if(loggedInstructorName_ != null){
                // How to know if the teacher corresponds
                // to him.

                // In case the course already has day and everything
                // put it on. I need to find it tho


                // Get the entire course info from the db
                /*GetCourseTask getCourseTask = new GetCourseTask();
                getCourseTask.execute();*/
            }

            super.onPostExecute(instrName);
        }

    }

    private class SaveCourseTask extends AsyncTask<Integer,Void, Long>{
        @Override
        protected Long doInBackground(Integer... integers) {
            return courseDAO.insertOneCourse(currentCourse_);
        }

        @Override
        protected void onPostExecute(Long modif) {
            if(modif > 0){
                Toast.makeText(getContext(),"Course saved successfully",Toast.LENGTH_LONG).show();
            }
            NavDirections direction = InstrCourseDetailFragmentDirections.actionInstrCourseDetailFragmentToInstrCourseListFragment();
            NavHostFragment.findNavController(getParentFragment()).navigate(direction);
            super.onPostExecute(modif);
        }
    }
    public int getDayIndex(String day){
        int index = 0;

        if(day == AppUtils.TUESDAY){
            index = 1;
        }else if(day == AppUtils.WEDNESDAY){
            index=  2;
        }else if(day == AppUtils.THURSDAY){
            index = 3;
        }else if(day == AppUtils.FRIDAY){
            index = 4;
        }else if(day == AppUtils.SATURDAY){
            index = 5;
        }
        else if(day == AppUtils.SUNDAY){
            index = 6;
        }
        return index;
    }

}
