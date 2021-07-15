package com.example.android.coursebookingapp.database;

import androidx.room.Entity;
import androidx.room.Insert;

@Entity(primaryKeys = {"student_id", "course_id"})
public class StudentCourseCrossRef {
    public int student_id;
    public int course_id;
    
    public StudentCourseCrossRef(int student_id,int course_id){
        this.student_id = student_id;
        this.course_id = course_id;
    }

    public StudentCourseCrossRef(){};

    // We create an object of type "StudentCourseCrossRef"

    /**error: Not sure how to handle insert method's return type.
     int insertStudentCourseCrossRef(StudentCourseCrossRef studentCourseCrossRef);*/
}
