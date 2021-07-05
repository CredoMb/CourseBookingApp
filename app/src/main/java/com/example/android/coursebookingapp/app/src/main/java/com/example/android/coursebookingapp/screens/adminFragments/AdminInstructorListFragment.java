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
import com.example.android.coursebookingapp.database.Instructor;
import com.example.android.coursebookingapp.R;
import com.example.android.coursebookingapp.database.CourseBookingDataBase;
import com.example.android.coursebookingapp.database.InstructorDAO;
import com.example.android.coursebookingapp.databinding.AdminInstructorListFragmentBinding;

import java.util.ArrayList;
import java.util.List;


public class AdminInstructorListFragment extends Fragment {

    private CourseBookingDataBase db;
    private ArrayAdapter<String> adapter;

    private InstructorDAO instructorDAO;
    private RelativeLayout emptyGroupView_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        AdminInstructorListFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.admin_instructor_list_fragment,
                container,
                false);

        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        instructorDAO = db.instructorDao();

        // Create the adapter to hold the list of instructors
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        InstructorOperationsTask instructorOperationsTask = new InstructorOperationsTask();
        instructorOperationsTask.execute();

        emptyGroupView_ = binding.emptyGroupView;

        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String InstructNameAndUname = adapter.getItem(position);

                NavDirections direction = AdminInstructorListFragmentDirections.actionInstructorListFragmentToInstructorDetailFragment()
                        .setNameAndUname(InstructNameAndUname);
                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
                // Separate the text
            }
        });

        getActivity().setTitle("Instructor list");
        return binding.getRoot();
    }

    // To retrieve all the elements
    // in a background thread
    private class InstructorOperationsTask extends AsyncTask<Integer,Void,List<String>> {
        @Override
        protected List<String> doInBackground(Integer... operation) {

            List<Instructor> allInstructor = instructorDAO.getAll();
            List<String> instructorStringList = new ArrayList<String>();

            if(!allInstructor.isEmpty()){
                for(int i=0; i<allInstructor.size();i++){
                    instructorStringList.add(allInstructor.get(i).name_ + " | "+allInstructor.get(i).userName);
                }
                return instructorStringList;
            };
            return null;

        }

        @Override
        protected void onPostExecute(List<String> instructorList) {

            if(instructorList != null) {
                //instructorArrList_ = (ArrayList<String>) instructorList;
                adapter.addAll(instructorList);
                synchronized(adapter){
                    adapter.notifyAll();
                }
                emptyGroupView_.setVisibility(View.GONE);
            }else{
                emptyGroupView_.setVisibility(View.VISIBLE);
            }
            // Add it to the listView here
            super.onPostExecute(instructorList);
        }
    }
}
