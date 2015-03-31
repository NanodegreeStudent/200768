package com.example.mikhail.santafe.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the menu database.
 */
public class SantafeContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.

    public static final String CONTENT_AUTHORITY = "com.example.mikhail.santafe.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DISHES = "dishes";
    public static final String PATH_CATEGORIES = "categories";


    // Table for categoies of disches (salad,soup etc)
        public static final class CategoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES;

        // Table name
        public static final String TABLE_NAME = "category";
        // The name of category
        public static final String COLUMN_CAT_TITLE = "cat_title";

        public static Uri buildCategoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        /* TODO Uncomment for
        4b - Adding ContentProvider to our Contract
        https://www.udacity.com/course/viewer#!/c-ud853/l-1576308909/m-1637521471

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        */

            /**
             * TODO YOUR CODE BELOW HERE FOR QUIZ
             * QUIZ - 4b - Adding LocationEntry with ID UriBuilder
             * https://www.udacity.com/course/viewer#!/c-ud853/l-1576308909/e-1604969848/m-1604969849
             **/

        }

        /*
        /* Inner class that defines the table contents of the weather table */
        public static final class DishEntry implements BaseColumns {

            public static final Uri CONTENT_URI =
                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_DISHES).build();

            public static final String CONTENT_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DISHES;

            public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DISHES;

            public static final String TABLE_NAME = "dish";

            public static Uri buildDishUri(long id) {
                return ContentUris.withAppendedId(CONTENT_URI, id);
            }


            public static Uri buildDishCategoryWithId(long categorySetting, long id) {
                return CONTENT_URI.buildUpon().appendPath(Long.toString(categorySetting))
                        .appendPath(Long.toString(id)).build();
            }

            //For query with category of dish
            public static Uri buildDishCategory(String categorySetting) {
                return CONTENT_URI.buildUpon().appendPath(categorySetting).build();
            }

            public static String getCategorySettingFromUri(Uri uri) {
                            return uri.getPathSegments().get(1);
                       }



            public static long getDishIdFromUri(Uri uri) {
                return Long.parseLong(uri.getPathSegments().get(2));
            }

            // Dish id as returned by API, to identify the icon to be used
            public static final String COLUMN_DISH_ID = "dish_id";

            // Title of dish
            public static final String COLUMN_DISH_TITLE = "dish_title";

            // Full description of the dish, as provided by API.
            public static final String COLUMN_FULL_DESC = "full_desc";

            // Price
            public static final String COLUMN_PRICE = "price";

            // Caloric value (ccal)
            public static final String COLUMN_CCAL = "caloric_value";

            // Column with the foreign key into the category table.
            public static final String COLUMN_CAT_KEY = "category_id";

            // Weight
            public static final String COLUMN_WEIGHT = "weight";

            public static  final String COLUMN_IMAGE_ID = "image_id";

        }
    }
