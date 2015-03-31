package com.example.mikhail.santafe;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mikhail.santafe.data.SantafeContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail on 06.03.2015.
 */
public  class DetailedMenuFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

//    final String SALAD = SantafeContract.DishEntry.buildDishCategory("Салаты").toString();
//    final String COLD = SantafeContract.DishEntry.buildDishCategory("Холодные закуски").toString();
//    final String HOT = SantafeContract.DishEntry.buildDishCategory("Горячие закуски").toString();

    final String SALAD = SantafeContract.DishEntry.buildDishCategory("Salads").toString();
    final String COLD = SantafeContract.DishEntry.buildDishCategory("Cold dishes").toString();
    final String HOT = SantafeContract.DishEntry.buildDishCategory("Hot dishes").toString();

    final int SALAD_KEY = 1;
    final int COLD_DISH_KEY = 2;
    final int HOT_DISH_KEY = 3;


    private static final int DETAIL_LOADER = 0;

    private String mForecast;

    private static final String LOG_TAG = DetailedMenuFragment.class.getSimpleName();
    private static final String[] FORECAST_COLUMNS = {

            SantafeContract.DishEntry.TABLE_NAME + "." + SantafeContract.DishEntry._ID,
            SantafeContract.DishEntry.COLUMN_DISH_TITLE,
            SantafeContract.DishEntry.COLUMN_FULL_DESC,
            SantafeContract.DishEntry.COLUMN_CCAL,
            SantafeContract.DishEntry.COLUMN_PRICE,
            SantafeContract.DishEntry.COLUMN_WEIGHT,
            SantafeContract.DishEntry.COLUMN_CAT_KEY,
            SantafeContract.DishEntry.COLUMN_IMAGE_ID
    };

    static final int COL_WEATHER_ID = 0;
    static final int COL_DISH_TITLE = 1;
    static final int COL_FULL_DESC = 2;
    static final int COL_CCAL = 3;
    static final int COL_PRICE = 4;
    static final int COL_WEIGHT = 5;
    static final int COL_CAT_KEY = 6;
    static  final int COL_IMAGE_ID = 7;

    private DetailedMenuAdapter  mMenuAdapter;



    public interface Callback {
        public void onItemSelected(Uri dateUri);
    }

    public DetailedMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



       // return inflater.inflate(R.layout.fragment_detailed_menu, container, false);

        mMenuAdapter = new DetailedMenuAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_detailed_menu, container, false);

        // Get a reference to the ListView, and attach this adapter to it.

        ListView listView = (ListView) rootView.findViewById(R.id.listview_detailed_menu);
        listView.setAdapter(mMenuAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    // String locationSetting = Utility.getPreferredLocation(getActivity());
                    int cat_key =cursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_CAT_KEY);
                    int dish_id = cursor.getColumnIndex(SantafeContract.DishEntry._ID);
//
//                    Intent intent = new Intent(getActivity(), DishDetails.class).setData(SantafeContract.DishEntry.buildDishCategoryWithId(
//                            cursor.getLong(cat_key),
//                           cursor.getLong(dish_id)
//                            ));

                    ((Callback) getActivity()).onItemSelected(SantafeContract.DishEntry.buildDishCategoryWithId(
                            cursor.getLong(cat_key),
                            cursor.getLong(dish_id)
                    ));

//                    startActivity(intent);
                }
            }
        });

        return rootView;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");

        String str;
        Intent intent = getActivity().getIntent();
        if (intent == null|| intent.getData() == null) {
            return null;
        }
        str = intent.getDataString();

        if (str.equals(SALAD)) {
            //  arr = getDishByCategoryId(SALAD_KEY);
            return new CursorLoader(
                    getActivity(),
                    SantafeContract.DishEntry.CONTENT_URI,
                    FORECAST_COLUMNS,
                    SantafeContract.DishEntry.COLUMN_CAT_KEY + " = ?",
                    new String[]{Long.toString(SALAD_KEY)},
                    null
            );
        }
        // Users chose cold dishes
        else if (str.equals(COLD)) {
            // arr  = getDishByCategoryId(COLD_DISH_KEY);
           // cursor = getCursorDishByCategoryId(COLD_DISH_KEY);
           // mMenuAdapter = new DetailedMenuAdapter(getActivity(), cursor, 0);

            return new CursorLoader(
                    getActivity(),
                    SantafeContract.DishEntry.CONTENT_URI,
                    FORECAST_COLUMNS,
                    SantafeContract.DishEntry.COLUMN_CAT_KEY + " = ?",
                    new String[]{Long.toString(COLD_DISH_KEY)},
                    null
            );
        }
        else if (str.equals(HOT)) {
            // arr  = getDishByCategoryId(COLD_DISH_KEY);
            // cursor = getCursorDishByCategoryId(COLD_DISH_KEY);
            // mMenuAdapter = new DetailedMenuAdapter(getActivity(), cursor, 0);

            return new CursorLoader(
                    getActivity(),
                    SantafeContract.DishEntry.CONTENT_URI,
                    FORECAST_COLUMNS,
                    SantafeContract.DishEntry.COLUMN_CAT_KEY + " = ?",
                    new String[]{Long.toString(HOT_DISH_KEY)},
                    null
            );
        }

        return new CursorLoader(
                getActivity(),
                SantafeContract.DishEntry.CONTENT_URI,
                FORECAST_COLUMNS,
                SantafeContract.DishEntry.COLUMN_CAT_KEY + " = ?",
                new String[]{Long.toString(COLD_DISH_KEY)},
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        Log.v(LOG_TAG, "In onLoadFinished");
//        if (!data.moveToFirst()) { return; }
//
//        String title = data.getString(COL_DISH_TITLE);
//
//        String dishDescription =
//                data.getString(COL_FULL_DESC);
//
//        mForecast = title;
//
//        View view = getView().findViewById(R.id.list_item_detailed_menu_textview);
//        TextView detailTextView = (TextView)getView().findViewById(R.id.list_item_detailed_menu_textview);
//        detailTextView.setText(mForecast);


        mMenuAdapter.swapCursor(data);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
    //    if (mShareActionProvider != null) {
      //      mShareActionProvider.setShareIntent(createShareForecastIntent());


        }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) { mMenuAdapter.swapCursor(null);}

