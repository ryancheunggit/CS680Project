package edu.bentley.casca;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.widget.Toast;

public class addEvent extends AppCompatActivity implements OnClickListener {
    private EditText edit_eventTitle;
    private EditText edit_location;
    private EditText edit_startT;
    private EditText edit_endT;
    private EditText edit_date;
    private EditText edit_des;


    private SQLHelper helper;

    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;

    /** Called when the activity is first created. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // grab references to widgets
        edit_eventTitle = (EditText)findViewById(R.id.edit_eventTitle_f);
        edit_location = (EditText)findViewById(R.id.edit_location_f);
        edit_des = (EditText)findViewById(R.id.edit_des_f);
        edit_startT = (EditText) findViewById(R.id.edit_startT_f);
        edit_endT = (EditText)findViewById(R.id.edit_endT_f);
        edit_date = (EditText)findViewById(R.id.edit_date_f);

        // set listeners
        edit_startT.setOnClickListener(this);
        edit_endT.setOnClickListener(this);
        edit_date.setOnClickListener(this);

        helper = new SQLHelper(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.edit_startT_f:
                // Process to get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                edit_startT.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                tpd.show();
                break;
            case R.id.edit_endT_f:
                // Process to get Current Time
                final Calendar c1 = Calendar.getInstance();
                mHour = c1.get(Calendar.HOUR_OF_DAY);
                mMinute = c1.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd1 = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                edit_endT.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                tpd1.show();
                break;
            case R.id.edit_date_f:
                // Process to get Current Date
                final Calendar c3 = Calendar.getInstance();
                mYear = c3.get(Calendar.YEAR);
                mMonth = c3.get(Calendar.MONTH);
                mDay = c3.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                edit_date.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
        }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_event_menu, menu);
        return true;
    }

    // event handlers for option menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear_event:
                edit_eventTitle.setText("");
                edit_des.setText("");
                edit_location.setText("");
                edit_startT.setText("");
                edit_endT.setText("");
                break;
            case R.id.menu_create_event:
                if (edit_eventTitle.getText().toString().equals("") ||
                        edit_location.getText().toString().equals("") ||
                        edit_startT.getText().toString().equals("") ||
                        edit_endT.getText().toString().equals("") ||
                        edit_date.getText().toString().equals("")){
                    // if there are fields that is still empty, show a message, do not do insert
                    Toast.makeText(this, "Invalid Inputs", Toast.LENGTH_SHORT);
                }
                else {
                    // if fields are not empty
                    //save the current info as a new entry to the database
                    helper.addEvent(new event(
                            edit_eventTitle.getText().toString(),
                            edit_location.getText().toString(),
                            edit_startT.getText().toString(),
                            edit_endT.getText().toString(),
                            edit_date.getText().toString(),
                            edit_des.getText().toString()
                    ));
                    // debug print out
                    Log.d("DebugInsert", edit_eventTitle.getText().toString());

                    // ---- create an alarm that is 5 minuts before the event ---- //
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    Calendar calendar = Calendar.getInstance();
                    // Log.d("DebugAdd", edit_date.getText().toString());   //debug output
                    // Log.d("DebugAdd", edit_startT.getText().toString()); //debug output
                    String date = edit_date.getText().toString();
                    String Year = date.substring(date.lastIndexOf("-") + 1);
                    String Month = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
                    String Day = date.substring(0, date.indexOf("-"));
                    String StartTime = edit_startT.getText().toString();
                    String Hour = StartTime.substring(0, StartTime.indexOf(":"));
                    String Minute = StartTime.substring(StartTime.indexOf(":")+1);
                    // Log.d("DebugAdd", "Year?" + Year);     //debug output
                    // Log.d("DebugAdd", "Month?" + Month);   //debug output
                    // Log.d("DebugAdd", "Day?" + Day);       //debug output
                    calendar.set(Calendar.YEAR, Integer.parseInt(Year));
                    calendar.set(Calendar.MONTH, Integer.parseInt(Month));  //Note: might need to -1
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(Day));
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Hour));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(Minute));
                    calendar.set(Calendar.SECOND, 0);

                    Intent i = new Intent("edu.bentley.casca");
                    i.putExtra("CascaEventNotif", 1);

                    PendingIntent displayIntent = PendingIntent.getActivity(
                            getBaseContext(), 0, i, 0
                    );

                    // set alarm to trigger
                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis() - 1000 * 60 * 15,  // notify 15 minutes ahead
                            displayIntent);

                    // go back to MainActivity
                    Intent goBack = new Intent(this, MainActivity.class);
                    startActivity(goBack);
                }
                break;
            }
        return true;
    }




}




