package com.example.andrewwilloughby.campus_assistant;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by andrewwilloughby on 18/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class TravelInformationMenuInstrumentedTests {

    @Rule
    public IntentsTestRule<TravelInformationMenu> travelInformationMenuIntentsTestRule = new IntentsTestRule<TravelInformationMenu>(TravelInformationMenu.class);

    @Test
    public void travelInformationMenuPreRequisites(){
        onView(withId(R.id.viewStyleBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.safetyBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.travelInfoBusTimesBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.travelInfoRailTimesBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.travelInfoBusTimetableBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testViewStyleButtonIsClickable(){
        onView(withId(R.id.viewStyleBtn)).check(matches(isClickable()));
    }

    @Test
    public void testViewStyleButtonFiresCorrectIntent(){
        onView(withId(R.id.viewStyleBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.MainActivity"));
    }

    @Test
    public void testSafetyButtonIsClickable(){
        onView(withId(R.id.safetyBtn)).check(matches(isClickable()));
    }

    @Test
    public void testSafetyButtonClickFiresCorrectIntent(){
        onView(withId(R.id.safetyBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.SafetyInfo"));
    }

    @Test
    public void testBusTimesButtonIsClickable(){
        onView(withId(R.id.travelInfoBusTimesBtn)).check(matches(isClickable()));
    }

    @Test
    public void testBusTimesButtonHasCorrectText(){
        onView(allOf(withId(R.id.travelInfoBusTimesBtn), withText(R.string.travelInfoBusTimesBtnText)));
    }

    @Test
    public void testBusTimesButtonClickFiresCorrectIntent(){
        onView(withId(R.id.travelInfoBusTimesBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.BusTimes"));
    }

    @Test
    public void testRailTimesButtonIsClickable(){
        onView(withId(R.id.travelInfoRailTimesBtn)).check(matches(isClickable()));
    }

    @Test
    public void testRailTimesButtonHasCorrectText(){
        onView(allOf(withId(R.id.travelInfoRailTimesBtn), withText(R.string.travelInfoRailTimesBtnText)));
    }

    @Test
    public void testRailTimesButtonClickFiresCorrectIntent(){
        onView(withId(R.id.travelInfoRailTimesBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.RailDepartures"));
    }

    @Test
    public void testBusTimetableButtonIsClickable(){
        onView(withId(R.id.travelInfoBusTimetableBtn)).check(matches(isClickable()));
    }

    @Test
    public void testBusTimetableButtonHasCorrectText(){
        onView(allOf(withId(R.id.travelInfoBusTimetableBtn), withText(R.string.travelInfoBusTimetableBtnText)));
    }

    @Test
    public void testBusTimetableButtonClickFiresCorrectIntent(){
        onView(withId(R.id.travelInfoBusTimetableBtn)).perform(click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("http://www.reading-buses.co.uk/files/timetables/current/claret%20times.pdf")),
                hasExtra(equalTo("webpageName"), equalTo("University Bus Timetable")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));
    }
}
