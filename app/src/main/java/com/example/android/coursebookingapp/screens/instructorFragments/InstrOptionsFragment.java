package com.example.android.coursebookingapp.screens.instructorFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.android.coursebookingapp.R;
import com.example.android.coursebookingapp.databinding.AdminOptionsFragmentBinding;
import com.example.android.coursebookingapp.screens.adminFragments.AdminOptionsFragmentDirections;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class InstrOptionsFragment extends Fragment {

    private int CheckedButtonId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        AdminOptionsFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.admin_options_fragment,
                container,
                false);

        CheckedButtonId = -1;

        binding.roleToggleButtons.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                CheckedButtonId = checkedId;
            }
        });

        binding.exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections direction;

                if(CheckedButtonId== R.id.courses_option_button){
                    direction = AdminOptionsFragmentDirections.actionAdminOptionsFragmentToCourseListFragment();
                    Navigation.findNavController(v).navigate(direction);
                }else if(CheckedButtonId== R.id.instructors_option_button){
                    direction = AdminOptionsFragmentDirections.actionAdminOptionsFragmentToInstructorListFragment();
                    Navigation.findNavController(v).navigate(direction);
                }else if(CheckedButtonId== R.id.student_option_button) {
                    direction = AdminOptionsFragmentDirections.actionAdminOptionsFragmentToStudentListFragment();
                    Navigation.findNavController(v).navigate(direction);
                }
            }
        });

        return binding.getRoot();
    }
}
