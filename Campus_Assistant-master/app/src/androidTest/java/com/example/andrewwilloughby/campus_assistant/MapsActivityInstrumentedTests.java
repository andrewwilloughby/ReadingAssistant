package com.example.andrewwilloughby.campus_assistant;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MapsActivityInstrumentedTests {

    @Rule
    public IntentsTestRule<InteractiveMapActivity> mapsActivityIntentsTestRule = new IntentsTestRule<InteractiveMapActivity>(InteractiveMapActivity.class);

    @Test
    public void mapPreRequisites(){
        onView(withId(R.id.searchBox)).check(matches(isDisplayed()));
        onView(withId(R.id.searchBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.searchNearbySpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.searchNearbyTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchBoxHasCorrectHintText(){
        onView(CoreMatchers.allOf(withId(R.id.searchBox), withText(R.string.mapSearchHintText)));
    }

    @Test
    public void testSearchButtonIsClickable(){
        onView(withId(R.id.searchBtn)).check(matches(isClickable()));
    }

    @Test
    public void testSearchButtonHasCorrectText(){
        onView(CoreMatchers.allOf(withId(R.id.searchBtn), withText(R.string.mapSearchBtnText)));
    }

    @Test
    public void testShowNearbyTextViewHasCorrectText(){
        onView(CoreMatchers.allOf(withId(R.id.searchNearbyTextView), withText(R.string.search_nearby_textView_text)));
    }

    @Test
    public void testSearchForLondon(){
        ViewInteraction editText = onView(
                allOf(withId(R.id.searchBox), isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.searchBox), isDisplayed()));
        editText2.perform(replaceText("london"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.searchBtn), withText("Search"), isDisplayed()));
        button.perform(click());

        onView(
                allOf(withContentDescription("London. "),
                        childAtPosition(
                                allOf(withContentDescription("Google Map"),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                1)),
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
