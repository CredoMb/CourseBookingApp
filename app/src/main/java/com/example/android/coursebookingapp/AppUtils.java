package com.example.android.coursebookingapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AppUtils {

    // Days of the week
    public static String MONDAY = "Monday";
    public static String TUESDAY = "Tuesday";
    public static String WEDNESDAY = "Wednesday";
    public static String THURSDAY = "Thursday";
    public static String FRIDAY = "Friday";
    public static String SATURDAY = "Saturday";
    public static String SUNDAY = "Sunday";


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
    // database
    public static int ADD_COURSE = 6;
    public static int REMOVE_COURSE = 7;
    public static int GET_ALL_COURSES = 8;

    public static String[] daysArray = {MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,
            SATURDAY,SUNDAY};

    //

    public static String TEACHING_TEXT = "(teaching)";

    public static String TEACH_THIS = "Teach course";
    public static String STOP_TEACHING = "Drop course";

    public static String INSTRUCTOR_NAME_EXTRA = "intruction_name";
}
