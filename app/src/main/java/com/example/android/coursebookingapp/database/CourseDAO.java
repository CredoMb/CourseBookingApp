package com.example.android.coursebookingapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CourseDAO {
    @Query("SELECT * FROM course")
    List<Course> getAll();

    // How can I convert it into a
    // list of string of the form "courseName | courseCode".
    // Ask for a specific format of 2 fiel

    /*
    @Query("SELECT * FROM course WHERE id IN (:courseIds)")
    List<Course> loadAllByIds(int[] courseIds);
    before updating, we find it
    */

    @Query("SELECT * FROM course WHERE code LIKE :courseCode LIMIT 1")
    Course findByCode(String courseCode);

    @Query("SELECT * FROM course WHERE name LIKE :courseName LIMIT 1")
    Course findByName(String courseName);

    @Query("SELECT * FROM course WHERE name LIKE :courseName AND code LIKE :courseCode LIMIT 1")
    Course findByNameAndCode(String courseName,String courseCode);

    //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Course... courses);

    // Returns the id of the element(entity = Course.class)
    // we have just inserted.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOneCourse(Course course);

    @Query("SELECT * FROM course WHERE name LIKE :courseInfo OR " +
            " code LIKE :courseInfo LIMIT 1")
    Course findByCodeOrName(String courseInfo);

    //
    @Query("SELECT * FROM course WHERE name LIKE :info OR " +
            " code LIKE :info OR day1 LIKE :info OR day2 LIKE :info")
    List<Course> findByAnyThing(String info);

    @Query("DELETE FROM course WHERE name = :courseName AND "+
    " code = :courseCode")
    int delete(String courseName,String courseCode);

    // Try to find by day and hour
    @Query("SELECT * FROM course WHERE day1 LIKE :day AND hour1 LIKE :hour " +
            "OR day2 LIKE :day AND hour2 LIKE :hour")
    List<Course> findCoursesByDayAndHour(String day,String hour);

    /*@Delete(entity = Course.class)
    int delete(Course course);
     AND " +
            "password LIKE :pWord LIMIT 1"*/
}
