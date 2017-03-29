package com.example.andrewwilloughby.campus_assistant;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RailDeparturesInstrumentedTests {

    @Rule
    public IntentsTestRule<RailDeparturesActivity> railDeparturesIntentsTestRule  =
            new IntentsTestRule<>(RailDeparturesActivity.class);

    @Test
    public void railDeparturesLayout(){
        onView(withId(R.id.railList)).check(matches(isDisplayed()));
        onView(withId(R.id.trainHeaderTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void testRefreshTextViewHasCorrectText(){
        onView(withId(R.id.trainHeaderTextView)).check(matches(withText("Pull to refresh")));
    }
}
