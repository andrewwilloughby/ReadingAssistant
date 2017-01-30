package com.example.andrewwilloughby.campus_assistant;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class openStudentServicesWebPage {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void openStudentServicesWebPage() {
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

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.mainStudentInfoBtn), withText("Student Information"),
                        withParent(allOf(withId(R.id.firstRowLayout),
                                withParent(withId(R.id.gridLayout)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.studentInfoServicesBtn),
                        childAtPosition(
                                allOf(withId(R.id.firstRowLayout),
                                        childAtPosition(
                                                withId(R.id.activityContainer),
                                                2)),
                                0),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.studentInfoServicesBtn), withText("Student Services"),
                        withParent(allOf(withId(R.id.firstRowLayout),
                                withParent(withId(R.id.activityContainer))))));
        appCompatButton2.perform(scrollTo(), click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("https://student.reading.ac.uk/")),
                hasExtra(equalTo("webpageName"), equalTo("Student Services")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));

        ViewInteraction textView = onView(
                allOf(withText("Student Services"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Student Services")));

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
