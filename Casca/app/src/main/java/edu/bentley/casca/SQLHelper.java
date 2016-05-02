package edu.bentley.casca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Ryan on 4/17/2016.
 */
public class SQLHelper extends SQLiteOpenHelper {

    // Constant values
    public static final String DATABASE_NAME = "events.db";
    public static final String TABLE_NAME = "events";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_ID = "id";
    public static final String KEY_EVENT = "eventTitle";
    public static final String KEY_LOC = "location";
    public static final String KEY_STARTTIME = "startTime";
    public static final String KEY_ENDTIME = "endTime";
    public static final String KEY_DATE = "dateT";
    public static final String KEY_DESCRIPTION = "description";
    public static final String CREATE_TABLE = "CREATE TABLE events ("
            + KEY_ID + " integer primary key autoincrement,"
            + KEY_EVENT + " text,"
            + KEY_LOC + " text,"
            + KEY_STARTTIME + " text,"
            + KEY_ENDTIME + " text,"
            + KEY_DATE + " text,"
            + KEY_DESCRIPTION + " text);";

    private Cursor cursor;
    private ContentValues values;

    // constructor
    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // called to create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = CREATE_TABLE;
        Log.d("Create Table", "Creating table using: " + sql);
        db.execSQL(sql);
    }

    //called when database version mismatch
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // helper method that will insert a event into database
    public void addEvent(event newEvent) {
        SQLiteDatabase db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(KEY_EVENT, newEvent.getEventTitle());
        values.put(KEY_LOC, newEvent.getLocation());
        values.put(KEY_STARTTIME, newEvent.getStartTime());
        values.put(KEY_ENDTIME, newEvent.getEndTime());
        values.put(KEY_DATE, newEvent.getDateT());
        values.put(KEY_DESCRIPTION, newEvent.getDescription());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // helper method, query events for a given day
    public ArrayList<event> queryEvent(String day, String month, String year) {

        // get database
        SQLiteDatabase db = this.getWritableDatabase();

        // get a cursor, using the query
        cursor = db.rawQuery("SELECT * FROM events WHERE dateT =?", new String[]{day + "-" + month + "-" + year});
        // Log.d("DebugQueryCursor", "" + cursor.getCount()); //debug output

        // populate arraylist with events
        ArrayList<event> returnList = new ArrayList<event>();
        while (cursor.moveToNext()) {
            returnList.add(new event( // convert string valued query results into event objects
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6)
            ));
        }
        return returnList;
    }

    // helper method, query events using id (id is the primary key for event)
    public event queryEvent(String id) {
        // get the database
        SQLiteDatabase db = this.getWritableDatabase();
        // get a cursor to find events of a specific date
        cursor = db.rawQuery("SELECT * FROM events WHERE id =?", new String[]{id});

        // open up the cursor
        cursor.moveToFirst();

        // construct an event object from query result and return it.
        return new event(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6)
            );
    }

    // helper method, delete an event based on id
    public boolean deleteEvent(String id) {
        // get the database
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_NAME, KEY_ID + "=" + id, null) > 0;
    }

    // helper method, get the id of the most recent added event
    public int getId(){
        // get the database
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT MAX(id) FROM events", new String[]{});
        cursor.moveToFirst();
        return Integer.parseInt(cursor.getString(0));
    }
}
