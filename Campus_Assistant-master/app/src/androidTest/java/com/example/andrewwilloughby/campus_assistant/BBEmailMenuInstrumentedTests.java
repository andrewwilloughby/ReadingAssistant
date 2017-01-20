package com.example.andrewwilloughby.campus_assistant;

/**
 * Created by andrewwilloughby on 16/01/2017.
 */

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.CoreMatchers;
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
    public IntentsTestRule<BBEmailMenu> bbEmailMenuIntentsTestRule = new IntentsTestRule<BBEmailMenu>(BBEmailMenu.class);

    @Test
    public void blackboardEmailMenupreRequisites(){
        onView(withId(R.id.viewStyleBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.safetyBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.bbemailMenuBlackboardBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.bbemailMenuEmailBtn)).check(matches(isDisplayed()));
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
    public void testBlackboardButtonIsClickable(){
        onView(withId(R.id.bbemailMenuBlackboardBtn)).check(matches(isClickable()));
    }

    @Test
    public void testBlackboardButtonHasCorrectText(){
        onView(CoreMatchers.allOf(withId(R.id.bbemailMenuBlackboardBtn), withText(R.string.bbemailBlackboardBtnText)));
    }

    @Test
    public void testBlackboardButtonFiresCorrectIntent(){
        onView(withId(R.id.bbemailMenuBlackboardBtn)).perform(click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("https://bb.reading.ac.uk/")),
                hasExtra(equalTo("webpageName"), equalTo("Blackboard")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));
    }

    @Test
    public void testEmailButtonIsClickable(){
        onView(withId(R.id.bbemailMenuEmailBtn)).check(matches(isClickable()));
    }

    @Test
    public void testEmailButtonHasCorrectText(){
        onView(CoreMatchers.allOf(withId(R.id.bbemailMenuEmailBtn), withText(R.string.bbemailEmailBtnText)));
    }

    @Test
    public void testEmailButtonFiresCorrectIntent(){
        onView(withId(R.id.bbemailMenuEmailBtn)).perform(click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("http://mail.live.reading.ac.uk/")),
                hasExtra(equalTo("webpageName"), equalTo("University Email")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));
    }
}

