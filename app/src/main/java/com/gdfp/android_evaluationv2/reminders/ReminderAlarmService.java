package com.gdfp.android_evaluationv2.reminders;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.gdfp.android_evaluationv2.R;
import com.gdfp.android_evaluationv2.data.DatabaseContract;

public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        String description = cursor.getString(cursor.getColumnIndex(DatabaseContract.TaskColumns.DESCRIPTION));

        Notification notification = new Notification.Builder(this)
                .setContentTitle(description)
                .setSmallIcon(R.drawable.ic_reminder)
                .build();
        Log.d("Reminder","uri");
        manager.notify(NOTIFICATION_ID,notification);
    }
}
