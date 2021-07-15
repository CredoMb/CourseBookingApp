package com.example.android.coursebookingapp.screens.instructorFragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.coursebookingapp.AppUtils;
import com.example.android.coursebookingapp.InstructorActivity;
import com.example.android.coursebookingapp.MainActivity;
import com.example.android.coursebookingapp.R;
import com.example.android.coursebookingapp.database.Course;
import com.example.android.coursebookingapp.database.CourseBookingDataBase;
import com.example.android.coursebookingapp.database.CourseDAO;
import com.example.android.coursebookingapp.database.Instructor;
import com.example.android.coursebookingapp.database.InstructorDAO;
import com.example.android.coursebookingapp.databinding.InstrCourseSearchFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.coursebookingapp.AppUtils.INSTRUCTOR_NAME_EXTRA;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class InstrCourseSearchFragment extends Fragment {

    private String courseInfo;

    private CourseDAO courseDAO;
    private InstructorDAO instructorDAO;

    private CourseBookingDataBase db;

    private ArrayAdapter<String> adapter;
    private RelativeLayout emptyGroupView_;

    private String instructorName_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InstrCourseSearchFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.instr_course_search_fragment,
                container,
                false);

        setHasOptionsMenu(true);
        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        instructorName_ = InstrCourseSearchFragmentArgs.fromBundle(getArguments()).getInstructorName();

        courseDAO = db.courseDao();
        instructorDAO = db.instructorDao();

        // Create the adapter to hold the list of courses
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        binding.listView.setAdapter(adapter);

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.editCourseName.getText().toString().isEmpty()){

                    courseInfo = binding.editCourseName.getText().toString();

                    CourseSearchTask courseSearchTask = new CourseSearchTask();
                    courseSearchTask.execute();

                }else{
                    Toast.makeText(getContext(),"The search text should not be empty",Toast.LENGTH_LONG);
                }
            }
        });

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String courseNameAndCode = adapter.getItem(position);

                // Search for the course and determine
                NavDirections direction = InstrCourseSearchFragmentDirections.actionInstrCourseSearchFragmentToInstrCourseDetailFragment()
                        .setInstructorName(instructorName_)
                        .setCourseFullName(courseNameAndCode);

                if(courseNameAndCode.contains(AppUtils.TEACHING_TEXT)) {
                    direction = InstrCourseListFragmentDirections.actionInstrCourseListFragmentToInstrCourseDetailFragment()
                            .setCourseFullName(courseNameAndCode)
                            .setIsAssigned(true)
                            .setInstructorName(instructorName_);
                }

                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
                // Separate the text
            }
        });
        return binding.getRoot();
    }

    private class CourseSearchTask extends AsyncTask<Integer,Void, String> {
        @Override
        protected String doInBackground(Integer... operation) {

            // Find the searched course and Instructor
            Course foundCourse = courseDAO.findByCodeOrName(courseInfo);
            Instructor courseInstructor = instructorDAO.findByName(instructorName_);

            String teachingText = "";

            if(foundCourse != null){
                if(foundCourse.teacher_id == courseInstructor.id) {
                    teachingText = " "+AppUtils.TEACHING_TEXT;
                }else{
                    teachingText = "";
                }
                return foundCourse.courseName + " | "+foundCourse.courseCode + teachingText;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String courseFullName) {

            if(courseFullName != null) {
                adapter.add(courseFullName);
                synchronized(adapter){
                    adapter.notifyAll();
                }
            }
            super.onPostExecute(courseFullName);
        }
    }

    @Override
        public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
            inflater.inflate(R.menu.instr_menu, menu);
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.listMenuButton){
            Intent intent = new Intent(getActivity(), InstructorActivity.class);
            intent.putExtra(INSTRUCTOR_NAME_EXTRA,instructorName_);
            startActivity(intent);
        }else if(item.getItemId() == R.id.logoutMenuButton){

            int nbF = getActivity().getSupportFragmentManager().getBackStackEntryCount();

            for(int i =0; i<nbF;i++) {
                getActivity().getSupportFragmentManager().popBackStack();
            }

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.searchMenuButton).setEnabled(false);
        super.onPrepareOptionsMenu(menu);
    }

    // Le monde est à nous, à toi puis moi
}