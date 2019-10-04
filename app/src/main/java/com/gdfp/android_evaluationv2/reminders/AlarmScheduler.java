package com.gdfp.android_evaluationv2.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Helper to manage scheduling the reminder alarm
 */
public class AlarmScheduler {

    /**
     * Schedule a reminder alarm at the specified time for the given task.
     *
     * @param context Local application or activity context
     * @param alarmTime Alarm start time
     * @param reminderTask Uri referencing the task in the content provider
     */
    public static void scheduleAlarm(Context context, long alarmTime, Uri reminderTask) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);
        PendingIntent operation =  ReminderAlarmService.getReminderPendingIntent(context, reminderTask);

        manager.set(AlarmManager.RTC, alarmTime, operation);
        Log.d("Alarm","Alarm scheduled");
    }
}
