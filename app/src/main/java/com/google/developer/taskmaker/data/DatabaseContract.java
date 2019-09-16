package com.google.developer.taskmaker.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    //Database schema information
    public static final String TABLE_TASKS = "tasks";

    public static final class TaskColumns implements BaseColumns {
        //Task description
        public static final String DESCRIPTION = "description";
        //Completed marker
        public static final String IS_COMPLETE = "is_complete";
        //Priority marker
        public static final String IS_PRIORITY = "is_priority";
        //Completion date (can be null)
        public static final String DUE_DATE = "due_date";
    }

    //Unique authority string for the content provider
    public static final String CONTENT_AUTHORITY = "com.google.developer.taskmaker";

    /* Sort order constants */
    //TODO Priority first, Completed last, the rest by date


    //TODO Completed last, then by date, followed by priority


    //TODO Base content Uri for accessing the provider


    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }
}
