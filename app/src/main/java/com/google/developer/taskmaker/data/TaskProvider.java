package com.google.developer.taskmaker.data;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class TaskProvider extends ContentProvider {
    private static final String TAG = TaskProvider.class.getSimpleName();

    private static final int CLEANUP_JOB_ID = 43;

    private static final int TASKS = 100;
    private static final int TASKS_WITH_ID = 101;

    private TaskDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.google.developer.taskmaker/tasks
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_TASKS,
                TASKS);

        // content://com.google.developer.taskmaker/tasks/id
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_TASKS + "/#",
                TASKS_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new TaskDbHelper(getContext());
        manageCleanupJob();
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null; /* Not used */
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        //TODO: Implement task query
        //TODO: Expected "query all" Uri: content://com.google.developer.taskmaker/tasks
        //TODO: Expected "query one" Uri: content://com.google.developer.taskmaker/tasks/{id}

        // Create a Cursor object to return
        Cursor returnCursor;

        // Get a reference to the readable SQLiteDatabase
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //TODO Match the URI passed in
        switch (sUriMatcher.match(uri)) {

            //TODO If the URI does not contain an ID of a single Task...
            case TASKS:

                //TODO Return a cursor that queries the database for all Tasks

                );

                //TODO Break from the switch statement
                break;

            //TODO If the URI does contain an ID of a single Task...
            case TASKS_WITH_ID:

                //TODO Return a cursor that queries the database for the one Task,
                // specifying its Id in the selection parameter

                );

                //TODO Break from the switch statement
                break;

            //TODO In the default case...
            default:

                //TODO Throw a UnsupportedOperationException
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        //TODO Get the Context
        Context context = getContext();

        //TODO If the context is not null...
        if (context != null) {

            //TODO Register to watch this Content URI for changes
        }

        //TODO Return the cursor
        return returnCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //TODO: Implement new task insert
        //TODO: Expected Uri: content://com.google.developer.taskmaker/tasks

        //TODO Get a reference to the writable SQLiteDatabase
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //TODO Declare the return Uri
        Uri returnUri;

        //TODO Match the URI passed in
        switch (sUriMatcher.match(uri)) {

            //TODO If the URI does not contain an ID of a single Task...
            case TASKS:

                //TODO Insert the passed values into the Task table in the database
                db.insert(
                        DatabaseContract.TABLE_TASKS,
                        null,
                        values
                );

                // Get a reference to the ContentUri
                returnUri = DatabaseContract.CONTENT_URI;

                // Break from the switch statement
                break;

            // In the default case...
            default:

                // Throw a UnsupportedOperationException
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        //TODO Get the Context
        Context context = getContext();

        //TODO If the context is not null...
        if (context != null) {

            //TODO Register to watch this Content URI for changes
            context.getContentResolver().notifyChange(uri, null);
        }

        //TODO Return the Uri
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //TODO: Implement existing task update
        //TODO: Expected Uri: content://com.google.developer.taskmaker/tasks/{id}
        switch (sUriMatcher.match(uri)) {

            //TODO If the URI does contain an ID of a single Task...
            case TASKS_WITH_ID:

                //TODO Get the id of said Task

                //TODO Create a selection filter using the _ID column of the Task column

                //TODO Create a selection argument using the id of the Task

                // Break from the switch statement
                break;

                // In the case of default...
            default:

                // Throw a new IllegalArgumentException
                throw new IllegalArgumentException("Illegal delete URI");
        }

        // Get a reference to the writable SQLiteDatabase
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //TODO Update the Task in the database with the passed values and filters,
        // returning the number of rows updated, if any.
        int count = db.update(DatabaseContract.TABLE_TASKS, values, selection, selectionArgs);

        // If there were row(s) updated...
        if (count > 0) {

            // Notify observers of the change
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        //TODO Match the URI passed in
        switch (sUriMatcher.match(uri)) {

            //TODO If the URI does not contain an ID of a single Task...
            case TASKS:

                //TODO Rows aren't counted with null selection

                // Break from the switch statement
                break;

            //TODO If the URI does contain an ID of a single Task...
            case TASKS_WITH_ID:

                //TODO Get the id of said Task

                // Create a selection filter using the _ID column of the Task column
                selection = String.format("%s = ?", DatabaseContract.TaskColumns._ID);

                //TODO Create a selection argument using the id of the Task

                // Break from the switch statement
                break;

                // In the case of default...
            default:

                // Throw a IllegalArgumentException
                throw new IllegalArgumentException("Illegal delete URI");
        }

        // Get a reference to the writable SQLiteDatabase
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //TODO Delete the Task in the database using the passed filters,
        // returning the number of rows deleted, if any.

        //TODO If there were row(s) deleted...
        if (count > 0) {

            // Notify observers of the change
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return count;
    }

    /* Initiate a periodic job to clear out completed items */
    private void manageCleanupJob() {
        Log.d(TAG, "Scheduling cleanup job");
        JobScheduler jobScheduler = (JobScheduler) getContext()
                .getSystemService(Context.JOB_SCHEDULER_SERVICE);

        //TODO Run the job approximately every hour
        // Set the jobInterval variable to be 1 hour
        long jobInterval = 3600000L;

        if (jobScheduler.schedule(task) != JobScheduler.RESULT_SUCCESS) {
            Log.w(TAG, "Unable to schedule cleanup job");
        }
    }
}
