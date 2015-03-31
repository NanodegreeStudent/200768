package com.example.mikhail.santafe;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mikhail.santafe.data.SantafeContract;

public  class MenuFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FORECAST_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
// In this case the id needs to be fully qualified with a table name, since
// the content provider joins the location & weather tables in the background
// (both have an _id column)
// On the one hand, that's annoying. On the other, you can search the weather table
// using the location set by the user, which is only in the Location table.
// So the convenience is worth it.
            SantafeContract.DishEntry.TABLE_NAME + "." + SantafeContract.DishEntry._ID,
            SantafeContract.DishEntry.COLUMN_DISH_TITLE,
            SantafeContract.DishEntry.COLUMN_FULL_DESC,
            SantafeContract.DishEntry.COLUMN_CCAL,
            SantafeContract.DishEntry.COLUMN_PRICE,
            SantafeContract.DishEntry.COLUMN_WEIGHT,
            SantafeContract.DishEntry.COLUMN_CAT_KEY


    };
    // These indices are tied to FORECAST_COLUMNS. If FORECAST_COLUMNS changes, these
// must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_DISH_TITLE = 1;
    static final int COL_FULL_DESC = 2;
    static final int COL_CCAL = 3;
    static final int COL_PRICE = 4;
    static final int COL_WEIGHT = 5;
    static final int COL_CAT_KEY = 6;


    private MenuAdapter mMenuAdapter;

    public MenuFragment() {}

     final String SALAD = "Салаты";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setHasOptionsMenu(true);
        }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menufragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // Cursor cur = getActivity().getContentResolver().query(SantafeContract.CategoryEntry.CONTENT_URI,  null, null, null, null);

//        // The CursorAdapter will take data from our cursor and populate the ListView.
        mMenuAdapter = new MenuAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_my, container, false);

       // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_main);
        listView.setAdapter(mMenuAdapter);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            // CursorAdapter returns a cursor at the correct position for getItem(), or null
            // if it cannot seek to that position.
            Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
            if (cursor != null) {
            // String locationSetting = Utility.getPreferredLocation(getActivity());
                int a =cursor.getColumnIndex(SantafeContract.CategoryEntry.COLUMN_CAT_TITLE);
                String a2=  cursor.getString(a);
            Intent intent = new Intent(getActivity(), DetailedMenu.class).setData(SantafeContract.DishEntry.buildDishCategory(
                    cursor.getString(cursor.getColumnIndex(SantafeContract.CategoryEntry.COLUMN_CAT_TITLE)
            )));

            startActivity(intent);
            }
            }
                    });



        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

//    String[] getAllCategories() {
//        List<String > categories= new ArrayList();
//
//
//        Cursor categoryCursor = this.getActivity().getContentResolver().query(
//                SantafeContract.CategoryEntry.CONTENT_URI,
//                new String[]{SantafeContract.CategoryEntry.COLUMN_CAT_TITLE},
//                null,
//                null,
//                null);
//
//        if (categoryCursor.moveToFirst()) {
//            while (categoryCursor.moveToNext()) {
//                int categoryIndex = categoryCursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_DISH_TITLE);
//                categories.add(categoryCursor.getString(categoryIndex));
//            }
//        }
//        String[] array = new String[categories.size()];
//        categories.toArray(array);
//
//        return array;
//    }



    private void updateWeather() {
        FetchMenuTask weatherTask = new FetchMenuTask(getActivity());
        weatherTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        //updateWeather();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
       // String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
//        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
//        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
//                locationSetting, System.currentTimeMillis());
//
//        return new CursorLoader(getActivity(),
//                weatherForLocationUri,
//                null,
//                null,
//                null,
//                sortOrder);

        return new CursorLoader(getActivity(),
                SantafeContract.CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                mMenuAdapter.swapCursor(cursor);
            }

                @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
                mMenuAdapter.swapCursor(null);
            }

    }


