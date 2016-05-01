package edu.bentley.casca;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_detail_menu, menu);
        return true;
    }

    // event handlers for option menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share: {
                // generate message content
                String message = "Event: " + eventTitle.getText().toString() + "\n" +
                        "Location: " + eventLocation.getText().toString() + "\n" +
                        "Start Time: " + eventStartTime.getText().toString() + "\n" +
                        "End Time: " + eventEndTime.getText().toString() + "\n" +
                        "Date: " + eventDate.getText().toString() + "\n" +
                        "Description: " + eventDescription.getText().toString();

                Log.d("DebugMessage:", message);
                // create intent obj
                Intent smsShareIntent = new Intent(Intent.ACTION_VIEW);
                // put event information to the intent
                smsShareIntent.setData(Uri.parse("sms:"));
                smsShareIntent.putExtra("sms_body", message);
                // start the activity
                startActivity(smsShareIntent);
                break;
            }
        }
        return true;
    }

}
