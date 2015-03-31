package com.example.mikhail.santafe.data;


import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/*
    Uncomment this class when you are ready to test your UriMatcher.  Note that this class utilizes
    constants that are declared with package protection inside of the UriMatcher, which is why
    the test must be in the same data package as the Android app code.  Doing the test this way is
    a nice compromise between data hiding and testability.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String CATEGORY_QUERY = "SALADS";
    private static final long TEST_DATE = 1419033600L;  // December 20th, 2014
    private static final long TEST_LOCATION_ID = 10L;

    // content://com.example.android.sunshine.app/dish"
    private static final Uri TEST_DISH_DIR = SantafeContract.DishEntry.CONTENT_URI;
    private static final Uri TEST_DISH_WITH_CATEGORY_DIR = SantafeContract.DishEntry.buildDishCategory(CATEGORY_QUERY);

    // content://com.example.android.sunshine.app/category"
    private static final Uri TEST_CATEGORY_DIR = SantafeContract.CategoryEntry.CONTENT_URI;

    /*
         This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = SantafeProvider.buildUriMatcher();

        assertEquals("Error: The DISH URI was matched incorrectly.",
                testMatcher.match(TEST_DISH_DIR), SantafeProvider.DISH);
        assertEquals("Error: The WEATHER WITH LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_DISH_WITH_CATEGORY_DIR), SantafeProvider.DISH_WITH_CATEGORY);

        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_CATEGORY_DIR), SantafeProvider.CATEGORY);
    }
}