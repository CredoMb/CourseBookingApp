package com.example.android.coursebookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {

    //private NavHostFragment navHostFragment;

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setTitle("Admin Section");
        //this.getActionBar().setTitle("Admin Section");
        fm = this.getSupportFragmentManager();
    }
    // There should be a logout thing!
    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logoutMenuButton){
            fm.getBackStackEntryCount();

            int nbF = fm.getFragments().size();

            //int f = 0;
            for(int i =0; i<nbF;i++) {
               // f++;
                fm.popBackStack();
            }
            //Toast.makeText(this,String.valueOf(f),Toast.LENGTH_LONG).show();

           Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}
