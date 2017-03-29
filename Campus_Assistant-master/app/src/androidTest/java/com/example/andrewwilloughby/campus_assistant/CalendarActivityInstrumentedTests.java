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
public class CalendarActivityInstrumentedTests {

    @Rule
    public IntentsTestRule<CalendarActivity> calendarActivityIntentsTestRule =
            new IntentsTestRule<>(CalendarActivity.class);

    @Test
    public void calendarActivityPreRequisites(){
        onView(withId(R.id.todayBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.dayEventsListView)).check(matches(isDisplayed()));
        onView(withId(R.id.timetableCalendar)).check(matches(isDisplayed()));
    }

    @Test
    public void testTodayButtonIsClickable(){
        onView(withId(R.id.todayBtn)).check(matches(isClickable()));
    }

    @Test
    public void testTodayButtonHasCorrectText(){
        onView(allOf(withId(R.id.todayBtn), withText(R.string.todayBtnText)));
    }

    @Test
    public void testCalendarViewIsClickable(){
        onView(withId(R.id.timetableCalendar)).check(matches(isClickable()));
    }
}
