package com.example.andrewwilloughby.campus_assistant;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
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

@RunWith(AndroidJUnit4.class)
public class openLiveBusTimesVerifyUIComponents {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void openLiveBusTimesVerifyUIComponents() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.viewStyleBtn), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.mainTravelInfoBtn), withText("Travel Information"),
                        withParent(allOf(withId(R.id.secondRowLayout),
                                withParent(withId(R.id.gridLayout)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.travelInfoBusTimesBtn), withText("Live Bus Times"),
                        withParent(allOf(withId(R.id.firstRowLayout),
                                withParent(withId(R.id.activityContainer)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        onView(
                allOf(withText("Live Bus Departures"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));

        onView(
                allOf(withId(R.id.chancellorWayBusBtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_bus_times),
                                        0),
                                0),
                        isDisplayed()));

        onView(
                allOf(withId(R.id.whiteknightsHouseBusBtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_bus_times),
                                        0),
                                2),
                        isDisplayed()));

        onView(
                allOf(withId(R.id.readingStationBusBtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_bus_times),
                                        0),
                                4),
                        isDisplayed()));

        onView(
                allOf(withId(R.id.routeTxtView), withText("Route"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_bus_times),
                                        2),
                                0),
                        isDisplayed()));

        onView(
                allOf(withId(R.id.destinationTxtView), withText("Destination"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_bus_times),
                                        2),
                                1),
                        isDisplayed()));

        onView(
                allOf(withId(R.id.arrivalTxtView), withText("Time"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_bus_times),
                                        2),
                                2),
                        isDisplayed()));

        onView(
                allOf(withId(R.id.busHeaderTextView), withText("Pull to refresh"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.activity_bus_times),
                                        1),
                                0),
                        isDisplayed()));

        onView(
                allOf(withId(R.id.busList),
                        childAtPosition(
                                allOf(withId(R.id.bus_swipe_layout),
                                        childAtPosition(
                                                withId(R.id.activity_bus_times),
                                                3)),
                                0),
                        isDisplayed()));

        onView(
                allOf(withId(R.id.busList),
                        childAtPosition(
                                allOf(withId(R.id.bus_swipe_layout),
                                        childAtPosition(
                                                withId(R.id.activity_bus_times),
                                                3)),
                                0),
                        isDisplayed()));
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
