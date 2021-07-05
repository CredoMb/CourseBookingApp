package com.example.android.coursebookingapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "admins")
public class Admin {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "username")
    public String userName;

    @ColumnInfo(name = "password")
    public String passWord;

    @ColumnInfo(name = "name")
    public String Name;

    public Admin(String userName, String passWord, String Name){
        this.Name = Name;
        this.passWord = passWord;
        this.userName = userName;
    }
    /*
    @ColumnInfo(name = "last_name")
    public String lastName;*/

}
