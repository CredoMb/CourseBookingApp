package com.example.android.coursebookingapp.screens.instructorFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.coursebookingapp.database.CourseBookingDataBase;
import com.example.android.coursebookingapp.database.CourseDAO;

import java.util.ArrayList;

public class InstrCourseListFragment extends Fragment {

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
        //return inflater;
    }
}
