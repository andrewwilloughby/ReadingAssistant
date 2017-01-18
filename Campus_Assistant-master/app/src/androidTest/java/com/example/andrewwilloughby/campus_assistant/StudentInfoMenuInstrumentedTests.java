package com.example.andrewwilloughby.campus_assistant;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
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
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by andrewwilloughby on 18/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class StudentInfoMenuInstrumentedTests {

    @Rule
    public IntentsTestRule<StudentInfoMenu> studentInfoMenuIntentsTestRule = new IntentsTestRule<StudentInfoMenu>(StudentInfoMenu.class);

    @Test
    public void studentInfoMenuPreRequisites(){
        onView(withId(R.id.viewStyleBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.safetyBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.studentInfoServicesBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.studentInfoLibraryBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.studentInfoPaymentsBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.studentInfoRisisBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.studentInfoStaffDirectoryBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testViewStyleButtonIsClickable(){
        onView(withId(R.id.viewStyleBtn)).check(matches(isClickable()));
    }

    @Test
    public void testViewStyleButtonFiresCorrectIntent(){
        onView(withId(R.id.viewStyleBtn)).perform(scrollTo(), click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.MainActivity"));
    }

    @Test
    public void testSafetyButtonIsClickable(){
        onView(withId(R.id.safetyBtn)).check(matches(isClickable()));
    }

    @Test
    public void testSafetyButtonClickFiresCorrectIntent(){
        onView(withId(R.id.safetyBtn)).perform(scrollTo(), click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.SafetyInfo"));
    }

    @Test
    public void testStudentServicesButtonIsClickable(){
        onView(withId(R.id.studentInfoServicesBtn)).check(matches(isClickable()));
    }

    @Test
    public void testStudentServicesButtonClickFiresCorrectIntent(){
        onView(withId(R.id.studentInfoServicesBtn)).perform(scrollTo(), click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("https://student.reading.ac.uk/")),
                hasExtra(equalTo("webpageName"), equalTo("Student Services")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));
    }

    @Test
    public void testLibraryButtonIsClickable(){
        onView(withId(R.id.studentInfoLibraryBtn)).check(matches(isClickable()));
    }

    @Test
    public void testLibraryButtonClickFiresCorrectIntent(){
        onView(withId(R.id.studentInfoLibraryBtn)).perform(scrollTo(), click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("http://www.reading.ac.uk/library/")),
                hasExtra(equalTo("webpageName"), equalTo("Library")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));
    }

    @Test
    public void testPaymentsButtonIsClickable(){
        onView(withId(R.id.studentInfoPaymentsBtn)).check(matches(isClickable()));
    }

    @Test
    public void testPaymentsButtonClickFiresCorrectIntent(){
        onView(withId(R.id.studentInfoPaymentsBtn)).perform(scrollTo(), click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("https://www.webpay.reading.ac.uk/studentpayments/")),
                hasExtra(equalTo("webpageName"), equalTo("University Payments")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));
    }

    @Test
    public void testRisisButtonIsClickable(){
        onView(withId(R.id.studentInfoRisisBtn)).check(matches(isClickable()));
    }

    @Test
    public void testRisisButtonClickFiresCorrectIntent(){
        onView(withId(R.id.studentInfoRisisBtn)).perform(scrollTo(), click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("https://www.risisweb.reading.ac.uk/")),
                hasExtra(equalTo("webpageName"), equalTo("RISIS")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));
    }

    @Test
    public void testStaffSearchButtonIsClickable(){
        onView(withId(R.id.studentInfoStaffDirectoryBtn)).check(matches(isClickable()));

    }

    @Test
    public void testStaffSearchButtonClickFiresCorrectIntent(){
        onView(withId(R.id.studentInfoStaffDirectoryBtn)).perform(scrollTo(), click());

        intended(allOf(
                hasExtra(equalTo("webpageURL"), equalTo("https://www.reading.ac.uk/search/search-staff.aspx")),
                hasExtra(equalTo("webpageName"), equalTo("Staff Search")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));
    }
}
