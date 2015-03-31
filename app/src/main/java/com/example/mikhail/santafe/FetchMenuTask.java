package com.example.mikhail.santafe;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.mikhail.santafe.data.SantafeContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class FetchMenuTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMenuTask.class.getSimpleName();
    private ArrayAdapter<String> AdapterForMenu;
    private final Context mContext;

    public FetchMenuTask(Context context) {
        mContext = context;
    }

    private boolean DEBUG = true;


    String[] getDish(long id) {
        List<String > category= new ArrayList();
        Cursor categoryCursor = mContext.getContentResolver().query(
                SantafeContract.DishEntry.CONTENT_URI,
                new String[]{SantafeContract.DishEntry.COLUMN_DISH_TITLE},
                SantafeContract.CategoryEntry._ID + " = ?",
                new String[]{Long.toString(id)},
                null);

        if (categoryCursor.moveToFirst()) {
            while (categoryCursor.moveToLast()) {
                int categoryIndex = categoryCursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_DISH_TITLE);
                category.add(categoryCursor.getString(categoryIndex));
            }
        }
        String[] array = new String[category.size()];
        category.toArray(array);

        return array;
    }

    /**
     * Method help us to get dishes by category
     *
     * @param id  id of category
     * @return the row string of the category.
     */

    String getCategory(long id) {
        String category="";
        Cursor categoryCursor = mContext.getContentResolver().query(
                SantafeContract.CategoryEntry.CONTENT_URI,
                new String[]{SantafeContract.CategoryEntry.COLUMN_CAT_TITLE},
                SantafeContract.CategoryEntry._ID + " = ?",
                new String[]{Long.toString(id)},

                null);

        if (categoryCursor.moveToFirst()) {
            int categoryIndex = categoryCursor.getColumnIndex(SantafeContract.CategoryEntry.COLUMN_CAT_TITLE);
            category = categoryCursor.getString(categoryIndex);
        }
        return category;
    }

    long addCategory(String categorySetting) {
        long categoryId;

        // First, check if the location with this city name exists in the db
        Cursor categoryCursor = mContext.getContentResolver().query(
                SantafeContract.CategoryEntry.CONTENT_URI,
                new String[]{SantafeContract.CategoryEntry._ID},
                SantafeContract.CategoryEntry.COLUMN_CAT_TITLE + " = ?",
                new String[]{categorySetting},
                null);

        if (categoryCursor.moveToFirst()) {
            int locationIdIndex = categoryCursor.getColumnIndex(SantafeContract.CategoryEntry._ID);
            categoryId = categoryCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues categoryValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            categoryValues.put(SantafeContract.CategoryEntry.COLUMN_CAT_TITLE, categorySetting);


            // Finally, insert category data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    SantafeContract.CategoryEntry.CONTENT_URI,
                    categoryValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            categoryId = ContentUris.parseId(insertedUri);
        }

        categoryCursor.close();
        // Wait, that worked?  Yes!
        return categoryId;
    }

    private void getDataFromJson(String forecastJsonStr)
            throws JSONException {

        final String TITLE = "DishTitle";
        final String FULL_DESCRIPTION = "Description";
        final String PRICE = "Price";
        final String WEIGHT = "Weight";
        final String CCAL = "Ccal";
        final String IMAGE_ID = "ImageId";

        final String CATEGORY_NAME = "CategoryName";

        // Delete all "\"
        String s = forecastJsonStr.replaceAll("\\\\", "");

        // Delete first and last "", beacuse before it was "JSON" after JSON
        String clearJson = s.substring(1, s.length() - 1);

        try {
            JSONArray menuItems = new JSONArray(clearJson);

            //String[] resultStrs = new String[]{};

            Vector<ContentValues> cVVector = new Vector<ContentValues>(menuItems.length());

            for (int i = 0; i < menuItems.length(); i++) {

                String title;
                String fullDescription;
                int price;
                int weight;
                int ccal;
                String categoryName;
                int imageId;

                // Get the JSON object representing the dish
                JSONObject dishObject = menuItems.getJSONObject(i);

                title = dishObject.getString(TITLE);
                fullDescription = dishObject.getString(FULL_DESCRIPTION);
                price = dishObject.getInt(PRICE);
                weight = dishObject.getInt(WEIGHT);
                ccal = dishObject.getInt(CCAL);

                imageId= dishObject.getInt(IMAGE_ID);

                categoryName = dishObject.getString(CATEGORY_NAME);

                // Adding values about category
                long categoryId = addCategory(categoryName);


                // Adding values about dish

                ContentValues dishValues = new ContentValues();

                dishValues.put(SantafeContract.DishEntry.COLUMN_CAT_KEY, categoryId);
                dishValues.put(SantafeContract.DishEntry.COLUMN_DISH_TITLE, title);
                dishValues.put(SantafeContract.DishEntry.COLUMN_FULL_DESC, fullDescription);
                dishValues.put(SantafeContract.DishEntry.COLUMN_WEIGHT, weight);
                dishValues.put(SantafeContract.DishEntry.COLUMN_PRICE, price);
                dishValues.put(SantafeContract.DishEntry.COLUMN_CCAL, ccal);
                dishValues.put(SantafeContract.DishEntry.COLUMN_IMAGE_ID,imageId);

                cVVector.add(dishValues);
            }

            int inserted = 0;

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(SantafeContract.DishEntry.CONTENT_URI, cvArray);
            }

//            // Sort order:  Ascending, by date.
//            String sortOrder = WeatherEntry.COLUMN_DATE + " ASC";
//            Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithStartDate(
//                    locationSetting, System.currentTimeMillis());
//
//            Cursor cur = mContext.getContentResolver().query(weatherForLocationUri,
//                    null, null, null, sortOrder);
//
//            cVVector = new Vector<ContentValues>(cur.getCount());
//            if ( cur.moveToFirst() ) {
//                do {
//                    ContentValues cv = new ContentValues();
//                    DatabaseUtils.cursorRowToContentValues(cur, cv);
//                    cVVector.add(cv);
//                } while (cur.moveToNext());
//            }

            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");

            // Нужно было когда мы возвращали массив строк в меню, теперь мы просто записываем в бд
            // А не каждый раз как было до этого
           // String[] resultStrs = convertContentValuesToUXFormat(cVVector);
           // return resultStrs;

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


    String[] convertContentValuesToUXFormat(Vector<ContentValues> cvv) {
        // return strings to keep UI functional for now
        Integer[] resultStrs = new Integer[cvv.size()];
        for (int i = 0; i < cvv.size(); i++) {
            ContentValues weatherValues = cvv.elementAt(i);

            resultStrs[i] = Integer.parseInt(weatherValues.getAsString(SantafeContract.DishEntry.COLUMN_CAT_KEY));
        }

        Integer[] res =removeDuplicates(resultStrs);

        String [] categories = new String[res.length];
        for (int i=0;i<res.length;i++)
        {
            categories[i]=getCategory (Long.valueOf(res[i]).longValue());
        }
        return categories;
    }

    public static Integer[] removeDuplicates(Integer[] resultStrs) {
        return new HashSet<Integer>(Arrays.asList(resultStrs)).toArray(new Integer[0]);
    }

    @Override
    protected Void doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            URL url = new URL("http://cafeapi.azurewebsites.net/api/dishes");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line);
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Menu Json String: " + forecastJsonStr);

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        try {
             getDataFromJson(forecastJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

  //  @Override
   // protected void onPostExecute(String[] result) {
   //     if (result != null && AdapterForMenu != null) {
   //         AdapterForMenu.clear();
   //         for(String dayForecastStr : result) {
   //             AdapterForMenu.add(dayForecastStr);
   //         }
   //         // New data is back from the server.  Hooray!
    //    }
    //}
}