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
    public static String TAKING_TEXT = "(taking)";

    public static String TEACH_THIS = "Teach course";
    public static String STOP_TEACHING = "Drop course";

    public static String INSTRUCTOR_NAME_EXTRA = "intruction_name";

    public static int FULL_TIME_TEXT_SIZE = 11;

    public static String STUDENT_NAME_EXTRA = "student_name";

    public static String validateTimeEntered(String fulltime){

        /*
        fulltime.replaceAll(":","");
        int indexUnion = fulltime.indexOf("-");*/

        // The first and second half "-"
        /*String start = fulltime.substring(0,indexUnion).trim();
        String end = fulltime.substring(indexUnion+1,fulltime.length()).trim();
        */

        // Make sure they are numbers
        if(!fulltime.isEmpty()){
            if(fulltime.trim().length() != AppUtils.FULL_TIME_TEXT_SIZE){
                return null;
            }else{
                // Now, we need to do what
                // we need to do
                return fulltime;
            }
        }
        return null;
    }

    public static boolean hasTimeConflict(String day1, String time1,String day2 ,String time2){
        time1.replaceAll(":","");
        time2.replaceAll(":","");

        // Take
        int endTimeFirst = Integer.valueOf(time1.substring(0,time1.indexOf("-")));
        int startTimeSec = Integer.valueOf(time2.substring(time2.indexOf("-")+1,time2.length()));;

        //
        if(endTimeFirst >= startTimeSec && (day1.equals(day2))){
            return true;
        }
        return false;
    }
}
