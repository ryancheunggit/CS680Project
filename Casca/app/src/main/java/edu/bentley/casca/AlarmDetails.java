package edu.bentley.casca;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.util.Log;

public class AlarmDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);
        Log.d("Called?", "is AlarmDetails ever called?");

        // get a notification manager
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        nm.cancel(getIntent().getExtras().getInt("CascaEventNotif"));
    }
}
