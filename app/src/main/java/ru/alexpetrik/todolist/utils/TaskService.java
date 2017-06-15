package ru.alexpetrik.todolist.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import ru.alexpetrik.todolist.R;

public class TaskService extends Service {

    private NotificationManager nm;
    private static final int NOTIFY_ID = 101;

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotif();
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotif() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("")
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        builder.setContentTitle(getResources().getString(R.string.remind));
        builder.setContentText("sdfhgwehvboweivb");

        Notification notify = builder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;

//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        nm.notify(NOTIFY_ID, notify);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
