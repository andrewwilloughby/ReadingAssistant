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
 * Created by andrewwilloughby on 19/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class BusTimesInstrumentedTests {

    @Rule
    public IntentsTestRule<BusTimes> busTimesIntentsTestRule = new IntentsTestRule<BusTimes>(BusTimes.class);

    @Test
    public void busTimesPreRequisites(){
        onView(withId(R.id.chancellorWayBusBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.whiteknightsHouseBusBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.readingStationBusBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.busList)).check(matches(isDisplayed()));
    }

    @Test
    public void testChancellorsWayButtonIsClickable(){
        onView(withId(R.id.chancellorWayBusBtn)).check(matches(isClickable()));
    }

    @Test
    public void testChancellorsWayButtonHasCorrectText(){
        onView(CoreMatchers.allOf(withId(R.id.chancellorWayBusBtn), withText(R.string.chancellorWayBusBtnText)));
    }

    @Test
    public void testWhiteknightsHouseButtonIsClickable(){
        onView(withId(R.id.whiteknightsHouseBusBtn)).check(matches(isClickable()));
    }

    @Test
    public void testWhiteknightsHouseButtonHasCorrectText(){
        onView(CoreMatchers.allOf(withId(R.id.whiteknightsHouseBusBtn), withText(R.string.whiteknightsHouseBusBtnText)));
    }

    @Test
    public void testReadingStationButtonIsClickable(){
        onView(withId(R.id.readingStationBusBtn)).check(matches(isClickable()));
    }

    @Test
    public void testReadingStationButtonHasCorrectText(){
        onView(CoreMatchers.allOf(withId(R.id.readingStationBusBtn), withText(R.string.railStationBusBtnText)));
    }
}
