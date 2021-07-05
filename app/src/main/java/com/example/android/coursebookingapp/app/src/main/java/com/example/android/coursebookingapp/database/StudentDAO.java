package com.example.android.coursebookingapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface StudentDAO {
        @Query("SELECT * FROM students")
        List<Student> getAll();

        @Query("SELECT * FROM students WHERE id IN (:studentIds)")
        List<Student> loadAllByIds(int[] studentIds);

        @Query("SELECT * FROM students WHERE name LIKE :name_ LIMIT 1 ")
        Student findByName(String name_);

        // Query to get an Student using his
        // username and password
        @Query("SELECT * FROM students WHERE username LIKE :uName AND " +
                "password LIKE :pWord LIMIT 1")
        Student findByUsernameAndPassword(String uName, String pWord);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(Student... students);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertOneStudent(Student student);

        @Delete
        void delete(Student student);
}
