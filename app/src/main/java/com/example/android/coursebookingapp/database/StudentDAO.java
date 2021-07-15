package com.example.android.coursebookingapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
@Dao
public interface StudentDAO {
        @Query("SELECT * FROM students")
        List<Student> getAll();

        @Query("SELECT * FROM students WHERE student_id IN (:studentIds)")
        List<Student> loadAllByIds(int[] studentIds);

        @Query("SELECT * FROM students WHERE name LIKE :name_ LIMIT 1 ")
        Student findByName(String name_);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        long insertStudentCourseCrossRef(StudentCourseCrossRef studentCourseCrossRef);

        // Query to get an Student using his
        // username and password
        @Query("SELECT * FROM students WHERE username LIKE :uName AND " +
                "password LIKE :pWord LIMIT 1")
        Student findByUsernameAndPassword(String uName, String pWord);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(Student... students);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertOneStudent(Student student);

        // Do we
        @Transaction
        @Query("SELECT * FROM students")
        public List<StudentWithCourses> getStudentsWithCourses();

        /*@Delete
        void delete(Student student);*/

        @Query("DELETE FROM StudentCourseCrossRef WHERE student_id = :stuId AND "+
                " course_id = :courseId ")
        int deleteStudentCourseCrossRef(int stuId,int courseId);

        @Query("DELETE FROM students WHERE name = :iName AND "+
                " username = :uName")
        int delete(String iName,String uName);
}
