package com.gdfp.android_evaluationv2.data;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.gdfp.android_evaluationv2.reminders.ReminderAlarmService;


/* Process DB actions on a background thread */
public class TaskUpdateService extends IntentService {
    private static final String TAG = TaskUpdateService.class.getSimpleName();
    //Intent actions
    public static final String ACTION_INSERT = TAG + ".INSERT";
    public static final String ACTION_UPDATE = TAG + ".UPDATE";
    public static final String ACTION_DELETE = TAG + ".DELETE";

    public static final String EXTRA_VALUES = TAG + ".ContentValues";

    public static void insertNewTask(Context context, ContentValues values) {
        Intent intent = new Intent(context, TaskUpdateService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void updateTask(Context context, Uri uri, ContentValues values) {
        Intent intent = new Intent(context, TaskUpdateService.class);
        intent.setAction(ACTION_UPDATE);
        intent.setData(uri);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void deleteTask(Context context, Uri uri) {
        Intent intent = new Intent(context, TaskUpdateService.class);
        intent.setAction(ACTION_DELETE);
        intent.setData(uri);
        context.startService(intent);
    }

    public TaskUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (ACTION_DELETE.equals(action)) {
            performDelete(intent.getData());
        } else if (ACTION_UPDATE.equals(action)) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performUpdate(intent.getData(), values);
        } else if (ACTION_INSERT.equals(action)) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performInsert(values);
        }
    }

    private void performInsert(ContentValues values) {
        if (getContentResolver().insert(DatabaseContract.CONTENT_URI, values) != null) {
            Log.d(TAG, "Inserted new task");
        } else {
            Log.w(TAG, "Error inserting new task");
        }
    }

    private void performUpdate(Uri uri, ContentValues values) {
        int count = getContentResolver().update(uri, values, null, null);
        Log.d(TAG, "Updated " + count + " task items");
    }

    //send task id on selectionArgs
    private void performDelete(Uri uri) {

        int count = getContentResolver().delete(uri, null, null);
        //Cancel any reminders that might be set for this item
        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(this, uri);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.cancel(operation);
        Log.d(TAG, "Deleted " + count + " tasks");
    }
}
