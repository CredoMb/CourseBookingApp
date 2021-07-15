package com.example.android.coursebookingapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "course",indices = {@Index(value = {"name","code"},unique = true)})
public class Course {

    @PrimaryKey(autoGenerate = true)
    public int course_id;

    @ColumnInfo(name = "name")
    @NonNull
    public String courseName;

    @ColumnInfo(name = "code")
    @NonNull
    public String courseCode;

    @ColumnInfo(name = "description")
    public String courseDescription;

    public Course(String courseName, String courseCode) {
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public Course(){}

    // The entire course
    // Save it We
    /*public Course(Course course){
        this.courseName = course.courseName;
        this.courseCode = course.courseCode;
        this.id = course.id;
    }*/

    @ColumnInfo(name = "teacher_id", defaultValue = "-1")
    public int teacher_id;

    // First time period
    @ColumnInfo(name = "day1")
    public String day1;
    @ColumnInfo(name = "hour1")
    public String hour1;

    // Second time period
    @ColumnInfo(name = "day2")
    public String day2;
    @ColumnInfo(name = "hour2")
    public String hour2;

    @ColumnInfo(name = "capacity")
    public int capacity;
}

