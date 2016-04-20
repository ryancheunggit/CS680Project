package edu.bentley.casca;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Ryan on 4/17/2016.
 */
public class SQLHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "events.db";
    public static final String TABLE_NAME = "events";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_ID = "id";
    private static final String KEY_EVENT = "eventTitle";
    private static final String KEY_LOC = "location";
    private static String KEY_STARTTIME = "startTime";
    private static final String KEY_ENDTIME = "endTime";
    private static final String KEY_DATE = "dateT";
    private static final String KEY_DESCRIPTION = "description";
    public static final String CREATE_TABLE = "CREATE TABLE events ("
            + KEY_ID + " integer primary key autoincrement,"
            + KEY_EVENT + " text,"
            + KEY_LOC + " text,"
            + KEY_STARTTIME + " text,"
            + KEY_ENDTIME + " text,"
            + KEY_DATE + " text,"
            + KEY_DESCRIPTION + " text);";

    private Cursor cursor;

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

    public ArrayList<event> getEvents () {
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.query(TABLE_NAME,
            null, null, null, null, null, KEY_STARTTIME);

        Log.d("Debug", "lalala" + cursor.getCount());

        ArrayList<event> eventList = new ArrayList<event>();

        while (cursor.moveToNext()){
            Log.d("Debug", "lalala" + " in the loop?");


            int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String eventTitle = cursor.getString(cursor.getColumnIndex(KEY_EVENT));
            Log.d("Debug", "lalala" + eventTitle);

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
}
