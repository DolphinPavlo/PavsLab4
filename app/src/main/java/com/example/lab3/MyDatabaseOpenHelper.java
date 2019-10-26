package com.example.lab3;
import android.app.Activity;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDatabaseFile";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "Message";
    public static final String COL_ID = "_id";
    public static final String COL_SEND = "isSend";
    public static final String COL_MESSAGE = "message";

    public MyDatabaseOpenHelper(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("onCreate", "");
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_SEND + " INTEGER , " + COL_MESSAGE + " TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void printCursor(Cursor c) {
        Log.d("printCursor", "Database Version Number: " + VERSION_NUM);
        Log.d("printCursor", "Number of columns in the cursor: " + c.getColumnCount());
        Log.d("printCursor", "Name of the columns in the cursor: " + Arrays.toString(c.getColumnNames()));
        Log.d("printCursor", "Number of results in the cursor: " + c.getCount());
        Log.d("printCursor", "Each row of results in the cursor: " + DatabaseUtils.dumpCursorToString(c));
    }

}