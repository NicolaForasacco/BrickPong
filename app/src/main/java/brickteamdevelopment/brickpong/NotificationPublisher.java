package brickteamdevelopment.brickpong;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;


public class NotificationPublisher extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "\nTry Push-It! https://play.google.com/store/apps/details?id=brickteamdevelopment.pushit");
        shareIntent.setType("text/plain");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntentPlay = PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntentShare = PendingIntent.getActivity(context,0,Intent.createChooser(shareIntent, "Share using "),PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action actionPlay = new NotificationCompat.Action.Builder(R.drawable.playwhite, "PLAY", pendingIntentPlay).build();

        NotificationCompat.Action actionShare = new NotificationCompat.Action.Builder(R.drawable.share_white, "SHARE", pendingIntentShare).build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("BrickPong")
                .setContentText("Play BrickPong or share it with your friends!")
                .addAction(actionPlay)
                .addAction(actionShare)
                .setContentIntent(pendingIntentPlay)
                .setColor(Color.rgb(183, 28, 28))
                .setSmallIcon(R.drawable.icon_brick_pong_white)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.brickpong_icon))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] { 500, 500})
                .setLights(Color.RED, 3000, 3000);

        notificationManager.notify(1,builder.build());
    }
}
