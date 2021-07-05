package com.example.android.coursebookingapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "instructors")
public class Instructor {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "username")
    public String userName;

    @ColumnInfo(name = "password")
    public String passWord;

    @ColumnInfo(name = "name")
    public String name_;

    public Instructor(String userName,String passWord, String name_){

        this.userName = userName;
        this.passWord = passWord;
        this.name_ = name_;
    }

    /*
    @ColumnInfo(name = "last_name")
    public String lastName;*/

    /*

    We don't need this
    @ColumnInfo(name = "course_list")
    public ArrayList<String> courseList; */

    // What the best way to access the
    // course ?  By it course code, I guess
    // Can we save a list of sum in a column ?
}
