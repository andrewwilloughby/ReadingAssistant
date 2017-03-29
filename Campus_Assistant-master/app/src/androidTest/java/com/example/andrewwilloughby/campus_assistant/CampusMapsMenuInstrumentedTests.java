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

@RunWith(AndroidJUnit4.class)
public class CampusMapsMenuInstrumentedTests {

    @Rule
    public IntentsTestRule<CampusMapsMenuActivity> campusMapsMenuIntentsTestRule = new IntentsTestRule<CampusMapsMenuActivity>(CampusMapsMenuActivity.class);

    @Test
    public void testCampusMapsMenuLayout(){
        onView(withId(R.id.viewStyleBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.safetyBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.campusMapsMenuWhiteknightsBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.campusMapsLoroBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.campusMapsHallsBtn)).check(matches(isDisplayed()));
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
    public void testWhiteknightsButtonIsClickable(){
        onView(withId(R.id.campusMapsMenuWhiteknightsBtn)).check(matches(isClickable()));
    }

    @Test
    public void testWhiteknightsButtonHasCorrectText(){
        onView(withId(R.id.campusMapsMenuWhiteknightsBtn)).check(matches(withText("Whiteknights Campus")));
    }

    @Test
    public void testWhiteknightsButtonLoadsCorrectWebView(){
        onView(withId(R.id.campusMapsMenuWhiteknightsBtn)).perform(click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("http://www.reading.ac.uk/web/FILES/whiteknights-campus-map-and-keys-2016.pdf")),
                hasExtra(equalTo("webpageName"), equalTo("Whiteknights Campus Map")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageViewActivity")));
    }

    @Test
    public void testLondonRoadButtonIsClickable(){
        onView(withId(R.id.campusMapsLoroBtn)).check(matches(isClickable()));
    }

    @Test
    public void testLondonRoadButtonHasCorrectText(){
        onView(withId(R.id.campusMapsLoroBtn)).check(matches(withText("London Road Campus")));
    }

    @Test
    public void testLoroButtonLoadsCorrectWebView(){
        onView(withId(R.id.campusMapsLoroBtn)).perform(click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("http://www.reading.ac.uk/web/FILES/University-of-Reading-London-Road-COLOUR-NUMERIC.pdf")),
                hasExtra(equalTo("webpageName"), equalTo("London Road Campus Map")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageViewActivity")));
    }

    @Test
    public void testHallsButtonIsClickable(){
        onView(withId(R.id.campusMapsHallsBtn)).check(matches(isClickable()));
    }

    @Test
    public void testHallsButtonHasCorrectText(){
        onView(withId(R.id.campusMapsHallsBtn)).check(matches(withText("Student Halls")));
    }

    @Test
    public void testHallsButtonLoadsCorrectWebView(){
        onView(withId(R.id.campusMapsHallsBtn)).perform(click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("https://www.reading.ac.uk/web/FILES/accommodation/Walking_distances_to_halls.pdf")),
                hasExtra(equalTo("webpageName"), equalTo("Student Halls Map")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageViewActivity")));
    }
}
