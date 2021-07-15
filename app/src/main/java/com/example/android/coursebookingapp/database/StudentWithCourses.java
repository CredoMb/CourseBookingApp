package com.example.android.coursebookingapp.database;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class StudentWithCourses {
    @Embedded
    public Student student;
    @Relation(
            parentColumn = "student_id",
            entityColumn = "course_id",
            associateBy = @Junction(StudentCourseCrossRef.class)
    )

    //
    public List<Course> courses;
}
