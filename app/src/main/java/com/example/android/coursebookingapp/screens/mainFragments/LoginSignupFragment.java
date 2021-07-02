package com.example.android.coursebookingapp.screens.mainFragments;

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
import com.example.android.coursebookingapp.database.Admin;
import com.example.android.coursebookingapp.database.AdminDAO;
import com.example.android.coursebookingapp.database.CourseBookingDataBase;
import com.example.android.coursebookingapp.database.CourseDAO;
import com.example.android.coursebookingapp.database.Instructor;
import com.example.android.coursebookingapp.database.InstructorDAO;
import com.example.android.coursebookingapp.database.Student;
import com.example.android.coursebookingapp.database.StudentDAO;
import com.example.android.coursebookingapp.databinding.LoginSignupFragmentBinding;

public class LoginSignupFragment extends Fragment {

    private CourseBookingDataBase db;

    private AdminDAO adminDAO;
    private CourseDAO courseDAO;
    private InstructorDAO instructorDAO;
    private StudentDAO studentDAO;

    private String password_;
    private String username_;
    private String name_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LoginSignupFragmentBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.login_signup_fragment,
                container,
                false);


        // Make an instance of the database
        db = Room.databaseBuilder(getContext(),
                CourseBookingDataBase.class, AppUtils.DATA_BASE_NAME).build();

        // Initialize all the DAO objects
        adminDAO = db.adminDao();
        courseDAO = db.courseDao();
        instructorDAO = db.instructorDao();
        studentDAO = db.studentDao();

        // Get the role and the action that the
        // user want to perform
        int role = LoginSignupFragmentArgs.fromBundle(getArguments()).getRole();
        int action = LoginSignupFragmentArgs.fromBundle(getArguments()).getAction();

        //loginSignupFragmentArgs.getRole();
        //val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()

        if(action == AppUtils.ACTION_LOGIN) {
            binding.signupLoginButton.setText("Login");
            binding.loginSignupTitle.setText(matchRoleWithHeader(role)+" Login");

            // Create the admin account
            // incase it's not already there
            InsertionTask insertionTask = new InsertionTask();
            insertionTask.execute(AppUtils.ROLE_ADMIN);
            // I don't know what to do, fuck
        } else {
            // The confirmation button will be signup
            binding.signupLoginButton.setText("Signup");
            // Make the reenter password
            // and name field visible
            binding.reenterPasswordOutlinedTextField.setVisibility(View.VISIBLE);
            binding.nameOutlinedTextField.setVisibility(View.VISIBLE);

            // Set the text for the title
            binding.loginSignupTitle.setText(matchRoleWithHeader(role)+" Signup");
        }

        // Place a listener onto the login_signup button
        // We need to use the database here
        binding.signupLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(action == AppUtils.ACTION_LOGIN){

                    username_ = binding.editUsername.getText().toString();
                    password_ = binding.editPassword.getText().toString();

                    if(username_.isEmpty() || password_.isEmpty()) {
                        Toast.makeText(getContext(),"Please make sure to complete the username and the password",Toast.LENGTH_LONG).show();
                    }else {
                        RetrieveTask retrieveTask = new RetrieveTask();

                        if(role == AppUtils.ROLE_STUDENT){
                            retrieveTask.execute(AppUtils.ROLE_STUDENT);
                        }else if(role == AppUtils.ROLE_INSTRUCTOR){
                            // Find the element in the
                            // database
                            retrieveTask.execute(AppUtils.ROLE_INSTRUCTOR);
                        } else if(role == AppUtils.ROLE_ADMIN){
                            retrieveTask.execute(AppUtils.ROLE_ADMIN);
                        }
                    }

                }else{

                    name_ = binding.editName.getText().toString();
                    username_ = binding.editUsername.getText().toString();
                    password_ = "";

                    if(name_.isEmpty() || username_.isEmpty() || binding.editPassword.getText().toString().isEmpty()
                            || binding.editPassword.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(),"Please make sure to complete all the fields",Toast.LENGTH_LONG).show();
                    }else {

                        if(binding.editPassword.getText().toString().equals(binding.editReenterPassword.getText().toString())) {
                            password_ = binding.editPassword.getText().toString();
                        }else {
                            Toast.makeText(getContext(),"The two passwords field should match",Toast.LENGTH_LONG).show();
                        }

                        InsertionTask insertionTask = new InsertionTask();
                        // Register a new element
                        if(role == AppUtils.ROLE_STUDENT){
                            insertionTask.execute(AppUtils.ROLE_STUDENT);
                        }else if(role == AppUtils.ROLE_INSTRUCTOR){
                            // Insert the new instructor
                            // into the database
                            insertionTask.execute(AppUtils.ROLE_INSTRUCTOR);
                        }
                    }

                }
            }
        });

        return binding.getRoot();
    }

    public String matchRoleWithHeader(int role) {

        if(role == AppUtils.ROLE_ADMIN) {
            return "Admin";
        }else if(role == AppUtils.ROLE_STUDENT) {
            return "Student";
        }else{
            return "Instructor";
        }
    }

    private class RetrieveTask extends AsyncTask<Integer,Void,Integer>{
        @Override
        protected Integer doInBackground(Integer... role) {

            // What happens if we do not find
            // the element in the database ?
            if(role[0] == AppUtils.ROLE_INSTRUCTOR) {
                Instructor instructor = instructorDAO.findByUsernameAndPassword(username_, password_);
                if(instructor!=null){
                    name_ = instructor.name_;
                }
                return AppUtils.ROLE_INSTRUCTOR;
            }else if(role[0] == AppUtils.ROLE_STUDENT){
                Student student = studentDAO.findByUsernameAndPassword(username_, password_);
                if(student!=null){
                    name_ = student.name_;
                }
                return AppUtils.ROLE_STUDENT;

            }else if(role[0] == AppUtils.ROLE_ADMIN){
                Admin admin = adminDAO.findByUsernameAndPassword(username_, password_);
                if(admin!=null){
                    name_ = admin.Name;
                }
                return AppUtils.ROLE_ADMIN;
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer role) {

            if(name_ !=null) {
                NavDirections direction = LoginSignupFragmentDirections.actionLoginSignupFragmentDestinationToWelcomeFragment()
                        .setName(name_)
                        .setRole(role);

                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
            }else{
                Toast.makeText(getContext(),"Sorry the person is not registered",Toast.LENGTH_LONG).show();
            }

            super.onPostExecute(role);
        }
    }


    private class InsertionTask extends AsyncTask<Integer,Void,Integer>{
        @Override
        protected Integer doInBackground(Integer... role) {
            if(role[0] == AppUtils.ROLE_INSTRUCTOR) {
                instructorDAO.insertOneInstructor(new Instructor(username_, password_, name_));
                return AppUtils.ROLE_INSTRUCTOR;
            }else if(role[0] == AppUtils.ROLE_STUDENT){
                studentDAO.insertOneStudent(new Student(username_, password_, name_));
                return AppUtils.ROLE_STUDENT;
            }else if(role[0] == AppUtils.ROLE_ADMIN){
                if(adminDAO.getAll().isEmpty()){
                    adminDAO.insertOneAdmin(new Admin("admin","admin123","Olivia Borel"));
                }
                return AppUtils.ROLE_ADMIN;
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer role) {
            if(role != AppUtils.ROLE_ADMIN && role >0){
                NavDirections direction = LoginSignupFragmentDirections.actionLoginSignupFragmentDestinationToWelcomeFragment()
                        .setName(name_)
                        .setRole(role);
                NavHostFragment.findNavController(getParentFragment()).navigate(direction);
            }else if(role <0){
                Toast.makeText(getContext(),"Sorry the user was not inserted ",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(role);
        }
    }
}