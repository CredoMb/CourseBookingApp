package com.example.android.coursebookingapp.screens.studentFragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.example.android.coursebookingapp.AppUtils;
import com.example.android.coursebookingapp.MainActivity;
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
import com.example.android.coursebookingapp.databinding.StudentCourseListFragmentBinding;
import com.example.android.coursebookingapp.databinding.StudentListFragmentBinding;
import com.example.android.coursebookingapp.screens.instructorFragments.InstrCourseListFragment;
import com.example.android.coursebookingapp.screens.instructorFragments.InstrCourseListFragmentDirections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentCourseListFragment#} factory method to
 * create an instance of this fragment.
 */
public class StudentCourseListFragment extends Fragment {

    private CourseDAO courseDAO;
    private InstructorDAO instructorDAO;
    private StudentDAO studentDAO;

    // Klarissa
    private String courseName_;
    private String courseCode_;

    private ArrayList<String> courseArrList_;
    private CourseBookingDataBase db;
    private ArrayAdapter<String> adapter;

    private Instructor currentInstructor_;
    private Intent intent;

    private String studentName_;
    private boolean IM_taking = false;

    private RelativeLayout emptyGroupView_;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        StudentCourseListFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.student_course_list_fragment,
                container,
                false);

        // Get the intent extra from
        studentName_ = getActivity().getIntent().getStringExtra(AppUtils.STUDENT_NAME_EXTRA).trim();

        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        courseDAO = db.courseDao();
        instructorDAO = db.instructorDao();
        studentDAO = db.studentDao();

        emptyGroupView_ = binding.emptyGroupView;

        // Create the adapter to hold the list of courses
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        // Start a background thread to get all the courses
        // from the database
        GetCoursesTask courseOperations = new GetCoursesTask();
        courseOperations.execute();

        //adapter.add("simbad");
        binding.courseListView.setAdapter(adapter);
        
        binding.courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String courseNameAndCode = adapter.getItem(position);

                // Search for the course and determine
                NavDirections direction = StudentCourseListFragmentDirections.actionStudentCourseListFragmentToStudentCourseDetailFragment()
                        .setStudentName(studentName_)
                        .setIsTaking(true)
                        .setCourseFullName(courseNameAndCode);
                
                /*if(courseNameAndCode.contains(AppUtils.TAKING_TEXT)) {
                    direction = StudentCourseListFragmentDirections.actionStudentCourseListFragmentToStudentCourseDetailFragment()
                            .setCourseFullName(courseNameAndCode)
                            .setStudentName(studentName_);
                    // Oh don't do it, yo.. I'm the predator, nigga im alive
                    // 
                }*/

                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
                // Separate the text
            }
        });
        // Inflate the layout for this fragment.
        getActivity().setTitle(studentName_ +" Course List");

        return binding.getRoot();
    }


    private class GetCoursesTask extends AsyncTask<Integer,Void, List<String>> {
        @Override
        protected List<String> doInBackground(Integer... operation) {

            /*
            List<Course> allCourse = courseDAO.getAll();
            Student currStudent = studentDAO.findByName(studentName_);
            */

            // Log.i("Stu C List Fragment ","creation : student id "+String.valueOf(stuCross.student_id));

            List<StudentWithCourses> studentWithCourse = studentDAO.getStudentsWithCourses();
            List<Course> coursesOfStu = new ArrayList<Course>();

            // studentWithCourse.get(i).student.name_
            for(int i = 0; i<studentWithCourse.size();i++){
                //Log.i("Stu C List Fragment ","stu name  "+" "+studentName_);
                if(studentWithCourse.get(i).student.name_.equals(studentName_)  ){
                    //Log.i("Stu C List Fragment ","size  "+" "+String.valueOf(studentWithCourse.get(i).courses.size()));
                    coursesOfStu.addAll(studentWithCourse.get(i).courses) ;
                }
            }

            // The current course we are dealing with

            // Get the instructor from the database
            // Get the student with the paired courses: how ?
            /*currentInstructor_ = instructorDAO.findByName(instructorName_);*/

            Course currCourse = new Course();
            List<String> courseStringList = new ArrayList<String>();

            if(!coursesOfStu.isEmpty()){
                for(int i=0; i<coursesOfStu.size();i++){
                    currCourse = coursesOfStu.get(i);
                    courseStringList.add(currCourse.courseName + " | "+currCourse.courseCode);

                    // What to do now ?
                    // Log.i("Stu C List Fragment ","course name "+" "+currCourse.courseName);
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
        inflater.inflate(R.menu.student_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.searchMenuButton){
            //
            NavDirections  direction = StudentCourseListFragmentDirections.actionStudentCourseListFragmentToStudentCourseSearchFragment()
                    .setStudentName(studentName_);
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
        menu.findItem(R.id.myListMenuButton).setEnabled(false);
        super.onPrepareOptionsMenu(menu);
    }
}