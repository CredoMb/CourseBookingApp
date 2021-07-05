package com.example.android.coursebookingapp.screens.adminFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import com.example.android.coursebookingapp.database.StudentDAO;
import com.example.android.coursebookingapp.databinding.StudentDetailFragmentBinding;

public class AdminStudentDetailFragment extends Fragment {

    private StudentDAO studentDAO;
    
    private String studentNameAndUsername_;
    private String studentName_;
    private String studentUsername_;

    private CourseBookingDataBase db;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        StudentDetailFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.student_detail_fragment,
                container,
                false);

        studentNameAndUsername_= AdminStudentDetailFragmentArgs.fromBundle(getArguments()).getNameAndUname();
        
        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        studentDAO = db.studentDao();

        if(studentNameAndUsername_ !=null){
            int nameSeparatorIndex = -1;
            nameSeparatorIndex = studentNameAndUsername_.indexOf("|");

            // Extract the student name and code from the
            // full name passed as an argument
            studentName_ = studentNameAndUsername_.substring(0,nameSeparatorIndex).trim();
            studentUsername_ = studentNameAndUsername_.substring(nameSeparatorIndex+1, studentNameAndUsername_.length()).trim();

            // Place the name and code onto
            // the correspondin0g edit text
            binding.editStuName.setText(studentName_.trim());
            binding.editStuUsername.setText(studentUsername_.trim());
        }

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletestudentTask deletestudentTask = new DeletestudentTask();

                studentUsername_ = binding.editStuUsername.getText().toString();
                studentName_ = binding.editStuName.getText().toString();

                if(studentName_.isEmpty() || studentUsername_.isEmpty()){
                    Toast.makeText(getContext(),"Make sure no field is empty",Toast.LENGTH_LONG).show();
                }else{
                    deletestudentTask.execute();
                }
            }
        });

        getActivity().setTitle("Student Detail");
        return binding.getRoot();
    }

    private class DeletestudentTask extends AsyncTask<Void,Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... none) {
            int nbDel = studentDAO.delete(studentName_, studentUsername_);

            if( nbDel> 0){
                return true;
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if(status){
                Toast.makeText(getContext(),"student Deleted",Toast.LENGTH_LONG).show();
                NavDirections direction = AdminStudentDetailFragmentDirections.actionStudentDetailFragmentToStudentListFragment();
                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
            }else{
                Toast.makeText(getContext(),"Operation failed",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(status);
        }
    }

}

