package com.example.mikhail.santafe;


import android.database.Cursor;

import com.example.mikhail.santafe.data.SantafeContract;

public class Utility {

//    final static String CURRENCY_RUB = "руб";
//
//    final static String WEGHT = "г";
//    final static String CCAL = "ккал";

    final static String CURRENCY_RUB = "rub";

    final static String WEGHT = "g";
    final static String CCAL = "ccal";

    /**
     * Добавление валюты к цене
     */
    public static String formatPrice(int price) {
        String newPrice= price  + " " + CURRENCY_RUB;
        return newPrice;
    }

   public static String formatWeight(int weight){
        String newWeight = weight + " " + WEGHT;
        return newWeight;
    }

    public static String formatCcal(int ccal){
        String newCcal = ccal + " " + CCAL;
        return newCcal;
    }
    /*
  This method gets titles of categories
   */
    public static String convertTitleForUX (Cursor cursor) {
        // get row indices for our cursor
        // Добавьть описание,цену,калории, итд
        int idx_title = cursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_DISH_TITLE);
        String title = cursor.getString(idx_title);
        return title;
    }

    public static String getDescriptionForUX (Cursor cursor){
        int idx_Description = cursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_FULL_DESC);
        String description = cursor.getString(idx_Description);
        return description;
    }

    public static String getPriceForUX(Cursor cursor){
        int idx_Price = cursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_PRICE);
        int price = cursor.getInt(idx_Price);
        String priceForUX = formatPrice(price);
        return priceForUX;
    }

    public static String getWeightForUX(Cursor cursor){
        int idx_Weight = cursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_WEIGHT);
        int weight = cursor.getInt(idx_Weight);
        String weightForUX = formatWeight(weight);
        return weightForUX;
    }

    public static String getCcalForUX(Cursor cursor){
        int idx_ccal = cursor.getColumnIndex(SantafeContract.DishEntry.COLUMN_CCAL);
        int ccal= cursor.getInt(idx_ccal);
        String ccalForUx = formatCcal(ccal);
        return ccalForUx;
    }

    public static int getIconResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId == 11 ) {
            return R.drawable.sal_kapreze;
        } else if (weatherId ==12) {
            return R.drawable.sal_grech;
        } else if (weatherId == 13) {
            return R.drawable.sal_tricolory;
        } else if (weatherId == 14) {
            return R.drawable.sal_mashuk;
        }
           else if (weatherId == 21) {
            return R.drawable.ovoch_doch;
        }
            else if (weatherId ==22) {
            return R.drawable.cheese_doch;

            } else if (weatherId == 31) {
            return R.drawable.vegetables;


        }
        return -1;
    }
}

