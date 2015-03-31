package com.example.mikhail.santafe.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.mikhail.santafe.data.SantafeContract.CategoryEntry;
import com.example.mikhail.santafe.data.SantafeContract.DishEntry;

/**
 * Created by Mikhail on 06.03.2015.
 */
public class SantafeProvider extends ContentProvider {

    // The URI Matcher used by content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SantafeDbHelper mOpenHelper;

    static final int DISH = 100;
    static final int DISH_WITH_CATEGORY = 101;
    static final int DISH_WITH_CATEGORY_AND_ID = 102;
    static final int CATEGORY = 300;

    private static final SQLiteQueryBuilder sDishByCategorySettingQueryBuilder;

    static{
            sDishByCategorySettingQueryBuilder = new SQLiteQueryBuilder();

            //This is an inner join which looks like
            //dish INNER JOIN category ON dish.category_id = category._id
            sDishByCategorySettingQueryBuilder.setTables(
                SantafeContract.DishEntry.TABLE_NAME + " INNER JOIN " +
                                SantafeContract.CategoryEntry.TABLE_NAME +
                                " ON " +  SantafeContract.DishEntry.TABLE_NAME +
                                "." +  SantafeContract.DishEntry.COLUMN_CAT_KEY +
                                " = " +  SantafeContract.CategoryEntry.TABLE_NAME +
                                "." +  SantafeContract.CategoryEntry._ID);
            }

    private static final String sCategorySettingSelection =
                        CategoryEntry.TABLE_NAME +
                                       "." + CategoryEntry.COLUMN_CAT_TITLE + " = ? ";

    private static final String sCategorySettingAndIdSelection =
            SantafeContract.CategoryEntry.TABLE_NAME +
                    "." + CategoryEntry._ID + " = ? AND " +
                    DishEntry.TABLE_NAME + "."  + DishEntry._ID + " = ? ";

    private Cursor getDishByCategorySetting(Uri uri, String[] projection, String sortOrder) {
        String categorySetting = DishEntry.getCategorySettingFromUri(uri);

        String[] selectionArgs;
        String selection;
        selection = sCategorySettingSelection;
        selectionArgs = new String[]{categorySetting};

        return sDishByCategorySettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getWeatherByLocationSettingAndDate(
            Uri uri, String[] projection, String sortOrder) {
        String categorySetting = SantafeContract.DishEntry.getCategorySettingFromUri(uri);
        long dishId = SantafeContract.DishEntry.getDishIdFromUri(uri);

        return sDishByCategorySettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sCategorySettingAndIdSelection,
                new String[]{categorySetting , Long.toString(dishId)},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
                final String authority = SantafeContract.CONTENT_AUTHORITY;

                matcher.addURI(authority, SantafeContract.PATH_DISHES, DISH);
                matcher.addURI(authority, SantafeContract.PATH_DISHES + "/*", DISH_WITH_CATEGORY);
                matcher.addURI(authority, SantafeContract.PATH_CATEGORIES, CATEGORY);
                matcher.addURI(authority, SantafeContract.PATH_DISHES + "/*/#", DISH_WITH_CATEGORY_AND_ID);

                return matcher;
           }

    @Override
        public boolean onCreate() {
                mOpenHelper = new SantafeDbHelper(getContext());
                return true;
            }

    @Override
        public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
         final int match = sUriMatcher.match(uri);
          switch (match) {

              case DISH_WITH_CATEGORY:
                  return SantafeContract.DishEntry.CONTENT_TYPE;

              case DISH_WITH_CATEGORY_AND_ID:
              return SantafeContract.DishEntry.CONTENT_ITEM_TYPE;



              case DISH:
                        return DishEntry.CONTENT_TYPE;

              case CATEGORY:
                        return CategoryEntry.CONTENT_TYPE;

              default:
                        throw new UnsupportedOperationException("Unknown uri: " + uri);
                        }
            }

