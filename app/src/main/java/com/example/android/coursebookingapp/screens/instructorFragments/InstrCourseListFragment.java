package com.example.android.coursebookingapp.screens.instructorFragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.android.coursebookingapp.AppUtils;
import com.example.android.coursebookingapp.InstructorActivity;
import com.example.android.coursebookingapp.MainActivity;
import com.example.android.coursebookingapp.R;
import com.example.android.coursebookingapp.database.Course;
import com.example.android.coursebookingapp.database.CourseBookingDataBase;
import com.example.android.coursebookingapp.database.CourseDAO;
import com.example.android.coursebookingapp.database.Instructor;
import com.example.android.coursebookingapp.database.InstructorDAO;
import com.example.android.coursebookingapp.databinding.InstrCourseListFragmentBinding;
import com.example.android.coursebookingapp.screens.adminFragments.AdminCourseListFragmentDirections;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.coursebookingapp.AppUtils.INSTRUCTOR_NAME_EXTRA;

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

    private RelativeLayout emptyGroupView_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // return inflater;
        //
        InstrCourseListFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.instr_course_list_fragment,
                container,
                false);

        // get the intent extra from
        instructorName_ = getActivity().getIntent().getStringExtra(AppUtils.INSTRUCTOR_NAME_EXTRA).trim();

        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        courseDAO = db.courseDao();
        instructorDAO = db.instructorDao();

        emptyGroupView_ = binding.emptyGroupView;

        // Create the adapter to hold the list of courses
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        // Start a background thread to get all the courses
        // from the database
        CourseOperationsTask courseOperations = new CourseOperationsTask();
        courseOperations.execute();

        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String courseNameAndCode = adapter.getItem(position);

                // Search for the course and determine
                NavDirections direction = InstrCourseListFragmentDirections.actionInstrCourseListFragmentToInstrCourseDetailFragment()
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

        // We need the id
        getActivity().setTitle("Instructor ");
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
                        teachingText = AppUtils.TEACHING_TEXT;
                    }else{
                        teachingText = "";
                    }

                    courseStringList.add(currCourse.courseName + " | "+currCourse.courseCode + " "+teachingText);
                    // What to do now ?
                    // I don't know
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
                emptyGroupView_.setVisibility(View.GONE);
            }else{
                emptyGroupView_.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(courseList);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.instr_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.searchMenuButton){
            NavDirections  direction = InstrCourseListFragmentDirections.actionInstrCourseListFragmentToInstrCourseSearchFragment()
                    .setInstructorName(instructorName_);
            NavHostFragment.findNavController(getParentFragment()).navigate(direction);
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
        menu.findItem(R.id.listMenuButton).setEnabled(false);
        super.onPrepareOptionsMenu(menu);
    }
}
