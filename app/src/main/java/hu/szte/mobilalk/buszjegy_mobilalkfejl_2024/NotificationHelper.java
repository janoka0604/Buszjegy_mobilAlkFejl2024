package hu.szte.mobilalk.buszjegy_mobilalkfejl_2024;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class NotificationHelper {
    private static final String CHANNEL_ID = "ticket_destroy_channel";
    private final int NOTIFICATION_ID_PURCHASE = 200;
    private final NotificationManager mNotifyManager;
    private final Context mContext;


    public NotificationHelper(Context context) {
        this.mContext = context;
        this.mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID, "Buszjeg értesítés", NotificationManager.IMPORTANCE_HIGH);

        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setDescription("Még több jegy vár rád!");

        mNotifyManager.createNotificationChannel(channel);
    }

    public void purchase() {
        Intent intent = new Intent(mContext, TicketActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, NOTIFICATION_ID_PURCHASE, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Buszjegy")
                .setContentText("Köszönjük a vásárlást!")
                .setColor(Color.CYAN)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000})
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        mNotifyManager.notify(NOTIFICATION_ID_PURCHASE, builder.build());
    }

}