//        Intent intent = getActivity().getIntent();
//
//       String[] arr= new String[]{};
//        Cursor cursor;
//        String str;
//        String value;
//        if (intent != null) {
//            str = intent.getDataString();
//
////            try {
////                value = URLDecoder.decode(str, HTTP.UTF_8);
////            } catch (UnsupportedEncodingException e) {
////                throw new AssertionError("UTF-8 is unknown");
////                // or 'throw new AssertionError("Impossible things are happening today. " +
////                //                              "Consider buying a lottery ticket!!");'
////            }
//
//
//            // User chose salads
//            if (str.equals(SALAD)) {
//                //  arr = getDishByCategoryId(SALAD_KEY);
//                cursor = getCursorDishByCategoryId(SALAD_KEY);
//                mMenuAdapter = new DetailedMenuAdapter(getActivity(), cursor, 0);
//            }
//            // Users chose cold dishes
//            else if (str.equals(COLD)) {
//               // arr  = getDishByCategoryId(COLD_DISH_KEY);
//                cursor = getCursorDishByCategoryId(COLD_DISH_KEY);
//                // The CursorAdapter will take data from our cursor and populate the ListView.
//                mMenuAdapter = new DetailedMenuAdapter(getActivity(), cursor, 0);
//            }
//
//
//            // ПРОСТО ПО
//          // arr2 = getAllCategories();
//        }
//
//
//
//        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
//
//        // Get a reference to the ListView, and attach this adapter to it.
//        ListView listView = (ListView) rootView.findViewById(R.id.listview_main);
//        listView.setAdapter(mMenuAdapter);
//
//        return rootView;
//    }



    String[] getAllCategories() {
        List<String > categories= new ArrayList();


        Cursor categoryCursor = this.getActivity().getContentResolver().query(
                SantafeContract.CategoryEntry.CONTENT_URI,
                new String[]{SantafeContract.CategoryEntry.COLUMN_CAT_TITLE},
                null,
                null,
                null);

        if (categoryCursor.moveToFirst()) {
            while (categoryCursor.moveToNext()) {
                int categoryIndex = categoryCursor.getColumnIndex(SantafeContract.CategoryEntry.COLUMN_CAT_TITLE);
                categories.add(categoryCursor.getString(categoryIndex));
            }
        }
        String[] array = new String[categories.size()];
        categories.toArray(array);

        return array;
    }


    Cursor getCursorDishByCategoryId(long id) {
        List<String > category= new ArrayList();

        Cursor categoryCursor = this.getActivity().getContentResolver().query(
                SantafeContract.DishEntry.CONTENT_URI,
                new String[]{SantafeContract.DishEntry.COLUMN_DISH_TITLE},
                SantafeContract.DishEntry.COLUMN_CAT_KEY + " = ?",
                new String[]{Long.toString(id)},
                null);


        return categoryCursor;
    }

    /*
     Return array of string (dishes) by category
     */
    String[] getDishByCategoryId(long id) {
        List<String > category= new ArrayList();

//        int categoryCursor2 = this.getActivity().getContentResolver().delete(
//                SantafeContract.DishEntry.CONTENT_URI,
//                null,
//                null
//        );

        Cursor categoryCursor = this.getActivity().getContentResolver().query(
                SantafeContract.DishEntry.CONTENT_URI,
                new String[]{SantafeContract.DishEntry.COLUMN_DISH_TITLE},
                SantafeContract.DishEntry.COLUMN_CAT_KEY + " = ?",
                new String[]{Long.toString(id)},
                null);

        if (categoryCursor.moveToFirst()) {
            while (categoryCursor.moveToNext()) {
                int categoryIndex = categoryCursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_DISH_TITLE);
                category.add(categoryCursor.getString(categoryIndex));
            }
        }
        String[] array = new String[category.size()];
        category.toArray(array);

        return array;
    }
}