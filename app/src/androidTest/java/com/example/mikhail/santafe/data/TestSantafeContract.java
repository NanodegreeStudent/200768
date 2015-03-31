package com.example.mikhail.santafe.data;


import android.net.Uri;
import android.test.AndroidTestCase;

/*
    Students: This is NOT a complete test for the WeatherContract --- just for the functions
    that we expect you to write.
 */
public class TestSantafeContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_DISH_CATEGORY = "MEAT";



    public void testBuildWeatherLocation() {
        Uri categoryUri = SantafeContract.DishEntry.buildDishCategory(TEST_DISH_CATEGORY);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildWeatherLocation in " +
                        "WeatherContract.",
                categoryUri);

        assertEquals("Error: Weather location not properly appended to the end of the Uri",
                TEST_DISH_CATEGORY, categoryUri.getLastPathSegment());
        assertEquals("Error: Weather location Uri doesn't match our expected result",
                categoryUri.toString(),
                "content://com.example.mikhail.santafe.app/dishes/MEAT");
    }
}