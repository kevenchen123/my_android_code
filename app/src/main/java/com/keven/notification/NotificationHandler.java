package com.keven.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.keven.api.AndroidApiActivity;
import com.keven.R;

import java.util.Random;

public class NotificationHandler {
    // Notification handler singleton
    private static NotificationHandler nHandler;
    private static NotificationManager mNotificationManager;
    private static String channelID = "notify_001";

    private NotificationHandler() {
    }

    /**
     * Singleton pattern implementation
     *
     * @return
     */
    public static NotificationHandler getInstance(Context context) {
        if (nHandler == null) {
            nHandler = new NotificationHandler();
            mNotificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelID, "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        return nHandler;
    }

    /**
     * Shows a simple notification
     *
     * @param context aplication context
     */
    public void createSimpleNotification(Context context) {
        // Creates an explicit intent for an Activity
       Intent resultIntent = new Intent(context, AndroidApiActivity.class);

        // Creating a artifical activity stack for the notification activity
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AndroidApiActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        // Pending intent to the notification manager
        PendingIntent resultPending = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Building the notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelID)
                .setVibrate(new long[]{100, 250})
                .setLights(Color.YELLOW, 500, 5000)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("I'm a simple notification") // main title of the notification
                .setContentText("I'm the text of the simple notification") // notification text
                .setContentIntent(resultPending); // notification intent

        // mId allows you to update the notification later on.
        mNotificationManager.notify(10, mBuilder.build());
    }

    public void createExpandableNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Building the expandable content
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            String lorem = context.getResources().getString(R.string.action_settings);
            String[] content = lorem.split("\\.");

            inboxStyle.setBigContentTitle("This is a big title");
            for (String line : content) {
                inboxStyle.addLine(line);
            }

            // Building the notification
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, channelID)
                    .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                    .setContentTitle("Expandable notification") // title of notification
                    .setContentText("This is an example of an expandable notification") // text inside the notification
                    .setStyle(inboxStyle); // adds the expandable content to the notification

            mNotificationManager.notify(11, nBuilder.build());
        } else {
            Toast.makeText(context, "Can't show", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Show a determinate and undeterminate progress notification
     *
     * @param context, activity context
     */
    public void createProgressNotification(final Context context) {

        // used to update the progress notification
        final int progresID = new Random().nextInt(1000);

        // building the notification
        final NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.slot_01)
                .setContentTitle("Progres notification")
                .setContentText("Now waiting")
                .setTicker("Progress notification created")
                .setUsesChronometer(true)
                .setProgress(100, 0, true);

        AsyncTask<Integer, Integer, Integer> downloadTask = new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mNotificationManager.notify(progresID, nBuilder.build());
            }

            @Override
            protected Integer doInBackground(Integer... params) {
                try {
                    // Sleeps 2 seconds to show the undeterminated progress
                    Thread.sleep(5000);

                    // update the progress
                    for (int i = 0; i < 101; i += 5) {
                        nBuilder
                                .setContentTitle("Progress running...")
                                .setContentText("Now running...")
                                .setProgress(100, i, false)
                                .setSmallIcon(R.drawable.slot_02)
                                .setContentInfo(i + " %");

                        // use the same id for update instead created another one
                        mNotificationManager.notify(progresID, nBuilder.build());
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);

                nBuilder.setContentText("Progress finished :D")
                        .setContentTitle("Progress finished !!")
                        .setTicker("Progress finished !!!")
                        .setSmallIcon(R.drawable.slot_03)
                        .setUsesChronometer(false);

                mNotificationManager.notify(progresID, nBuilder.build());
            }
        };

        // Executes the progress task
        downloadTask.execute();
    }

    public void createButtonNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Prepare intent which is triggered if the  notification button is pressed
            Intent intent = new Intent(context, AndroidApiActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Building the notifcation
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, channelID)
                    .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                    .setContentTitle("Button notification") // notification title
                    .setContentText("Expand to show the buttons...") // content text
                    .setTicker("Showing button notification") // status bar message
                    .addAction(R.drawable.slot_04, "Accept", pIntent) // accept notification button
                    .addAction(R.drawable.slot_05, "Cancel", pIntent); // cancel notification button

            mNotificationManager.notify(1001, nBuilder.build());
        } else {
            Toast.makeText(context, "You need a higher version", Toast.LENGTH_LONG).show();
        }
    }

    public void createRemoteViewNotification(Context context, String packagename) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID);
        RemoteViews myNotificationView = new RemoteViews(packagename, R.layout.customer_notification_view);
        myNotificationView.setTextViewText(R.id.notification_title, "title善良劫匪");
        myNotificationView.setTextViewText(R.id.tv_content, "深咖啡了看司法还生来就发了所肩负的拉萨附近拉丝粉静安寺浪费精力萨福克就算了肯德基发上来是覅偶按时发货撒发神经啊发顺丰金卡卡萨菲尼克斯的积分卢卡斯解放路看见啊李师傅卡萨减肥法");
        builder.setContent(myNotificationView)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);
        mNotificationManager.notify(1001, builder.build());
    }
}