    @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                                                    String sortOrder) {
              // Here's the switch statement that, given a URI, will determine what kind of request it is,
              // and query the database accordingly.
                              Cursor retCursor;
               switch (sUriMatcher.match(uri)) {

                       // "dish/*"
                               case DISH_WITH_CATEGORY: {
                                retCursor = getDishByCategorySetting(uri, projection, sortOrder);
                               break;
                            }
                        // "dish"
                   case DISH: {
                       retCursor = mOpenHelper.getReadableDatabase().query(
                               SantafeContract.DishEntry.TABLE_NAME,
                               projection,
                               selection,
                               selectionArgs,
                               null,
                               null,
                               sortOrder
                       );
                       break;
                   }

                   case DISH_WITH_CATEGORY_AND_ID:
                   {
                       retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
                       break;
                   }
                        // "category"
                   case CATEGORY: {
                       retCursor = mOpenHelper.getReadableDatabase().query(
                               SantafeContract.CategoryEntry.TABLE_NAME,
                               projection,
                               selection,
                               selectionArgs,
                               null,
                               null,
                               sortOrder
                       );
                       break;
                   }
                                default:
                                throw new UnsupportedOperationException("Unknown uri: " + uri);
                        }
                retCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return retCursor;
            }

    @Override
        public Uri insert(Uri uri, ContentValues values) {
                final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                final int match = sUriMatcher.match(uri);
                Uri returnUri;

                        switch (match) {
                        case DISH: {

                                long _id = db.insert(DishEntry.TABLE_NAME, null, values);
                                if ( _id > 0 )
                                        returnUri = DishEntry.buildDishUri(_id);
                                else
                                   throw new android.database.SQLException("Failed to insert row into " + uri);
                                break;
                            }
                            case CATEGORY: {
                                long _id = db.insert(SantafeContract.CategoryEntry.TABLE_NAME, null, values);
                                if ( _id > 0 )
                                        returnUri = SantafeContract.CategoryEntry.buildCategoryUri(_id);
                                else
                                    throw new android.database.SQLException("Failed to insert row into " + uri);
                                break;
                                            }
                        default:
                                throw new UnsupportedOperationException("Unknown uri: " + uri);
                        }
                getContext().getContentResolver().notifyChange(uri, null);
                db.close();
                return returnUri;
            }

    @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                final int match = sUriMatcher.match(uri);
                int rowsDeleted;
                // this makes delete all rows return the number of rows deleted
                        if ( null == selection ) selection = "1";
                switch (match) {
                        case DISH:
                               // rowsDeleted = db.delete(
                                               // SantafeContract.DishEntry.TABLE_NAME, selection, selectionArgs);
                            rowsDeleted = db.delete( SantafeContract.DishEntry.TABLE_NAME, "1", null);

                                break;
                        case CATEGORY:
                                rowsDeleted = db.delete(
                                                SantafeContract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
                                break;
                       default:
                                throw new UnsupportedOperationException("Unknown uri: " + uri);
                        }
                // Because a null deletes all rows
                        if (rowsDeleted != 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                return rowsDeleted;
    }

    @Override
       public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                final int match = sUriMatcher.match(uri);
                int rowsUpdated;

                        switch (match) {
                        case DISH:
                              rowsUpdated = db.update(SantafeContract.DishEntry.TABLE_NAME, values, selection,
                                                selectionArgs);
                                break;

                        case CATEGORY:
                                rowsUpdated = db.update(SantafeContract.CategoryEntry.TABLE_NAME, values, selection,
                                                selectionArgs);
                                break;

                        default:
                                throw new UnsupportedOperationException("Unknown uri: " + uri);
                        }
                if (rowsUpdated != 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                return rowsUpdated;
    }

    @Override
        public int bulkInsert(Uri uri, ContentValues[] values) {
                final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                final int match = sUriMatcher.match(uri);
                switch (match) {
                       case DISH:
                                db.beginTransaction();
                                int returnCount = 0;
                                try {
                                        for (ContentValues value : values) {

                                                long _id = db.insert(DishEntry.TABLE_NAME, null, value);
                                                if (_id != -1) {
                                                        returnCount++;
                                                    }
                                            }
                                        db.setTransactionSuccessful();
                                    } finally {
                                        db.endTransaction();
                                    }
                                getContext().getContentResolver().notifyChange(uri, null);
                                return returnCount;
                        default:
                                return super.bulkInsert(uri, values);
                    }
            }

//        @Override
//        @TargetApi(10)
//        public void shutdown() {
//        mOpenHelper.close();
//        super.shutdown();
//        }


}
