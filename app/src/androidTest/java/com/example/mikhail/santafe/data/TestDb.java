package com.example.mikhail.santafe.data;



import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.mikhail.santafe.data.SantafeContract.CategoryEntry;
import com.example.mikhail.santafe.data.SantafeContract.DishEntry;

import java.util.Map;
import java.util.Set;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(SantafeDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new SantafeDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }



    public void testInsertReadDb() {

        // Test data we're going to insert into the DB to see if it works.
        String testCategoryTitle = "Салаты";


        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        SantafeDbHelper dbHelper = new SantafeDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = createCategoryValues();

        long categoryRowId;
        categoryRowId = db.insert(CategoryEntry.TABLE_NAME, null, testValues);
        // Verify we got a row back.
        assertTrue(categoryRowId != -1);
        Log.d(LOG_TAG, "New row id: " + categoryRowId);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Specify which columns you want.
        String[] columns = {
                CategoryEntry._ID,
                CategoryEntry.COLUMN_CAT_TITLE
        };

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                CategoryEntry.TABLE_NAME,  // Table to Query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // If possible, move to the first row of the query results.
        if (cursor.moveToFirst()) {
            // Get the value in each column by finding the appropriate column index.
            int locationIndex = cursor.getColumnIndex(CategoryEntry.COLUMN_CAT_TITLE);
            String title = cursor.getString(locationIndex);

            // Hooray, data was returned!  Assert that it's the right data, and that the database
            // creation code is working as intended.
            // Then take a break.  We both know that wasn't easy.
            assertEquals(testCategoryTitle, title);


            // Fantastic.  Now that we have a location, add some weather!
        } else {
            // That's weird, it works on MY machine...
            fail("No values returned :(");
        }

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues dishValues = createDishValues(categoryRowId);

        long dishRowId = db.insert(DishEntry.TABLE_NAME, null, dishValues);
        assertTrue(dishRowId != -1);
// A cursor is your primary interface to the query results.
        Cursor dishCursor = db.query(
                DishEntry.TABLE_NAME, // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        if (!dishCursor.moveToFirst()) {
            fail("No weather data returned!");
        }
        assertEquals(dishCursor.getInt(
                dishCursor.getColumnIndex(DishEntry.COLUMN_CAT_KEY)), categoryRowId);

        assertEquals(dishCursor.getString(
                dishCursor.getColumnIndex(DishEntry.COLUMN_DISH_TITLE)), "Цезарь");

        assertEquals(dishCursor.getString(
                dishCursor.getColumnIndex(DishEntry.COLUMN_FULL_DESC)), "Вкусный салат");

        assertEquals(dishCursor.getInt(
                dishCursor.getColumnIndex(DishEntry.COLUMN_PRICE)), 240);

        assertEquals(dishCursor.getInt(
                dishCursor.getColumnIndex(DishEntry.COLUMN_CCAL)), 300);

        assertEquals(dishCursor.getInt(
                dishCursor.getColumnIndex(DishEntry.COLUMN_WEIGHT)), 300);

        dishCursor.close();


        dbHelper.close();
    }



    static ContentValues createDishValues(long locationRowId) {
        ContentValues dishValues = new ContentValues();
        dishValues.put(DishEntry.COLUMN_CAT_KEY, locationRowId);
        dishValues.put(DishEntry.COLUMN_DISH_TITLE, "Цезарь");
        dishValues.put(DishEntry.COLUMN_FULL_DESC, "Вкусный салат");
        dishValues.put(DishEntry.COLUMN_PRICE, 240);
        dishValues.put(DishEntry.COLUMN_CCAL, 300);
        dishValues.put(DishEntry.COLUMN_WEIGHT,300);
        return dishValues;
    }

    static ContentValues createCategoryValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(CategoryEntry.COLUMN_CAT_TITLE, "Салаты");
        return testValues;
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }
}