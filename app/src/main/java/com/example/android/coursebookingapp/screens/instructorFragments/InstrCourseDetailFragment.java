package com.example.android.coursebookingapp.screens.instructorFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.android.coursebookingapp.databinding.AdminCourseDetailFragmentBinding;
import com.example.android.coursebookingapp.databinding.InstrCourseDetailFragmentBinding;
import com.example.android.coursebookingapp.databinding.InstrCourseListFragmentBinding;
import com.example.android.coursebookingapp.databinding.InstrCourseListFragmentBindingImpl;

public class InstrCourseDetailFragment extends Fragment {

    private String courseFullName_;
    private String courseCode_;
    private String courseName_;

    private Course currentCourse_;

    private CourseDAO courseDAO;
    private CourseBookingDataBase db;

    private boolean isAssigned;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        InstrCourseDetailFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.instr_course_detail_fragment,
                container,
                false);

        courseFullName_ = InstrCourseDetailFragmentArgs.fromBundle(getArguments()).getCourseFullName();
        isAssigned = InstrCourseDetailFragmentArgs.fromBundle(getArguments()).getIsAssigned();

        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        courseDAO = db.courseDao();
        //
        currentCourse_ = new Course();

        if(courseFullName_ !=null){
            int nameSeparatorIndex = -1;
            nameSeparatorIndex = courseFullName_.indexOf("|");

            // Extract the course name and code from the
            // full name passed as an argument
            courseName_ = courseFullName_.substring(0,nameSeparatorIndex).trim();
            courseCode_ = courseFullName_.substring(nameSeparatorIndex+1, courseFullName_.length()).trim();

            // Place the name and code onto
            // the corresponding edit text
            binding.editCourseName.setText(courseName_);
            binding.editCourseCode.setText(courseCode_);

            // Get the class from the database
            // and put it inside the "currentClass_" variable
            /*GetCourseTask  getCourseTask = new GetCourseTask();
            getCourseTask.execute();*/

        }

        return binding.getRoot();
    }

}
