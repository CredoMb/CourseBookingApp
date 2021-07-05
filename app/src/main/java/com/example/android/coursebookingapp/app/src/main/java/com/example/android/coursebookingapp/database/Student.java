package com.example.android.coursebookingapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "username")
    public String userName;

    @ColumnInfo(name = "password")
    public String passWord;

    @ColumnInfo(name = "name")
    public String name_;

    public Student(String userName,String passWord, String name_){
        this.userName = userName;
        this.passWord = passWord;
        this.name_ = name_;
    }

}
