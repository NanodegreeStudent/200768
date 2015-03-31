package com.example.mikhail.santafe.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mikhail.santafe.data.SantafeContract.CategoryEntry;
import com.example.mikhail.santafe.data.SantafeContract.DishEntry;

/**
 * Manages a local database for dish data.
 */

public class SantafeDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;

    public static final String DATABASE_NAME = "santafe.db";

    public SantafeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold categories.

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                CategoryEntry._ID + " INTEGER PRIMARY KEY," +
                CategoryEntry.COLUMN_CAT_TITLE  + " TEXT NOT NULL"+" );";


        final String SQL_CREATE_DISH_TABLE = "CREATE TABLE " + DishEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                DishEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                // the ID of the location entry associated with this weather data
                DishEntry.COLUMN_CAT_KEY + " INTEGER NOT NULL, " +
                DishEntry.COLUMN_DISH_TITLE + " TEXT NOT NULL, " +
                DishEntry.COLUMN_FULL_DESC + " TEXT NOT NULL, " +
                DishEntry.COLUMN_WEIGHT + " INTEGER NOT NULL, " +

                DishEntry.COLUMN_PRICE + " INTEGER NOT NULL, " +
                DishEntry.COLUMN_CCAL + " INTEGER NOT NULL, " +

                DishEntry.COLUMN_IMAGE_ID + " INTEGER NOT NULL, " +

                // Set up the category column as a foreign key to category table.
                " FOREIGN KEY (" + DishEntry.COLUMN_CAT_KEY + ") REFERENCES " +
                CategoryEntry.TABLE_NAME + " (" + CategoryEntry._ID + ") " + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DISH_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DishEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
