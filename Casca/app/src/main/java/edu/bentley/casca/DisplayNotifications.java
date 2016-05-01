package edu.bentley.casca;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DisplayNotifications extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Called?", "is DisplayNotfication ever called?");

        //---get the notification ID for the notification;
        // passed in by the MainActivity---
        int notifID = getIntent().getExtras().getInt("CascaEventNotif");

        //---PendingIntent to launch activity if the user selects
        // the notification---
        Intent i = new Intent("edu.bentley.casca.AlarmDetails");
        i.putExtra("CascaEventNotif", notifID);

        PendingIntent detailsIntent =
                PendingIntent.getActivity(this, 0, i, 0);

        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Notification notifyDetails = new Notification.Builder(this)
                .setContentTitle("Casca Notification")	//set Notification text and icon
                .setContentText("Time to get to work")
                .setSmallIcon(R.drawable.icon)
                .setWhen(System.currentTimeMillis())    //timestamp when event occurs
                        //set Android to vibrate when notified
                .setVibrate(new long[]{100, 100, 200, 300})
                .build();

        nm.notify(800, notifyDetails);
        //---destroy the activity---
        finish();
    }
}