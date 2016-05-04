package edu.bentley.casca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import java.text.DateFormatSymbols;
import android.location.Geocoder;
import java.util.Locale;
import java.util.List;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.FragmentManager;
import android.location.*;

public class displayDetail extends AppCompatActivity implements OnClickListener, OnInitListener {

    private SQLHelper helper;
    private TextView eventTitle;
    private TextView eventLocation;
    private TextView eventStartTime;
    private TextView eventEndTime;
    private TextView eventDate;
    private TextView eventDescription;
    private TextToSpeech speaker;
    private GoogleMap myMap;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail);

        // get sqlite database helper
        helper = new SQLHelper(this);

        // the extra info in intent will be the id of event
        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        // Log.d("DebugDetailId0", id);   // debug output

        // get the event based on id key value from the database
        event ent = helper.queryEvent(id);
        // Log.d("DebugDetailId", ent.getId()+"");  // debug output
        // Log.d("DebugDetailId", ent.getStartTime()+"");  // debug output

        // grab references
        eventTitle = (TextView) findViewById(R.id.eventTitleDetail);
        eventLocation = (TextView) findViewById(R.id.eventLocationDetail);
        eventStartTime = (TextView) findViewById(R.id.eventStartTimeDetail);
        eventEndTime = (TextView) findViewById(R.id.eventEndTimeDetail);
        eventDate = (TextView) findViewById(R.id.eventDateDetail);
        eventDescription = (TextView) findViewById(R.id.eventDescriptionDetail);

        // display information about events
        eventTitle.setText(ent.getEventTitle());
        eventLocation.setText(ent.getLocation());
        eventStartTime.setText(appendZeroToMinutes(ent.getStartTime()));
        eventEndTime.setText(appendZeroToMinutes(ent.getEndTime()));
        eventDate.setText(ent.getDateT());
        eventDescription.setText(ent.getDescription());

        //add listener on eventTitle and eventDescription to speak out content
        eventTitle.setOnClickListener(this);
        eventDescription.setOnClickListener(this);
        speaker = new TextToSpeech(this, this);

        // set up map
        FragmentManager myFragmentManager = getFragmentManager();
        MapFragment myMapFragment =
                (MapFragment)myFragmentManager.findFragmentById(R.id.map);
        myMap = myMapFragment.getMap();
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setAllGesturesEnabled(true);

        // get the position of the event location
        LatLng newLatLng= getLocationFromAddress(ent.getLocation());

        // Log.d("LatLng", newLatLng.toString()); //debug output
        // use a marker on map
        MarkerOptions markerOptions = new MarkerOptions()
                .position(newLatLng)
                .title(newLatLng.toString());
        myMap.addMarker(markerOptions);

        // move the camera to the initial position using an animation
        flyto(newLatLng, 17, 3000);
    }

    // listeners for textView widgets, all used to get user inputs
    @Override
    public void onClick(View view) {
        // defines event handling for text view
        switch (view.getId()) {
            case R.id.eventTitleDetail:
                // read out title and start time /date of the event
                String returnDate = translateDate(eventDate.getText().toString());
                String returnTime = translateTime(eventStartTime.getText().toString());
                String speakEvent = eventTitle.getText().toString() +
                        " The start time is " + returnTime + "Start Date is " + returnDate;
                speak(speakEvent);
                break;
            case R.id.eventDescriptionDetail:
                // read out description of the event
                String speakEventDes = eventDescription.getText().toString();
                speak(speakEventDes);
                break;
        }
    }

    // inflate the menu from xml file
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
                String message = prepareMessage();
                // Log.d("DebugMessage:", message); //debug printout
                // create intent obj
                Intent smsShareIntent = new Intent(Intent.ACTION_VIEW);
                // put event information to the intent
                smsShareIntent.setData(Uri.parse("sms:"));
                smsShareIntent.putExtra("sms_body", message);
                // start the activity
                startActivity(smsShareIntent);
                break;
            }
            case R.id.menu_delete_event: {
                // delete the event from database and go back to Main activity
                boolean deleted = helper.deleteEvent(id);
                if (deleted) {
                    Intent goBacktoMain = new Intent(this, MainActivity.class);
                    startActivity(goBacktoMain);
                }
                break;
            }
            case R.id.menu_mail: {
                // share the message using email
                String message = prepareMessage();
                Intent mailShareIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                mailShareIntent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(mailShareIntent);
                break;
            }
        }
        return true;
    }

    // tts function
    public void speak(String output) {
        speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
    }

    // initialize the tts
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = speaker.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Language is not available.");
            } else {
                Log.i("TextToSpeech", "TTS Initialization successful.");
            }
        } else {
            Log.e("TextToSpeech", "Could not initialize TextToSpeech.");
        }
    }

    //change numerical event date to standard date format: 5-1-2015: May first 2015"
    public String translateDate(String date) {
        DateFormatSymbols symbols = new DateFormatSymbols();
        String Year = date.substring(date.lastIndexOf("-") + 1);
        String Month = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
        String Day = date.substring(0, date.indexOf("-"));
        Month = symbols.getMonths()[Integer.parseInt(Month) - 1];
        String daySuffix = getDayOfMonthSuffix(Integer.parseInt(Day));
        String ordinalToday = Day + daySuffix;
        String returnDate = " "+ Month +" "+ ordinalToday +" " + Year;
        return returnDate;
    }

    //get correct suffix of dayofmonth === neat work yuge!
    public String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    //change time to standard time format for textToSpeach to function well
    public String translateTime(String time) {
        String Hour = time.substring(0, time.indexOf(":"));
        String Minute = time.substring(time.indexOf(":") + 1);
        int h = Integer.parseInt(Hour);
        int m = Integer.parseInt(Minute);
        String returnTime = "";
        if (h > 12) {
            returnTime = (h - 12) + " " + Minute + " PM ";
        } else if (h <= 12) {
            returnTime = h + " " + Minute + " AM ";
        }
        return returnTime;
    }

    // method to get location based on text address
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    /**
     * helper method, create cameraPosition based on given target and zoom level
     * */
    protected CameraPosition createCameraPosition(LatLng latlng, float zoomLevel){
        CameraPosition position = CameraPosition.builder()
                .target(latlng)
                .zoom(zoomLevel)
                .build();
        return position;
    }

    /**
     * helper method, change camera Position based on given target and zoom level
     * */
    protected void flyto(LatLng pos, float zoom, int time){
        myMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(createCameraPosition(pos, zoom)), // camera target position
                time, // animation time in m-seconds
                null   // call back function
        );
    }

    // helper method prepare the message body for sms and email
    public String prepareMessage(){
        // generate message content
        String message = "Event: " + eventTitle.getText().toString() + "\n" +
                "Location: " + eventLocation.getText().toString() + "\n" +
                "Start Time: " + eventStartTime.getText().toString() + "\n" +
                "End Time: " + eventEndTime.getText().toString() + "\n" +
                "Date: " + eventDate.getText().toString() + "\n" +
                "Description: " + eventDescription.getText().toString();
        return message;
    }

    // remember to shunt down tts
    @Override
    protected void onStop() {
        super.onStop();
        speaker.stop();
        speaker.shutdown();
        Log.d("DebugTTS", "shutdown TTS");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speaker.stop();
        speaker.shutdown();
        Log.d("DebugTTS", "shutdown TTS");
    }

    // helper method to add 0 to minutes if the value is 0
    public String appendZeroToMinutes(String time){
        if (time.substring(time.indexOf(":")+1).equals("0")){
            time += "0";
        }
        return time;
    }
}
