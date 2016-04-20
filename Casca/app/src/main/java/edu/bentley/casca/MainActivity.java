package edu.bentley.casca;

import android.content.Context;
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
import android.widget.ListView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // SQL lite database
    private SQLiteDatabase db;
    private SQLHelper helper;

    // declare ArrayAdapter
    // the ArrayAdapter will take data from a source and
    // use it to populate the ListView it's attached to.
    private ArrayAdapter<String> eventListAdapter = null;
    // initialize the toDoList with some dummy data
    private String[] eventListData = {
            "9:00 Breakfast",
            "10:00 Catch Bus",
            "12:00 Lunch",
            "5:00 Class",
            "9:50 Go home"};
    private ListView listView;

    // create an array with default values
    ArrayList<String> eventList = new ArrayList<String>(Arrays.asList(eventListData));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        helper = new SQLHelper(this);

        //create database
        try {
            db = helper.getWritableDatabase();
        } catch(SQLException e) {
            Log.d("SQLiteDemo", "Create database failed");
        }

        // for debug purpose
        ArrayList<event> testList = helper.getEvents();
        for (int i = 0; i < testList.size(); i++){
            Log.d("Debug", "lalala" + testList.get(i).toString());
        }

        eventList.clear();

        for (int i = 0; i < testList.size(); i++){
            eventList.add(testList.get(i).getStartTime() + " " + testList.get(i).getEventTitle());
        }

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent displayDetailIntent = new Intent(MainActivity.this, displayDetail.class);
                startActivity(displayDetailIntent);
            }
        });
        //hide title and icon in action bar
        // ActionBar actionBar = getActionBar();
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setDisplayUseLogoEnabled(false);
    }

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
                Intent addEventIntent = new Intent(this, addEvent.class);
                //startActivity(addEventIntent);
                startActivityForResult(addEventIntent,1);
                // Toast.makeText(this, "You clicked add event", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return true;
    }

}
