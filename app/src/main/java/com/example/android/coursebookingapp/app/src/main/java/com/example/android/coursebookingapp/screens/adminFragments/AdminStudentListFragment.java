package com.example.android.coursebookingapp.screens.adminFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.android.coursebookingapp.AppUtils;
import com.example.android.coursebookingapp.R;
import com.example.android.coursebookingapp.database.CourseBookingDataBase;
import com.example.android.coursebookingapp.database.Student;
import com.example.android.coursebookingapp.database.StudentDAO;
import com.example.android.coursebookingapp.database.Student;
import com.example.android.coursebookingapp.database.StudentDAO;
import com.example.android.coursebookingapp.databinding.StudentListFragmentBinding;
import com.example.android.coursebookingapp.databinding.StudentListFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminStudentListFragment extends Fragment {

    private CourseBookingDataBase db;
    private ArrayAdapter<String> adapter;

    private StudentDAO studentDAO;
    private RelativeLayout emptyGroupView_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        StudentListFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.student_list_fragment,
                container,
                false);

        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        studentDAO = db.studentDao();

        // Create the adapter to hold the list of students
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        StudentOperationsTask studentOperationsTask = new StudentOperationsTask();
        studentOperationsTask.execute();

        emptyGroupView_ = binding.emptyGroupView;

        binding.studentListView.setAdapter(adapter);

        // Build the list item click
        binding.studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String studentNameAndUname = adapter.getItem(position);
                NavDirections direction = AdminStudentListFragmentDirections.actionStudentListFragmentToStudentDetailFragment()
                        .setNameAndUname(studentNameAndUname);
                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
            }
        });

        getActivity().setTitle("Student list");
        return binding.getRoot();
    }

    // To retrieve all the elements
    // in a background thread
    private class StudentOperationsTask extends AsyncTask<Integer,Void,List<String>> {
        @Override
        protected List<String> doInBackground(Integer... operation) {

            List<Student> allStudent = studentDAO.getAll();
            List<String> studentStringList = new ArrayList<String>();

            if(!allStudent.isEmpty()){
                for(int i=0; i<allStudent.size();i++){
                    studentStringList.add(allStudent.get(i).name_ + " | "+allStudent.get(i).userName);
                }
                return studentStringList;
            };
            return null;

        }

        @Override
        protected void onPostExecute(List<String> studentList) {

            if(studentList != null) {
                //studentArrList_ = (ArrayList<String>) studentList;
                adapter.addAll(studentList);
                synchronized(adapter){
                    adapter.notifyAll();
                }
                emptyGroupView_.setVisibility(View.GONE);
            }else{
                emptyGroupView_.setVisibility(View.VISIBLE);
            }
            // Add it to the listView here
            super.onPostExecute(studentList);
        }
    }
    }
