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

@RunWith(AndroidJUnit4.class)
public class BusTimesInstrumentedTests {

    @Rule
    public IntentsTestRule<BusTimesActivity> busTimesIntentsTestRule = new IntentsTestRule<BusTimesActivity>(BusTimesActivity.class);

    @Test
    public void testBusTimesLayout(){
        onView(withId(R.id.chancellorWayBusBtn)).check(matches(allOf(isDisplayed(), isClickable(), withText("Chancellors Way"))));
        onView(withId(R.id.whiteknightsHouseBusBtn)).check(matches(allOf(isDisplayed(), isClickable(), withText("Whiteknights House"))));
        onView(withId(R.id.readingStationBusBtn)).check(matches(allOf(isDisplayed(), isClickable(), withText("Reading Station"))));
        onView(withId(R.id.busList)).check(matches(isDisplayed()));
    }

    //Record tests to populate this further.

}
