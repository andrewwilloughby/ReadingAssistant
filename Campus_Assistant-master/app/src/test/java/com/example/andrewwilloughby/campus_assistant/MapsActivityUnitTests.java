package com.example.andrewwilloughby.campus_assistant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MapsActivityUnitTests {

    MapsActivity mapsActivity = new MapsActivity();

    @Test
    public void test_DrawPath_PassNoData(){
       Boolean expected = false;
       Boolean actual = mapsActivity.drawPath("");

       assertThat(actual, is(expected));
    }
    @Test
    public void testMakeDirectionsURL_InvalidLatLng(){
        String expected = null;
        String actual = mapsActivity.makeDirectionsURL("abcde", "12.0495");
        assertThat(actual, is(expected));
    }
    @Test
    public void testMakeDirectionsURL_Valid(){
        String expected = "https://maps.googleapis.com/maps/api/directions/json?origin=51.509865,-0.118092&destination=51.568535,-1.772232&sensor=false&mode=walking&alternatives=true&key=AIzaSyCWyi6EulBxUxWDPZKwV9Ud6AZnmBqkWfE";
        String actual = mapsActivity.makeDirectionsURL("51.509865,-0.118092","51.568535,-1.772232");
        assertThat(actual, is(expected));
    }
    @Test
    public void testBuildPlacesURL_ValidLatLng(){
        double latitude = 51.509865;
        double longitude = -0.118092;

        String expected = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.509865,-0.118092&radius=" + mapsActivity.PROXIMITY_RADIUS + "&type=restaurant&sensor=true&key=AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0";
        String actual = mapsActivity.buildPlacesUrl(latitude, longitude, "restaurant");
        assertThat(actual, is(expected));
    }
}