package com.example.mikhail.santafe;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MenuAdapter extends CursorAdapter {
    public MenuAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_main_menu, parent, false);

        return view;
    }

    /**
     * Добавление валюты к цене
     */
    private String formatPrice(String price) {
        String newPrice="";
        if (price != null) {
            newPrice = price + "руб";
        }
        return newPrice;
    }

    /*
    TODO
    Написать метод добавления ккал к каллорийности
    Написать метод добавления грам к весу
     */



    /*
    This method gets titles of categories
     */
    private String convertTitleForUX (Cursor cursor) {
                // get row indices for our cursor
               // int idx_title = cursor.getColumnIndex(SantafeContract.CategoryEntry.COLUMN_CAT_TITLE);
                //String title = cursor.getString(idx_title);

                String title = cursor.getString(MenuFragment.COL_DISH_TITLE);
                return title;
            }


    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        TextView tv = (TextView)view;
       // tv.setText(convertCursorRowToUXFormat(cursor));
        tv.setText(convertTitleForUX(cursor));

        // Read weather icon ID from cursor
        int dishId = cursor.getInt(MenuFragment.COL_WEATHER_ID);
        // Use placeholder image for now
        //ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        //iconView.setImageResource(R.drawable.ic_launcher);

        // Read date from cursor
        long dateInMillis = cursor.getLong(MenuFragment.COL_DISH_TITLE);
        // Find TextView and set formatted date on it
//        TextView dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
//        dateView.setText(Utility.getFriendlyDayString(context, dateInMillis));
//
//        // Read weather forecast from cursor
//        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
//        // Find TextView and set weather forecast on it
//        TextView descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
//        descriptionView.setText(description);
//
//        // Read user preference for metric or imperial temperature units
//        boolean isMetric = Utility.isMetric(context);
//
//        // Read high temperature from cursor
//        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
//        TextView highView = (TextView) view.findViewById(R.id.list_item_high_textview);
//        highView.setText(Utility.formatTemperature(high, isMetric));
//
//        // Read low temperature from cursor
//        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
//        TextView lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
//        lowView.setText(Utility.formatTemperature(low, isMetric));
    }
}