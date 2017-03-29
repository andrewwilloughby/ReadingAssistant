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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityInstrumentedTest() {
        onView(withId(R.id.expandableList)).check(matches(isDisplayed()));
        onView(withId(R.id.viewStyleBtn)).check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.safetyBtn),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                0),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.safetyBtn),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                0),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.viewStyleBtn), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.mainStudentInfoBtn),
                        childAtPosition(
                                allOf(withId(R.id.firstRowLayout),
                                        childAtPosition(
                                                withId(R.id.gridLayout),
                                                0)),
                                0),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.mainLatestNewsBtn),
                        childAtPosition(
                                allOf(withId(R.id.firstRowLayout),
                                        childAtPosition(
                                                withId(R.id.gridLayout),
                                                0)),
                                1),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.mainCampusNavBtn),
                        childAtPosition(
                                allOf(withId(R.id.secondRowLayout),
                                        childAtPosition(
                                                withId(R.id.gridLayout),
                                                1)),
                                0),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.mainTravelInfoBtn),
                        childAtPosition(
                                allOf(withId(R.id.secondRowLayout),
                                        childAtPosition(
                                                withId(R.id.gridLayout),
                                                1)),
                                1),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction button5 = onView(
                allOf(withId(R.id.mainBbEmailBtn),
                        childAtPosition(
                                allOf(withId(R.id.thirdRowLayout),
                                        childAtPosition(
                                                withId(R.id.gridLayout),
                                                2)),
                                0),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));

        ViewInteraction button6 = onView(
                allOf(withId(R.id.mainTimetableBtn),
                        childAtPosition(
                                allOf(withId(R.id.thirdRowLayout),
                                        childAtPosition(
                                                withId(R.id.gridLayout),
                                                2)),
                                1),
                        isDisplayed()));
        button6.check(matches(isDisplayed()));

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
