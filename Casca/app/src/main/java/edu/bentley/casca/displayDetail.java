package edu.bentley.casca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class displayDetail extends AppCompatActivity {

    private SQLHelper helper;
    private TextView eventTitle;
    private TextView eventLocation;
    private TextView eventStartTime;
    private TextView eventEndTime;
    private TextView eventDate;
    private TextView eventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail);

        helper = new SQLHelper(this);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        // Log.d("DebugDetailId0", id);
        event ent = helper.queryEvent(id);

        // Log.d("DebugDetailId", ent.getId()+"");
        // Log.d("DebugDetailId", ent.getStartTime()+"");

        eventTitle = (TextView) findViewById(R.id.eventTitleDetail);
        eventLocation = (TextView) findViewById(R.id.eventLocationDetail);
        eventStartTime = (TextView) findViewById(R.id.eventStartTimeDetail);
        eventEndTime = (TextView) findViewById(R.id.eventEndTimeDetail);
        eventDate = (TextView) findViewById(R.id.eventDateDetail);
        eventDescription = (TextView) findViewById(R.id.eventDescriptionDetail);

        eventTitle.setText(ent.getEventTitle());
        eventLocation.setText(ent.getLocation());
        eventStartTime.setText(ent.getStartTime());
        eventEndTime.setText(ent.getEndTime());
        eventDate.setText(ent.getDateT());
        eventDescription.setText(ent.getDescription());
    }
}
