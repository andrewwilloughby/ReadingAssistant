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
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by andrewwilloughby on 18/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class CampusNavigationMenuInstrumentedTests {

    @Rule
    public IntentsTestRule<CampusNavigationMenu> studentInfoMenuIntentsTestRule = new IntentsTestRule<CampusNavigationMenu>(CampusNavigationMenu.class);

    @Test
    public void campusNavigationMenuPreRequisites(){
        onView(withId(R.id.viewStyleBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.safetyBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.campusNavInteractiveMapBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.campusNavMapsBtn)).check(matches(isDisplayed()));
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
    public void testInteractiveMapButtonIsClickable(){
        onView(withId(R.id.campusNavInteractiveMapBtn)).check(matches(isClickable()));
    }

    @Test
    public void testInteractiveMapButtonHasCorrectText(){
        onView(allOf(withId(R.id.campusNavInteractiveMapBtn), withText(R.string.campusNavInteractiveMapBtnText)));
    }

    @Test
    public void testInteractiveMapButtonFiresCorrectIntent(){
        onView(withId(R.id.campusNavInteractiveMapBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.MapsActivity"));
    }

    @Test
    public void testCampusMapsButtonIsClickable(){
        onView(withId(R.id.campusNavMapsBtn)).check(matches(isClickable()));
    }

    @Test
    public void testCampusMapsButtonHasCorrectText(){
        onView(allOf(withId(R.id.campusNavMapsBtn), withText(R.string.campusNavMapsBtnText)));
    }

    @Test
    public void testCampusMapsButtonFiresCorrectIntent(){
        onView(withId(R.id.campusNavMapsBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.CampusMapsMenu"));
    }
}
