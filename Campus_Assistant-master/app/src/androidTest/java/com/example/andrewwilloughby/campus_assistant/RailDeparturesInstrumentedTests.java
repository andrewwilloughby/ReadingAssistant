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

/**
 * Created by andrewwilloughby on 19/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RailDeparturesInstrumentedTests {

    @Rule
    public IntentsTestRule<RailDepartures> railDeparturesIntentsTestRule  = new IntentsTestRule<RailDepartures>(RailDepartures.class);

    @Test
    public void railDeparturesPreRequisites(){
        onView(withId(R.id.railList)).check(matches(isDisplayed()));
    }
}
