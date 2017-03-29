package com.example.andrewwilloughby.campus_assistant;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class BusTimesInstrumentedTests {

    @Rule
    public IntentsTestRule<BusTimesActivity> busTimesIntentsTestRule = new IntentsTestRule<BusTimesActivity>(BusTimesActivity.class);

    @Test
    public void testBusTimesLayout(){
        onView(withId(R.id.chancellorWayBusBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.whiteknightsHouseBusBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.readingStationBusBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.busList)).check(matches(isDisplayed()));
        onView(withId(R.id.routeTxtView)).check(matches(isDisplayed()));
        onView(withId(R.id.destinationTxtView)).check(matches(isDisplayed()));
        onView(withId(R.id.arrivalTxtView)).check(matches(isDisplayed()));
        onView(withId(R.id.busList)).check(matches(isDisplayed()));
        onView(withId(R.id.busHeaderTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void testChancellorsWayButtonIsClickable(){
        onView(withId(R.id.chancellorWayBusBtn)).check(matches(isClickable()));
    }

    @Test
    public void testChancellorsWayButtonHasCorrectText(){
        onView(withId(R.id.chancellorWayBusBtn)).check(matches(withText("Chancellors Way")));
    }

    @Test
    public void testWhiteknightsHouseButtonIsClickable(){
        onView(withId(R.id.whiteknightsHouseBusBtn)).check(matches(isClickable()));
    }

    @Test
    public void testWhiteknightsHouseButtonHasCorrectText(){
        onView(withId(R.id.whiteknightsHouseBusBtn)).check(matches(withText("Whiteknights House")));
    }

    @Test
    public void testReadingStationButtonIsClickable(){
        onView(withId(R.id.readingStationBusBtn)).check(matches(isClickable()));
    }

    @Test
    public void testReadingStationButtonHasCorrectText(){
        onView(withId(R.id.readingStationBusBtn)).check(matches(withText("Reading Station")));
    }

    @Test
    public void testRouteTextViewHasCorrectText(){
        onView(withId(R.id.routeTxtView)).check(matches(withText("Route")));
    }

    @Test
    public void testDestinationTextViewHasCorrectText(){
        onView(withId(R.id.destinationTxtView)).check(matches(withText("Journey Destination")));
    }

    @Test
    public void testArrivalTextViewHasCorrectText(){
        onView(withId(R.id.arrivalTxtView)).check(matches(withText("Time")));
    }

    @Test
    public void testBusHeaderTextViewHasCorrectText(){
        onView(withId(R.id.busHeaderTextView)).check(matches(withText("Pull to refresh")));
    }

}
