package com.example.andrewwilloughby.campus_assistant;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by andrewwilloughby on 18/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTests {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule = new IntentsTestRule<MainActivity>(MainActivity.class);

    @Test
    public void mainActivityPreRequisites(){
        onView(withId(R.id.viewStyleBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.safetyBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.expandableList)).check(matches(isDisplayed()));

        onView(withId(R.id.viewStyleBtn)).perform(click());

        onView(withId(R.id.mainStudentInfoBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.mainLatestNewsBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.mainCampusNavBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.mainTravelInfoBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.mainBbEmailBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.mainTimetableBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testViewStyleButtonIsClickable(){
        onView(withId(R.id.viewStyleBtn)).check(matches(isClickable()));
    }

    @Test
    public void testViewStyleButtonChangesLayout(){
        onView(withId(R.id.viewStyleBtn)).perform(click());
        onView(withId(R.id.gridLayout)).check(matches(isDisplayed()));

        onView(withId(R.id.viewStyleBtn)).perform(click());
        onView(withId(R.id.expandableList)).check(matches(isDisplayed()));
    }

    @Test
    public void testSafetyButtonIsClickable(){
        onView(withId(R.id.safetyBtn)).check(matches(isClickable()));
    }

    @Test
    public void testSafetyButtonClickFiresCorrectIntent(){
        onView(withId(R.id.safetyBtn)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.SafetyInfoActivity"));
    }

    @Test
    public void testStudentInfoButtonIsClickable(){
        onView(withId(R.id.mainStudentInfoBtn)).check(matches(isClickable()));
    }

    @Test
    public void testStudentInfoButtonHasCorrectText(){
        onView(allOf(withId(R.id.mainStudentInfoBtn), withText(R.string.mainStudentInfoBtnText)));
    }

    @Test
    public void testStudentInfoButtonClickFiresCorrectIntent(){
        try {
            onView(withId(R.id.mainStudentInfoBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.StudentInfoMenuActivity"));
        } catch (PerformException error){
            onView(withId(R.id.viewStyleBtn)).perform(click());
            onView(withId(R.id.mainStudentInfoBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.StudentInfoMenuActivity"));
        }
    }

    @Test
    public void testLatestNewsButtonIsClickable(){
        onView(withId(R.id.mainLatestNewsBtn)).check(matches(isClickable()));
    }

    @Test
    public void testLatestNewsButtonHasCorrectText(){
        onView(allOf(withId(R.id.mainLatestNewsBtn), withText(R.string.mainLatestNewsBtnText)));
    }

    @Test
    public void testLatestNewsButtonClickFiresCorrectIntent(){
        try {
            onView(withId(R.id.mainLatestNewsBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.LatestNewsActivity"));
        } catch (PerformException e){
            onView(withId(R.id.viewStyleBtn)).perform(click());
            onView(withId(R.id.mainLatestNewsBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.LatestNewsActivity"));
        }
    }

    @Test
    public void testCampusNavigationButtonIsClickable(){
        onView(withId(R.id.mainCampusNavBtn)).check(matches(isClickable()));
    }

    @Test
    public void testCampusNavigationButtonHasCorrectText(){
        onView(allOf(withId(R.id.mainCampusNavBtn), withText(R.string.mainCampusNavBtnText)));
    }

    @Test
    public void testCampusNavigationButtonClickFiresCorrectIntent(){
        try {
            onView(withId(R.id.mainCampusNavBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.CampusNavigationMenuActivity"));
        } catch (PerformException e){
            onView(withId(R.id.viewStyleBtn)).perform(click());
            onView(withId(R.id.mainCampusNavBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.CampusNavigationMenuActivity"));
        }
    }

    @Test
    public void testTravelInformationButtonIsClickable(){
        onView(withId(R.id.mainTravelInfoBtn)).check(matches(isClickable()));
    }

    @Test
    public void testTravelInformationButtonHasCorrectText(){
        onView(allOf(withId(R.id.mainTravelInfoBtn), withText(R.string.mainTravelInfoBtnText)));
    }

    @Test
    public void testTravelInformationButtonClickFiresCorrectIntent(){
        try {
            onView(withId(R.id.mainTravelInfoBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.TravelInformationMenuActivity"));
        } catch (PerformException e){
            onView(withId(R.id.viewStyleBtn)).perform(click());
            onView(withId(R.id.mainTravelInfoBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.TravelInformationMenuActivity"));
        }
    }

    @Test
    public void testBlackboardEmailButtonIsClickable(){
        onView(withId(R.id.mainBbEmailBtn)).check(matches(isClickable()));
    }

    @Test
    public void testBlackboardEmailButtonHasCorrectText(){
        onView(allOf(withId(R.id.mainBbEmailBtn), withText(R.string.mainBbEmailBtnText)));
    }

    @Test
    public void testBlackboardEmailButtonClickFiresCorrectIntent(){
        try {
            onView(withId(R.id.mainBbEmailBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.BBEmailMenuActivity"));
        } catch (PerformException e){
            onView(withId(R.id.viewStyleBtn)).perform(click());
            onView(withId(R.id.mainBbEmailBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.BBEmailMenuActivity"));
        }
    }

    @Test
    public void testTimetableButtonIsClickable(){
        onView(withId(R.id.mainTimetableBtn)).check(matches(isClickable()));
    }

    @Test
    public void testTimetableButtonHasCorrectText(){
        onView(allOf(withId(R.id.mainTimetableBtn), withText(R.string.mainTimetableBtnText)));
    }

    @Test
    public void testTimetableButtonClickFiresCorrectIntent(){
        try {
            onView(withId(R.id.mainTimetableBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.LoginActivity"));
        } catch (PerformException e){
            onView(withId(R.id.viewStyleBtn)).perform(click());
            onView(withId(R.id.mainTimetableBtn)).perform(click());
            intended(hasComponent("com.example.andrewwilloughby.campus_assistant.LoginActivity"));
        }
    }
}
