package com.gdfp.android_evaluationv2.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;

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
        //TODO Schedule the alarm. Will update an existing item for the same task.
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

    }
}
