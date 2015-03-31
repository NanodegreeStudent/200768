package com.example.mikhail.santafe;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mikhail.santafe.data.SantafeContract;


public class DetailedMenuAdapter extends CursorAdapter {
    public DetailedMenuAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_detiled_menu, parent, false);

        return view;
    }


    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

//        TextView tv = (TextView)view;
//        // tv.setText(convertCursorRowToUXFormat(cursor));
//        tv.setText(convertTitleForUX(cursor));

        int idx_imageId = cursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_IMAGE_ID);
        String imageId = cursor.getString(idx_imageId);

        ImageView dishIcon = (ImageView)view.findViewById(R.id.list_item_icon);
        dishIcon.setImageResource(Utility.getIconResourceForWeatherCondition(Integer.parseInt(imageId)));

        TextView dateView = (TextView) view.findViewById(R.id.list_item_detailed_menu_textview);
        dateView.setText(Utility.convertTitleForUX(cursor) );

        // Find TextView for description and set it to item
        TextView descTextView = (TextView) view.findViewById(R.id.list_item_detailed_menu_desc);
        descTextView.setText(Utility.getDescriptionForUX(cursor));


        // Find TextView for price and set it to item
        TextView priceTextView = (TextView)view.findViewById(R.id.list_item_detailed_menu_price);
        priceTextView.setText(Utility.getPriceForUX(cursor));


        TextView weightTextView = (TextView)view.findViewById(R.id.list_item_detailed_menu_weight);
        weightTextView.setText(Utility.getWeightForUX(cursor));

        // Read weather icon ID from cursor

        // Use placeholder image for now
        //ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        //iconView.setImageResource(R.drawable.ic_launcher);

        // Read date from cursor

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