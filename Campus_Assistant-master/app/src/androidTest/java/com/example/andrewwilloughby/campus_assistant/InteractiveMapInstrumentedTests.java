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
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class InteractiveMapInstrumentedTests {

    @Rule
    public IntentsTestRule<InteractiveMapActivity> interactiveMapActivityIntentsTestRule = new IntentsTestRule<>(InteractiveMapActivity.class);

    @Test
    public void testInteractiveMapLayout(){
        onView(withId(R.id.searchBox)).check(matches(isDisplayed()));
        onView(withId(R.id.searchBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.searchNearbyTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.searchNearbySpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchBoxIsClickable(){
        onView(withId(R.id.searchBox)).check(matches(isClickable()));
    }

    @Test
    public void testSearchBoxHasCorrectText(){
        onView(withId(R.id.searchBox)).check(matches((withHint(R.string.mapSearchHintText))));
    }

    @Test
    public void testSearchButtonIsClickable(){
        onView(withId(R.id.searchBtn)).check(matches(isClickable()));
    }

    @Test
    public void testSearchButtonHasCorrectText(){
        onView(withId(R.id.searchBtn)).check(matches((withText("Search"))));
    }

    @Test
    public void testShowNearbyTextViewHasCorrectText(){
        onView(withId(R.id.searchNearbyTextView)).check(matches((withText("Show nearby:"))));
    }

    @Test
    public void testSearchNearbySpinnerIsClickable(){
        onView(withId(R.id.searchNearbySpinner)).check(matches(isClickable()));
    }
}
