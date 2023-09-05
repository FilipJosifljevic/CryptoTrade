package com.example.cryptotrade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "trendingnotif")
                .setSmallIcon(R.drawable.blockchain)
                .setContentTitle("Check out trending coins!")
                .setContentText("Check out what coins have been trending for the past 24h!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        try {
            notificationManagerCompat.notify(200,builder.build());
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }
}
