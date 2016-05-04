package edu.bentley.casca;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Using a local SQLite database on device
    private SQLiteDatabase db;
    private SQLHelper helper;

    // declare ArrayAdapter
    // the ArrayAdapter will take data from a source and
    // use it to populate the ListView it's attached to.
    private ArrayAdapter<String> eventListAdapter = null;
    // an empty array
    private String[] eventListData = {"0306", // My birthday!!!!
            "0519" // My wedding day!!!!
    };
    // the listView that show events line by line
    private ListView listView;
    // create an array list with default values
    ArrayList<String> eventList = new ArrayList<String>(Arrays.asList(eventListData));
    ArrayList<event> resultList;

    // reference to the calenderview
    private CalendarView calenderview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // create a db helper object
        helper = new SQLHelper(this);

        // create database
        try {
            db = helper.getWritableDatabase();
        } catch(SQLException e) {
            Log.d("SQLiteDemo", "Create database failed");
        }

        // get the current day, month and year
        Calendar c = Calendar.getInstance();
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(c.get(Calendar.MONTH)+1);
        String year = String.valueOf(c.get(Calendar.YEAR));

        // initialize list based on query results, on events of a given day.
        initializeList(year, month, day);
        /*
        // for debug purpose
        ArrayList<event> testList = helper.getEvents();
        eventList.clear();
        for (int i = 0; i < testList.size(); i++){
            eventList.add(testList.get(i).getStartTime() + " " + testList.get(i).getEventTitle());
        }
        */

        // set up array adapter
        eventListAdapter = new ArrayAdapter<String>(
                // the current context
                this,
                // id of the list item layout
                R.layout.list_view_items,
                // id of the TextView
                R.id.list_item_eventList_textview,
                // data
                eventList);

        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(eventListAdapter);

        // define event handler on the item in listView been clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the id for event
                int clickedEventId = resultList.get(position).getId();
                // Log.d("DebugWhatPosition", ""+position); // debug print out
                // Log.d("DebugWhatId", ""+clickedEventId); // debug print out
                // create an intent
                Intent displayDetailIntent = new Intent(MainActivity.this, displayDetail.class);
                // put the id of the event as extra data to the intent
                displayDetailIntent.putExtra("id", "" + clickedEventId);
                // start the displayDetail activity using the intent, the id number will be passed
                startActivity(displayDetailIntent);
            }
        });

        // grab reference to the calenderview
        calenderview = (CalendarView) findViewById(R.id.calendarView);
        // set up listener handler user click on days in the calendar view
        calenderview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                // Log.d("calendarView", "whats the year: " + year + "");   //debug output
                // Log.d("calendarView", "whats the month: " + month + ""); //debug output
                // Log.d("calendarView", "whats the day: " + day + "");     //debug output

                // regenerate the list using only events of that perticular day
                // ==== month start from 0 by default here, should + 1 to get the actual month ====
                initializeList(year + "", (month+1) + "", day + "");
                // update the listView
                eventListAdapter.notifyDataSetChanged();
            }
        });
    }

    // inflate the menu from xml file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // event handlers for option menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_event: {
                // create intent and start addEvent activity
                Intent addEventIntent = new Intent(this, addEvent.class);
                startActivityForResult(addEventIntent,1);
                // Toast.makeText(this, "You clicked add event", Toast.LENGTH_SHORT).show(); // debug purpose
                break;
            }
        }
        return true;
    }

    /**
     * helper method that clear and refill the eventlist with events of a given day.
     * */
    public void initializeList(String year, String month, String day){
        // query the database and get events for that day
        resultList = helper.queryEvent(day, month, year);

        // clear the array list which provides value for list view
        eventList.clear();

        // add events info to the array based on query results
        for (int i = 0; i < resultList.size(); i++){
            eventList.add(resultList.get(i).getStartTime() + " " + resultList.get(i).getEventTitle());
        }

    }

    // define the behavior when switch back to MainActivity from either displayDetail or addEvent
    @Override
    public void onResume(){
        super.onResume();
        // get the current day, month and year
        Calendar c = Calendar.getInstance();
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(c.get(Calendar.MONTH)+1);
        String year = String.valueOf(c.get(Calendar.YEAR));
        // will always show the current day when get back
        initializeList(year, month, day);
        eventListAdapter.notifyDataSetChanged();
    }

}
