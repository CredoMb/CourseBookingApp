package com.example.android.coursebookingapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface AdminDAO {

    @Query("SELECT * FROM admins")
    List<Admin> getAll();
    /*
    @Query("SELECT * FROM admins WHERE id IN (:adminIds)")
    List<Admin> loadAllByIds(int[] adminIds);
    */

    @Query("SELECT * FROM admins WHERE name LIKE :name_ LIMIT 1 ")
    Admin findByName(String name_);

    @Query("SELECT * FROM admins WHERE username LIKE :uName AND " +
            "password LIKE :pWord LIMIT 1")
    Admin findByUsernameAndPassword(String uName, String pWord);

    @Insert(entity = Admin.class)
    void insertOneAdmin(Admin admin);
}
