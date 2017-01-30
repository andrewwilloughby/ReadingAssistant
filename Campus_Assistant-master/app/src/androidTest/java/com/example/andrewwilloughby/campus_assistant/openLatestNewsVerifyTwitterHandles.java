package com.example.andrewwilloughby.campus_assistant;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class openLatestNewsVerifyTwitterHandles {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void openLatestNewsVerifyTwitterHandles() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.viewStyleBtn), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.mainLatestNewsBtn), withText("Latest News"),
                        withParent(allOf(withId(R.id.firstRowLayout),
                                withParent(withId(R.id.gridLayout)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.rusuTweetsBtn), withText("RUSU Tweets"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tw__tweet_author_full_name), withText("RUSU"),
                        childAtPosition(
                                allOf(withId(R.id.tw__author_attribution),
                                        childAtPosition(
                                                withId(R.id.tw__tweet_view),
                                                1)),
                                0),
                        isDisplayed(), withText("RUSU")));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.uniTweetsBtn), withText("UoR Tweets"), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.tw__tweet_author_full_name), withText("Uni of Reading"),
                        childAtPosition(
                                allOf(withId(R.id.tw__author_attribution),
                                        childAtPosition(
                                                withId(R.id.tw__tweet_view),
                                                1)),
                                0),
                        isDisplayed(), withText("Uni of Reading")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
