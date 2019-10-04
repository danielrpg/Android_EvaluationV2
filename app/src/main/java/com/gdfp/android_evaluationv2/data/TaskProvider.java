package com.gdfp.android_evaluationv2.data;

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
import android.util.Log;

import androidx.annotation.Nullable;

import com.gdfp.android_evaluationv2.reminders.AlarmScheduler;

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
        // Create a Cursor object to return
        Cursor returnCursor;

        // Get a reference to the readable SQLiteDatabase
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case TASKS:
                returnCursor = db.query(
                        DatabaseContract.TABLE_TASKS,
                        null,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TASKS_WITH_ID:

                selection = String.format("%s = ?", DatabaseContract.TaskColumns._ID);

                selectionArgs = new String[1];
                String id = uri.getLastPathSegment();
                selectionArgs[0] = id;
                returnCursor = db.query(
                        DatabaseContract.TABLE_TASKS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }
        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return returnCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                db.insert(
                        DatabaseContract.TABLE_TASKS,
                        null,
                        values
                );
                AlarmScheduler.scheduleAlarm(getContext().getApplicationContext(), values.getAsLong(DatabaseContract.TaskColumns.DUE_DATE), uri);
                // Get a reference to the ContentUri
                returnUri = DatabaseContract.CONTENT_URI;

                // Break from the switch statement
                break;

            // In the default case...
            default:

                // Throw a UnsupportedOperationException
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }
        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }


        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case TASKS_WITH_ID:
                String id = uri.getLastPathSegment();
                selection = String.format("%s = ?", DatabaseContract.TaskColumns._ID);
                selectionArgs = new String[1];
                selectionArgs[0] = id;

                // Break from the switch statement
                break;

                // In the case of default...
            default:

                // Throw a new IllegalArgumentException
                throw new IllegalArgumentException("Illegal delete URI");
        }

        // Get a reference to the writable SQLiteDatabase
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
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
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                selectionArgs = new String[1];
                selectionArgs[0] = null;

                // Break from the switch statement
                break;
            case TASKS_WITH_ID:
                String id = uri.getLastPathSegment();

                // Create a selection filter using the _ID column of the Task column
                selection = String.format("%s = ?", DatabaseContract.TaskColumns._ID);
                selectionArgs = new String[1];
                selectionArgs[0] = id;

                // Break from the switch statement
                break;

                // In the case of default...
            default:

                // Throw a IllegalArgumentException
                throw new IllegalArgumentException("Illegal delete URI");
        }

        // Get a reference to the writable SQLiteDatabase
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = db.delete(DatabaseContract.TABLE_TASKS, selection, selectionArgs);
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
        long jobInterval = 3600000L;
        ComponentName job = new ComponentName(getContext(),CleanupJobService.class);
        JobInfo task = new JobInfo.Builder(CLEANUP_JOB_ID,job)
                .setPersisted(true)
                .setPeriodic(jobInterval)
                .build();

        if (jobScheduler.schedule(task) != JobScheduler.RESULT_SUCCESS) {
            Log.w(TAG, "Unable to schedule cleanup job");
        }
    }
}
