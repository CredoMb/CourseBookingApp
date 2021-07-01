package com.example.android.coursebookingapp;

public final class AppUtils {

    // The different role a user
    // can have
    public static int ROLE_ADMIN = 3;
    public static int ROLE_INSTRUCTOR = 4;
    public static int ROLE_STUDENT = 5;

    public static String DATA_BASE_NAME = "course_booking_database";

    // Action that can be made in the
    // app
    public static int ACTION_LOGIN = 1;
    public static int ACTION_SIGNUP = 2;
    public static Integer ACTION_SAVE = 9;
    public static Integer ACTION_DELETE = 10;

    // Actions to perform on the course
    // database
    public static int ADD_COURSE = 6;
    public static int REMOVE_COURSE = 7;
    public static int GET_ALL_COURSES = 8;

    //
    public static String INSTRUCTION_NAME_EXTRA = "intruction_name";
}
