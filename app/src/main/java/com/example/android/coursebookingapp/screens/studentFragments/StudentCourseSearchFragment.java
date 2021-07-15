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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.android.coursebookingapp.databinding.StudentCourseSearchFragmentBinding;
import com.example.android.coursebookingapp.screens.instructorFragments.InstrCourseListFragmentDirections;
import com.example.android.coursebookingapp.screens.instructorFragments.InstrCourseSearchFragmentDirections;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentCourseSearchFragment} factory method to
 * create an instance of this fragment.
 */
public class StudentCourseSearchFragment extends Fragment {



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StudentCourseSearchFragment.
     */
    private String courseInfo;

    private CourseDAO courseDAO;
    private StudentDAO studentDAO;

    private StudentCourseCrossRef currStudentCourseCross_;
    private Student currStudent;
    
    private String currStudentName_;
    
    private Course currentCourse_;
    
    private CourseBookingDataBase db;
    
    private ArrayAdapter<String> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StudentCourseSearchFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.student_course_search_fragment,
                container,
                false);

        setHasOptionsMenu(true);
        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();
        
        currStudentName_ = StudentCourseSearchFragmentArgs.fromBundle(getArguments()).getStudentName();
        studentDAO = db.studentDao();
        courseDAO = db.courseDao();

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

        // What to do now ?

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String courseNameAndCode = adapter.getItem(position);

                // Search for the course and determine
                NavDirections direction = StudentCourseSearchFragmentDirections.actionStudentCourseSearchFragmentToStudentCourseDetailFragment()
                        .setStudentName(currStudentName_)
                        .setCourseFullName(courseNameAndCode);

                //
                if(courseNameAndCode.contains(AppUtils.TAKING_TEXT)) {
                    direction = StudentCourseSearchFragmentDirections.actionStudentCourseSearchFragmentToStudentCourseDetailFragment()
                            .setCourseFullName(courseNameAndCode)
                            .setIsTaking(true)
                            .setStudentName(currStudentName_);
                }

                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
                // Separate the text
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    
    private class CourseSearchTask extends AsyncTask<Integer,Void, List<String>> {
        @Override
        protected List<String> doInBackground(Integer... operation) {
            // Find the searched course and Instructor
            List<Course> foundCourseList = courseDAO.findByAnyThing(courseInfo);
            currStudent = studentDAO.findByName(currStudentName_);
            List<StudentWithCourses> studentWithCourse = studentDAO.getStudentsWithCourses();

            String takingText = "";
            List<String> courseStringList = new ArrayList<String>();
            Course currCourse = new Course();

            if(!foundCourseList.isEmpty()){


                for(int i=0; i<foundCourseList.size();i++){
                    currCourse = foundCourseList.get(i);

                    for(int j=0; j<studentWithCourse.size();j++){

                        if(studentWithCourse.get(i).student.name_.equals(currStudent.name_)){

                            //
                            takingText="";
                            if(courseFound(studentWithCourse.get(i).courses, currCourse)){
                                takingText = " "+AppUtils.TAKING_TEXT;
                                Log.i("Search","Testage "+currCourse.courseName);
                            }else {

                            }
                            break;
                        //
                        }
                    }
                    courseStringList.add(currCourse.courseName + " | "+currCourse.courseCode + " "+takingText);
                }
                return courseStringList;
            };
            
            /*
            if(foundCourse != null){
                for(int i=0; i<studentWithCourse.size();i++){
                    if(studentWithCourse.get(i).student.student_id == currStudent.student_id){

                        if(courseFound(studentWithCourse.get(i).courses, foundCourse)){
                            takingText = " "+AppUtils.TAKING_TEXT;
                        }else {
                            takingText="";
                        }
                        //
                    }
                }
                return foundCourse.courseName + " | "+foundCourse.courseCode + takingText;
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(List<String> courseList) {

            if(!courseList.isEmpty()) {
                adapter.clear();
                adapter.addAll(courseList);
                
                synchronized(adapter){
                    adapter.notifyAll();
                }
            }
            super.onPostExecute(courseList);
        }
    }

    private boolean courseFound(List<Course> cList, Course toFind){

        for(int i =0; i<cList.size();i++) {
            return (cList.get(i).course_id == toFind.course_id)? true : false;
        }
        return false;
    }
}