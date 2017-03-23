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
public class BBEmailMenuInstrumentedTests {

    @Rule
    public IntentsTestRule<BBEmailMenuActivity> bbEmailMenuIntentsTestRule = new IntentsTestRule<BBEmailMenuActivity>(BBEmailMenuActivity.class);

    @Test
    public void testBlackboardEmailMenuLayout(){
        onView(withId(R.id.viewStyleBtn)).check(matches(allOf(isDisplayed(), isClickable())));
        onView(withId(R.id.safetyBtn)).check(matches(allOf(isDisplayed(), isClickable())));
        onView(withId(R.id.bbemailMenuBlackboardBtn)).check(matches(allOf(isDisplayed(), isClickable(), withText("Blackboard"))));
        onView(withId(R.id.bbemailMenuEmailBtn)).check(matches(allOf(isDisplayed(), isClickable(), withText("Email"))));
    }

    @Test
    public void testViewStyleButtonOpensMainActivity(){
        onView(withId(R.id.viewStyleBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.MainActivity"));
    }

    @Test
    public void testSafetyButtonOpensSafetyInfoActivity(){
        onView(withId(R.id.safetyBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.SafetyInfoActivity"));
    }

    @Test
    public void testBlackboardButtonOpensBlackboardActivity(){
        onView(withId(R.id.bbemailMenuBlackboardBtn)).perform(click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("https://bb.reading.ac.uk/")),
                hasExtra(equalTo("webpageName"), equalTo("Blackboard")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageViewActivity")));
    }

    @Test
    public void testEmailButtonOpensEmailActivity(){
        onView(withId(R.id.bbemailMenuEmailBtn)).perform(click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("http://mail.live.reading.ac.uk/")),
                hasExtra(equalTo("webpageName"), equalTo("University Email")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageViewActivity")));
    }
}

