package com.example.andrewwilloughby.campus_assistant;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by andrewwilloughby on 20/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MapsActivityInstrumentedTests {

    @Rule
    public IntentsTestRule<MapsActivity> mapsActivityIntentsTestRule = new IntentsTestRule<MapsActivity>(MapsActivity.class);

    @Test
    public void busTimesPreRequisites(){
        onView(withId(R.id.searchBox)).check(matches(isDisplayed()));
        onView(withId(R.id.searchBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.searchNearbySpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.searchNearbyTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchBoxHasCorrectHintText(){
        onView(CoreMatchers.allOf(withId(R.id.searchBox), withText(R.string.mapSearchHintText)));
    }

    @Test
    public void testSearchButtonIsClickable(){
        onView(withId(R.id.searchBtn)).check(matches(isClickable()));
    }

    @Test
    public void testSearchButtonHasCorrectText(){
        onView(CoreMatchers.allOf(withId(R.id.searchBtn), withText(R.string.mapSearchBtnText)));
    }

    @Test
    public void testShowNearbyTextViewHasCorrectText(){
        onView(CoreMatchers.allOf(withId(R.id.searchNearbyTextView), withText(R.string.search_nearby_textView_text)));
    }
}
