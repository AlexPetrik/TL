package ru.alexpetrik.todolist.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY,
                pIntent);


    }

}
