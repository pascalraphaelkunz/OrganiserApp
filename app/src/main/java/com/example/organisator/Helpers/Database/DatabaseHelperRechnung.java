package com.example.organisator.Helpers.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.organisator.Helpers.Rechnung.RechnungsDetails;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperRechnung extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RechnungDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Rechnungen";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DUE_DATE = "due_date";

    private static final String KEY_AMOUNT = "amount";

    private static final String KEY_STATUS = "bezahlt";

    // Create table statement
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME + " TEXT, " +
                    KEY_AMOUNT + " REAL, " +  // REAL is used for floating-point numbers
                    KEY_DUE_DATE + " DATE, " +
                    KEY_STATUS + " INTEGER DEFAULT 0)";

    public DatabaseHelperRechnung(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void markAsBezahlt(RechnungsDetails details){
        String updateQuery = String.format("UPDATE %s SET %s where name = %s AND amount = %s AND due_date = %s", TABLE_NAME, KEY_STATUS, details.name, details.amount, details.date.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updateQuery);
    }

    // Insert a new Rechnung into the database
    public void insertRechnung(RechnungsDetails rechnung) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, rechnung.name);
        values.put(KEY_DUE_DATE, String.valueOf(rechnung.date));
        values.put(KEY_AMOUNT, String.valueOf(rechnung.amount));

        // Insert the row
        db.insert(TABLE_NAME, null, values);
    }


    public List<RechnungsDetails> getAllRechnungsDetails() {
        List<RechnungsDetails> rechnungsDetailsList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE bezahlt = 0";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add RechnungsDetails to the list
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                @SuppressLint("Range") float amount = cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT));
                @SuppressLint("Range") String dueDateStr = cursor.getString(cursor.getColumnIndex(KEY_DUE_DATE));
                RechnungsDetails rechnungsDetails = new RechnungsDetails(name, Date.valueOf(dueDateStr), amount);
                rechnungsDetailsList.add(rechnungsDetails);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return rechnungsDetailsList;
    }

    public void logAllRechnungsDetails(List<RechnungsDetails> rechnungsDetails){
        for (RechnungsDetails x : rechnungsDetails){
            Log.d("Entry", x.toString());
        }
    }
}
