package com.example.android.coursebookingapp.screens.instructorFragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.android.coursebookingapp.AppUtils;
import com.example.android.coursebookingapp.R;
import com.example.android.coursebookingapp.database.Course;
import com.example.android.coursebookingapp.database.CourseBookingDataBase;
import com.example.android.coursebookingapp.database.CourseDAO;
import com.example.android.coursebookingapp.database.Instructor;
import com.example.android.coursebookingapp.database.InstructorDAO;
import com.example.android.coursebookingapp.databinding.InstrCourseListFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class InstrCourseListFragment extends Fragment {

    private CourseDAO courseDAO;
    private InstructorDAO instructorDAO;
    //
    private String courseName_;
    private String courseCode_;

    private ArrayList<String> courseArrList_;
    private CourseBookingDataBase db;
    private ArrayAdapter<String> adapter;

    private Instructor currentInstructor_;
    private Intent intent;

    private String instructorName_;
    private boolean IM_TEACHING = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return inflater;
        //
        InstrCourseListFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.instr_course_list_fragment,
                container,
                false);

        // get the intent extra from
        instructorName_ = getActivity().getIntent().getStringExtra(AppUtils.INSTRUCTOR_NAME_EXTRA);

        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        courseDAO = db.courseDao();
        instructorDAO = db.instructorDao();

        // Create the adapter to hold the list of courses
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        // Start a background thread to get all the courses
        // from the database
        CourseOperationsTask courseOperations = new CourseOperationsTask();
        courseOperations.execute();

        // We need the id
        return binding.getRoot();
    }

    private class CourseOperationsTask extends AsyncTask<Integer,Void, List<String>> {
        @Override
        protected List<String> doInBackground(Integer... operation) {

            List<Course> allCourse = courseDAO.getAll();
            List<String> courseStringList = new ArrayList<String>();

            // The current course we are dealing with
            Course currCourse = new Course();
            String teachingText = "";

            // Get the instructor from the database
            currentInstructor_ = instructorDAO.findByName(instructorName_);

            if(!allCourse.isEmpty()){
                for(int i=0; i<allCourse.size();i++){
                    currCourse = allCourse.get(i);

                    if(currCourse.teacher_id == currentInstructor_.id) {
                        teachingText = "(teaching)";
                    }
                    courseStringList.add(allCourse.get(i).courseName + " | "+allCourse.get(i).courseCode + " "+teachingText);
                }
                return courseStringList;
            };
            return null;
        }

        @Override
        protected void onPostExecute(List<String> courseList) {

            if(courseList != null) {
                courseArrList_ = (ArrayList<String>) courseList;
                adapter.addAll(courseList);
                synchronized(adapter){
                    adapter.notifyAll();
                }

                //courseArrList_ = (ArrayList<String>) courseList;
            }
            // Add it to the listView here
            //
            super.onPostExecute(courseList);
        }
    }
}
