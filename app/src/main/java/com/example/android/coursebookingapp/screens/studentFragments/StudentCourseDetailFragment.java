package com.example.android.coursebookingapp.screens.studentFragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.android.coursebookingapp.AppUtils;
import com.example.android.coursebookingapp.R;
import com.example.android.coursebookingapp.database.Course;
import com.example.android.coursebookingapp.database.CourseBookingDataBase;
import com.example.android.coursebookingapp.database.CourseDAO;

import com.example.android.coursebookingapp.database.Instructor;
import com.example.android.coursebookingapp.database.InstructorDAO;
import com.example.android.coursebookingapp.database.Student;
import com.example.android.coursebookingapp.database.StudentCourseCrossRef;
import com.example.android.coursebookingapp.database.StudentDAO;
import com.example.android.coursebookingapp.database.StudentWithCourses;
import com.example.android.coursebookingapp.databinding.InstrCourseDetailFragmentBinding;
import com.example.android.coursebookingapp.databinding.StudentCourseDetailFragmentBinding;
import com.example.android.coursebookingapp.screens.instructorFragments.InstrCourseDetailFragment;
import com.example.android.coursebookingapp.screens.instructorFragments.InstrCourseDetailFragmentDirections;

import java.util.ArrayList;
import java.util.List;

import static android.text.InputType.TYPE_NULL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentCourseDetailFragment} factory method to
 * create an instance of this fragment.
 */
public class StudentCourseDetailFragment extends Fragment {

    private String courseFullName_;
    private String courseCode_;
    private String courseName_;

    private Course currentCourse_;
    private boolean isTaking_;

    private CourseDAO courseDAO;
    private StudentDAO studentDAO;

    private CourseBookingDataBase db;
    private StudentCourseDetailFragmentBinding binding;

    private boolean hasConflict;
    private String loggedstudentName_;
    //private Student currentCoursestudent_;
    private Student loggedstudent_;

    private StudentCourseCrossRef currStudentCourseCrossRef_;

    private ArrayAdapter<String> daysAdapter_;

    private View.OnClickListener listener;

    private List<Course> enrolledCourseList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         binding = DataBindingUtil.inflate(
                inflater,
                R.layout.student_course_detail_fragment,
                container,
                false);

        courseFullName_ = StudentCourseDetailFragmentArgs.fromBundle(getArguments()).getCourseFullName();
        loggedstudentName_ = StudentCourseDetailFragmentArgs.fromBundle(getArguments()).getStudentName();
        isTaking_ =  StudentCourseDetailFragmentArgs.fromBundle(getArguments()).getIsTaking();

        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        // Initialize the DAOs
        courseDAO  = db.courseDao();
        studentDAO =  db.studentDao();
        hasConflict = false;

        // We need the id and the
        currStudentCourseCrossRef_ = new StudentCourseCrossRef();
        loggedstudent_ = new Student();
        currentCourse_ = new Course();
        //currentCourseInstructor_ = new Instructor();
        loggedstudent_ = new Student();

