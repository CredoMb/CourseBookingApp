package com.example.android.coursebookingapp.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class InstructorWithCourses {

    @Embedded
    public Instructor instructor;
    @Relation(
            // the column in the "instructor"
            // table.
            parentColumn = "id",
            entityColumn = "teacher_id"
    )
    public List<Course> courses;
}
