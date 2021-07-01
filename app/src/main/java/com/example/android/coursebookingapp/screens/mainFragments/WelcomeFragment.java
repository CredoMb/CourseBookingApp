package com.example.android.coursebookingapp.screens.mainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.coursebookingapp.AdminActivity;
import com.example.android.coursebookingapp.AppUtils;
import com.example.android.coursebookingapp.MainActivity;
import com.example.android.coursebookingapp.R;
import com.example.android.coursebookingapp.databinding.IntroductionFragmentBinding;
import com.example.android.coursebookingapp.databinding.WelcomeFragmentBinding;

public class WelcomeFragment extends Fragment {
    private int CheckedButtonId;

    private String name;
    private int role;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        WelcomeFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.welcome_fragment,
                container,
                false);

        this.setHasOptionsMenu(true);

        role = WelcomeFragmentArgs.fromBundle(getArguments()).getRole();
        name = WelcomeFragmentArgs.fromBundle(getArguments()).getName();


        binding.welcomeText.setText("Welcome "+name);
        if(!getRole(role).isEmpty()) {
            binding.roleText.setText("You logged in as a "+getRole(role));
        }

        if(role == AppUtils.ROLE_ADMIN) {
            binding.action1Button.setText("Admin Options");
        } else{
            binding.action1Button.setText("Get Started");
        }

        binding.action1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(role == AppUtils.ROLE_ADMIN) {
                    intent = new Intent(getActivity(), AdminActivity.class);
                    startActivity(intent);
                }
            }
        });

        return binding.getRoot();
    }

    private String getRole(int roleCode) {
        if(roleCode == AppUtils.ROLE_ADMIN)  {
            return "Admin";
        }else if(roleCode == AppUtils.ROLE_INSTRUCTOR) {
            return "Instructor";
        }else if (roleCode == AppUtils.ROLE_STUDENT) {
            return "Student";
        }
        return "";
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.admin_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logoutMenuButton){
            while(NavHostFragment.findNavController(getParentFragment()).popBackStack()){};
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}