package com.example.course_project.event.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.course_project.Main;
import com.example.course_project.MainActivity;
import com.example.course_project.R;

public class NotificationBox {
    public static void showNotification(String title, String text) {
        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) Main.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "prepared";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(Main.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.order_notified)
                .setContentTitle(title)
                .setContentText(text);

        Intent resultIntent = new Intent(Main.getContext(), MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(Main.getContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
