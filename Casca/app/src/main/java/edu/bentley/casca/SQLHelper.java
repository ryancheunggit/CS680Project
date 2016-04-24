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

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //called to create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = CREATE_TABLE;
        Log.d("SQLiteDemo", "onCreate: " + sql);
        db.execSQL(sql);
    }

    //called when database version mismatch
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /* get all the events from database return a array list of events
    public ArrayList<event> getEvents () {
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.query(TABLE_NAME,
            null, null, null, null, null, KEY_STARTTIME);

        Log.d("Debug", "lalala" + cursor.getCount());

        ArrayList<event> eventList = new ArrayList<event>();

        while (cursor.moveToNext()){
            // Log.d("Debug", "lalala" + " in the loop?");


            int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String eventTitle = cursor.getString(cursor.getColumnIndex(KEY_EVENT));
            // Log.d("Debug", "lalala" + eventTitle);

            String location = cursor.getString(cursor.getColumnIndex(KEY_LOC));
            String startTime = cursor.getString(cursor.getColumnIndex(KEY_STARTTIME));
            String endTime = cursor.getString(cursor.getColumnIndex(KEY_ENDTIME));
            String dateT = cursor.getString(cursor.getColumnIndex(KEY_DATE));
            String description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
            Log.d("Get any query results? ", "1" + eventTitle + location);
            eventList.add(new event(id, eventTitle, location, startTime, endTime, dateT, description));
        }
        db.close();

        return eventList;

    }
    */

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

    public ArrayList<event> queryEvent(String day, String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();

        // get a cursor to find events of a specific date
        cursor = db.rawQuery("SELECT * FROM events WHERE dateT =?", new String[]{day + "-" + month + "-" + year});

        // Log.d("DebugQueryCursor", "" + cursor.getCount()); //debug output

        ArrayList<event> returnList = new ArrayList<event>();
        while (cursor.moveToNext()) {
            returnList.add(new event(
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

    public event queryEvent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // get a cursor to find events of a specific date
        cursor = db.rawQuery("SELECT * FROM events WHERE id =?", new String[]{id});
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
}
