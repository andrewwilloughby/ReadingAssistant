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
public class LatestNewsInstrumentedTests {

    @Rule
    public IntentsTestRule<LatestNewsActivity> latestNewsIntentsTestRule = new IntentsTestRule<LatestNewsActivity>(LatestNewsActivity.class);

    @Test
    public void latestNewsPreRequisites(){
        onView(withId(R.id.uniTweetsBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.rusuTweetsBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.tweetsListView)).check(matches(isDisplayed()));
    }

    @Test
    public void testUniTweetsButtonIsClickable(){
        onView(withId(R.id.uniTweetsBtn)).check(matches(isClickable()));
    }

    @Test
    public void testUniTweetsButtonHasCorrectText(){
        onView(allOf(withId(R.id.uniTweetsBtn), withText(R.string.uorTweetsBtnText)));
    }

    @Test
    public void testRusuTweetsButtonIsClickable(){
        onView(withId(R.id.rusuTweetsBtn)).check(matches(isClickable()));
    }

    @Test
    public void testRusuTweetsButtonHasCorrectText(){
        onView(allOf(withId(R.id.rusuTweetsBtn), withText(R.string.rusuTweetsBtnText)));
    }
}
