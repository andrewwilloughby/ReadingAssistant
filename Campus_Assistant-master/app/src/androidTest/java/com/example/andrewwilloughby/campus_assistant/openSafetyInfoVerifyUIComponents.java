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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class openSafetyInfoVerifyUIComponents {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void openSafetyInfoVerifyUIComponents() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.safetyBtn), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("Safety Information"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Safety Information")));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.headBackgroundImage),
                        childAtPosition(
                                allOf(withId(R.id.headImageContainer),
                                        childAtPosition(
                                                withId(R.id.activityContainer),
                                                0)),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.emergencyMessage1), withText("In an emergency, always dial 999."),
                        childAtPosition(
                                allOf(withId(R.id.activityContainer),
                                        childAtPosition(
                                                withId(R.id.activity_safety_info),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("In an emergency, always dial 999.")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.emergencyMessage2), withText("When safe to do so, contact University of Reading Security."),
                        childAtPosition(
                                allOf(withId(R.id.activityContainer),
                                        childAtPosition(
                                                withId(R.id.activity_safety_info),
                                                0)),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("When safe to do so, contact University of Reading Security.")));

        ViewInteraction button = onView(
                allOf(withId(R.id.securityEmergencyBtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activityContainer),
                                        3),
                                0),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.securityNonEmergencyBtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activityContainer),
                                        3),
                                1),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.personalSafetyBtn),
                        childAtPosition(
                                allOf(withId(R.id.safetyGridLayout),
                                        childAtPosition(
                                                withId(R.id.gridLayout),
                                                0)),
                                0),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.healthAndSafetyBtn),
                        childAtPosition(
                                allOf(withId(R.id.safetyGridLayout),
                                        childAtPosition(
                                                withId(R.id.gridLayout),
                                                0)),
                                1),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction button5 = onView(
                allOf(withId(R.id.healthAndSafetyBtn),
                        childAtPosition(
                                allOf(withId(R.id.safetyGridLayout),
                                        childAtPosition(
                                                withId(R.id.gridLayout),
                                                0)),
                                1),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));

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
