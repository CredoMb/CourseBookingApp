package com.example.android.coursebookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class InstructorActivity extends AppCompatActivity {


    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        setTitle("Admin Section");
        //this.getActionBar().setTitle("Admin Section");
        fm = this.getSupportFragmentManager();
    }
    // There should be a logout thing!
    //


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu,menu); // instructor menu?
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logoutMenuButton){
            int nbF =
                    fm.getBackStackEntryCount();
            for(int i =0; i<nbF;i++) {
                fm.popBackStack();
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}


