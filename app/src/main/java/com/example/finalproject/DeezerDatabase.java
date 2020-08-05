package com.example.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database helper used for program
 * @author Austin
 * @version 1.0
 */

public class DeezerDatabase extends SQLiteOpenHelper {

    /** Name of this database */
    protected final static String DATABASE_NAME = "ArtistsDB";
    /** Version number of this database */
    protected final static int VERSION_NUM = 1;
    /** Name of a table for this database */
    public final static String TABLE_NAME = "Songs";
    /** Name of the ID column for this database */
    public final static String COL_ID = "_id";
    /** Name of the SONG column for this database */
    public final static String COL_SONG = "song";
    /** Name of the ALBUM column for this database */
    public final static String COL_ALBUM = "album";
    /** Name of the DURATION column for this database */
    public final static String COL_DURATION = "duration";
    /** Name of the IMAGE column for this database */
    public final static String COL_IMAGE = "image";

    /**
     * The current state of the database being used
     * @param context The current state of the database
     */

    DeezerDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Create a new table in the database for data to be stored if none exists yet
     * @param db The database to be used
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_SONG + " TEXT,"
                + COL_ALBUM + " TEXT,"
                + COL_DURATION + " TEXT,"
                + COL_IMAGE + " BLOB);");
    }

    /**
     * If the version number of the database is lower than VERSION_NUMBER,
     * drop the current table and remake
     * @param db The database to be used
     * @param oldVersion The old version number of the database
     * @param newVersion The new version number of the database
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    /**
     * If the version number of the database is higher than VERSION_NUMBER,
     * drop the current table and remake
     * @param db The database to be used
     * @param oldVersion The old version number of the database
     * @param newVersion The new version number of the database
     */

    //If version number is higher than VERSION_NUMBER
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Print the data in the location of the database of where the cursor is pointing to the log for debugging purposes
     * @param c The location of the cursor
     */

    public void printCursor(Cursor c) {
        SQLiteDatabase db = this.getReadableDatabase();
        for(int i = 0; i < c.getColumnCount(); i++) {
            Log.d("Column " + (i+1) + ": ", c.getColumnName(i));
        }
        Log.d("Cursor Object", DatabaseUtils.dumpCursorToString(c));
    }
}
