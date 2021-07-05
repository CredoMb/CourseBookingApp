package com.example.android.coursebookingapp.screens.adminFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
import com.example.android.coursebookingapp.databinding.AdminCourseListFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminCourseListFragment extends Fragment {
    // Create the daos
    private CourseDAO courseDAO;
    //
    private String courseName_;
    private String courseCode_;

    private ArrayList<String> courseArrList_;
    private CourseBookingDataBase db;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        AdminCourseListFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.admin_course_list_fragment,
                container,
                false);

        //
        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        courseDAO = db.courseDao();

        // Create the adapter to hold the list of courses
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        // Start a background thread to get all the courses
        // from the database
        CourseOperationsTask courseOperations = new CourseOperationsTask();
        courseOperations.execute();

        //
        binding.listView.setAdapter(adapter);

        // In case there is no course,
        // display the empty state
        if(adapter.getCount() > 0){
           binding.emptyGroupView.setVisibility(View.VISIBLE);
        }else{
            binding.emptyGroupView.setVisibility(View.GONE);
        }

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String courseNameAndCode = adapter.getItem(position);


                NavDirections direction = AdminCourseListFragmentDirections.actionCourseListFragmentToCourseDetailFragment()
                        .setCourseFullName(courseNameAndCode);

                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
                // Separate the text
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections direction = AdminCourseListFragmentDirections.actionCourseListFragmentToCourseDetailFragment();
                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
            }
        });
        // Put together the adapter and the listView
        getActivity().setTitle("Course list");
        return binding.getRoot();
    }

    private class CourseOperationsTask extends AsyncTask<Integer,Void,List<String>> {
        @Override
        protected List<String> doInBackground(Integer... operation) {

            List<Course> allCourse = courseDAO.getAll();
            List<String> courseStringList = new ArrayList<String>();

            if(!allCourse.isEmpty()){
                for(int i=0; i<allCourse.size();i++){
                    courseStringList.add(allCourse.get(i).courseName + " | "+allCourse.get(i).courseCode);
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
