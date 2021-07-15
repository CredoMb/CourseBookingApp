package com.example.android.coursebookingapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.android.coursebookingapp.database.CourseDAO;

// ,
@Database(entities = {Course.class,Instructor.class,Admin.class,Student.class,StudentCourseCrossRef.class}, version = 1,exportSchema = false)
public abstract class CourseBookingDataBase extends RoomDatabase {

abstract public CourseDAO courseDao();
abstract public InstructorDAO instructorDao();
abstract public AdminDAO adminDao();
abstract public StudentDAO studentDao();
}