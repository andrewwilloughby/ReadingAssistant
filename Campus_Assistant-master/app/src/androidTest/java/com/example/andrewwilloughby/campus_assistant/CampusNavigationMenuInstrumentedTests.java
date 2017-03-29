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

@RunWith(AndroidJUnit4.class)
public class CampusNavigationMenuInstrumentedTests {

    @Rule
    public IntentsTestRule<CampusNavigationMenuActivity> studentInfoMenuIntentsTestRule = new IntentsTestRule<>(CampusNavigationMenuActivity.class);

    @Test
    public void testCampusNavigationMenuLayout(){
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
    public void testSafetyButtonIsClickable(){
        onView(withId(R.id.safetyBtn)).check(matches(isClickable()));
    }

    @Test
    public void testSafetyButtonClickFiresCorrectIntent(){
        onView(withId(R.id.safetyBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.SafetyInfoActivity"));
    }

    @Test
    public void testInteractiveMapButtonIsClickable(){
        onView(withId(R.id.campusNavInteractiveMapBtn)).check(matches(isClickable()));
    }

    @Test
    public void testInteractiveMapButtonHasCorrectText(){
        onView(withId(R.id.campusNavInteractiveMapBtn)).check(matches(withText("Interactive Map")));
    }

    @Test
    public void testInteractiveMapButtonOpensMapsActivity(){
        onView(withId(R.id.campusNavInteractiveMapBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.InteractiveMapActivity"));
    }

    @Test
    public void testCampusMapsButtonIsClickable(){
        onView(withId(R.id.campusNavMapsBtn)).check(matches(isClickable()));
    }

    @Test
    public void testCampusMapsButtonHasCorrectText(){
        onView(withId(R.id.campusNavMapsBtn)).check(matches(withText("Campus Maps")));
    }

    @Test
    public void testCampusMapsButtonOpensCampusMapsMenu(){
        onView(withId(R.id.campusNavMapsBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.CampusMapsMenuActivity"));
    }
}
