package com.example.android.coursebookingapp.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.coursebookingapp.database.Instructor;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface InstructorDAO {
    @Query("SELECT * FROM instructors")
    List<Instructor> getAll();

    @Query("SELECT COUNT(*) FROM instructors")
    int count();
    /*
    @Query("SELECT * FROM instructors WHERE id IN (:instructorIds)")
    List<Instructor> loadAllByIds(int[] instructorIds);*/

    @Query("SELECT * FROM instructors WHERE name LIKE :name_ LIMIT 1 ")
    Instructor findByName(String name_);

    // Query to get an instructor using his
    // username and password
    @Query("SELECT * FROM instructors WHERE username LIKE :uName AND " +
            "password LIKE :pWord LIMIT 1")
    Instructor findByUsernameAndPassword(String uName, String pWord);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Instructor... instructors);

    @Insert(entity = Instructor.class)
    void insertOneInstructor(Instructor instructor);

    /*
    @Delete
    void delete(Instructor instructor);*/

    @Query("DELETE FROM instructors WHERE name = :iName AND "+
            " username = :uName")
    int delete(String iName,String uName);
}
