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
import com.example.android.coursebookingapp.database.InstructorDAO;
import com.example.android.coursebookingapp.databinding.AdminInstructorDetailFragmentBinding;

public class AdminInstructorDetailFragment extends Fragment {
    
    private InstructorDAO instructorDAO;
    
    private String instructorNameAndUsername_;
    private String instructorName_;
    private String instructorUsername_;
    
    //
    private CourseBookingDataBase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        AdminInstructorDetailFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.admin_instructor_detail_fragment,
                container,
                false);

        instructorNameAndUsername_ = AdminInstructorDetailFragmentArgs.fromBundle(getArguments()).getNameAndUname();

        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();
        
        instructorDAO = db.instructorDao();

        if(instructorNameAndUsername_ !=null){
            int nameSeparatorIndex = -1;
            nameSeparatorIndex = instructorNameAndUsername_.indexOf("|");

            // Extract the instructor name and code from the
            // full name passed as an argument
            instructorName_ = instructorNameAndUsername_.substring(0,nameSeparatorIndex).trim();
            instructorUsername_ = instructorNameAndUsername_.substring(nameSeparatorIndex+1, instructorNameAndUsername_.length()).trim();

            // Place the name and code onto
            // the corresponding edit text
            binding.editInstName.setText(instructorName_.trim());
            binding.editInstUsername.setText(instructorUsername_.trim());
        }

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteInstructorTask deleteInstructorTask = new DeleteInstructorTask();

                instructorUsername_ = binding.editInstUsername.getText().toString();
                instructorName_ = binding.editInstName.getText().toString();

                if(instructorName_.isEmpty() || instructorUsername_.isEmpty()){
                    Toast.makeText(getContext(),"Make sure no field is empty",Toast.LENGTH_LONG).show();
                }else{
                    deleteInstructorTask.execute();
                }
            }
        });

        getActivity().setTitle("Instructor Detail");
        return binding.getRoot();
    }

    private class DeleteInstructorTask extends AsyncTask<Void,Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... none) {
                int nbDel = instructorDAO.delete(instructorName_, instructorUsername_);

                if( nbDel> 0){
                    return true;
                }else{
                    return false;
                }
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if(status){
                Toast.makeText(getContext(),"Instructor Deleted",Toast.LENGTH_LONG).show();
                NavDirections direction = AdminInstructorDetailFragmentDirections.actionInstructorDetailFragmentToInstructorListFragment2();
                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
            }else{
                Toast.makeText(getContext(),"Operation failed",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(status);
        }
    }
}