        if(courseFullName_ != null) {
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

        binding.enrollSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.saveButton.setEnabled(true);
                if(isChecked){
                    isTaking_ = true;
                }else{
                    isTaking_ = false;
                }
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*currentCourse_.courseName = binding.editCourseName.getText().toString();
                currentCourse_.courseCode = binding.editCourseCode.getText().toString();

                // The teacher decided to teach the course
                currentCourse_.courseDescription = binding.editCourseDescription.getText().toString();

                // Take the first and second period
                currentCourse_.day1 = binding.day1AutoComplete.getText().toString();
                currentCourse_.hour1 = binding.editCourseHour1.getText().toString();

                currentCourse_.day2 = binding.day2AutoComplete.getText().toString();
                currentCourse_.hour2 = binding.editCourseHour2.getText().toString();

                //currentCourse_.hour2 = binding.editCourseHour2.getText().toString();

                // Take the capacity and change it into an int
                currentCourse_.capacity = !(binding.editCapacity.getText().toString().isEmpty())
                        ?Integer.valueOf(binding.editCapacity.getText().toString().trim()) :0;
                */

                SaveCourseTask saveCourseTask = new SaveCourseTask();
                saveCourseTask.execute();

                // Save the course. What to do now
                /*if(hourValid){
                    SaveCourseTask saveCourseTask = new SaveCourseTask();
                    saveCourseTask.execute();
                }else{
                    Toast.makeText(getContext(),"Enter valid hours (00:00)",Toast.LENGTH_LONG).show();
                }*/

            }
        });
        //
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private class GetCourseTask extends AsyncTask<Integer,Void, Void> {
        @Override
        protected Void doInBackground(Integer... operation) {

            // The current course we are dealing with
            currentCourse_ = courseDAO.findByName(courseName_);

            // Get the instructor associated with this course
            //currentCourseInstructor_ = instructorDAO.findById(currentCourse_.teacher_id);

            // Get the instructor loggedIn
            loggedstudent_ = studentDAO.findByName(loggedstudentName_.trim());

            /*if(currentCourseInstructor_!=null){
                return currentCourseInstructor_.name_;
            }*/

            // Null is always returned!!
            return null;
        }

        @Override
        protected void onPostExecute(Void nothing) {

            // Place the name and code onto
            // the corresponding edit text
            binding.editCourseName.setText(currentCourse_.courseName);
            binding.editCourseCode.setText(currentCourse_.courseCode);

            if(isTaking_){
                binding.enrollSwitch.setChecked(true);
            }
            // Make the switch invisble loggedInstructorName_.trim() !=instrName.trim()

                    // If another person is teaching this, display its info
                    // binding.instructorText.setText("Instructor : "+instrName);

                    //Put the course description
                    binding.editCourseDescription.setText(currentCourse_.courseDescription);
                    binding.editCourseDescription.setInputType(TYPE_NULL);

                    // Put the day 1 and 2 getDayIndex(currentCourse_.day1)
                    binding.day1AutoComplete.setText(currentCourse_.day1);
                    binding.editCourseHour1.setText(currentCourse_.hour1);
                    binding.editCourseHour1.setInputType(TYPE_NULL);

                            // Put the hour 1 and 2
                    binding.day2AutoComplete.setText(currentCourse_.day2);
                    binding.editCourseHour2.setText(currentCourse_.hour2);
                    binding.editCourseHour2.setInputType(TYPE_NULL);

                    // Put the capacity
                    binding.editCapacity.setText(String.valueOf(currentCourse_.capacity));
                    binding.editCapacity.setInputType(TYPE_NULL);
                    // Now, make the extra infos visible
                    //binding.courseExtraGroup.setVisibility(View.VISIBLE);

                    super.onPostExecute(nothing);
                }


        }

    private class SaveCourseTask extends AsyncTask<Integer,Void, Integer>{

        // Get the cross table element.
        @Override
        protected Integer doInBackground(Integer... integers) {

            loggedstudent_ = studentDAO.findByName(loggedstudentName_.trim());
            currentCourse_ = courseDAO.findByName(courseName_.trim());

            //courseDAO.findCoursesByDayAndHour;

            // Get the course list of this mf
            List<StudentWithCourses> studentWithCourse = studentDAO.getStudentsWithCourses();
            List<Course> coursesOfStu = new ArrayList<Course>();

            for(int i =0; i<studentWithCourse.size();i++) {
                if(studentWithCourse.get(i).student.name_.trim().equals(loggedstudentName_.trim())){
                    coursesOfStu.addAll(studentWithCourse.get(i).courses);
                }
            }

            String day1 = binding.day1AutoComplete.getText().toString().trim();
            String day2 = binding.day2AutoComplete.getText().toString().trim();

            String hour1 = binding.editCourseHour1.getText().toString().trim();
            String hour2 = binding.editCourseHour2.getText().toString().trim();

            List<Course> firstList = courseDAO.findCoursesByDayAndHour(day1,hour1);
            List<Course> secList = courseDAO.findCoursesByDayAndHour(day2,hour2);

            if(coursesOfStu.isEmpty()){
                hasConflict = false;
            }else{
                Course tempCourse = new Course();

                for(int i=0; i<coursesOfStu.size();i++){
                    //
                    tempCourse = coursesOfStu.get(i);

                    if(courseDAO.findCoursesByDayAndHour(tempCourse.day1,tempCourse.hour1).size() > 1
                      || courseDAO.findCoursesByDayAndHour(tempCourse.day2,tempCourse.hour2).size() > 1){
                        hasConflict = true;
                    }
                }
            }

            /*
            if(firstList.size() > 1
                    || secList.size() > 1){
                hasConflict = true;
                Log.i("Detail","Conflit ejali ");
            }else{
                hasConflict = false;
            }*/


            // studentWithCourse.get(i).student.name_
            /*for(int i = 0; i<studentWithCourse.size();i++){
                //Log.i("Stu C List Fragment ","stu name  "+" "+studentName_);
                if(studentWithCourse.get(i).student.name_.equals(loggedstudentName_)  ){
                    //Log.i("Stu C List Fragment ","size  "+" "+String.valueOf(studentWithCourse.get(i).courses.size()));
                    coursesOfStu.addAll(studentWithCourse.get(i).courses) ;
                }
            }*/

            // Check for a possible conflict
            currStudentCourseCrossRef_ = new StudentCourseCrossRef(loggedstudent_.student_id,currentCourse_.course_id);
            Integer modif = -1;

            if(isTaking_ && !hasConflict){
                modif = (int) studentDAO.insertStudentCourseCrossRef(currStudentCourseCrossRef_);
            }else if(!isTaking_){
                modif = studentDAO.deleteStudentCourseCrossRef(loggedstudent_.student_id,currentCourse_.course_id);
            }
            // The problem is how to remove a many relation

            return modif;
        }

        @Override
        protected void onPostExecute(Integer modif) {
            /*
            if(modif > 0){
                Toast.makeText(getContext(),"Modifications saved successfully",Toast.LENGTH_LONG).show();
            }
            There is a conflict and he's not taking
            */
            
            if((!hasConflict && isTaking_) || !isTaking_) {
                // There's a conflict with ...
                NavDirections direction = StudentCourseDetailFragmentDirections.actionStudentCourseDetailFragmentToStudentCourseListFragment();
                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
            }else if(hasConflict && isTaking_){
                Toast.makeText(getContext(),"Sorry, there's a time conflict ",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(modif);
        }
    }
}