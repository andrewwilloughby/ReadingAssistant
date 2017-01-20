package com.example.andrewwilloughby.campus_assistant;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by andrewwilloughby on 20/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class SafetyInfoInstrumentedTests {

    private static final String VALID_PHONE_NUMBER = "07590917581";
    private static final Uri INTENT_DATA_PHONE_NUMBER = Uri.parse("tel:" + VALID_PHONE_NUMBER);

    @Rule
    public IntentsTestRule<SafetyInfo> safetyInfoIntentsTestRule = new IntentsTestRule<SafetyInfo>(SafetyInfo.class);

    @Test
    public void safetyInforPreRequisites(){
        onView(withId(R.id.emergencyMessage1)).check(matches(isDisplayed()));
        onView(withId(R.id.emergencyMessage2)).check(matches(isDisplayed()));
        onView(withId(R.id.securityEmergencyBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.securityNonEmergencyBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.personalSafetyBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.healthAndSafetyBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmergencyMessage1HasCorrectText(){
        onView(allOf(withId(R.id.emergencyMessage1), withText(R.string.safetyMessage1)));
    }

    @Test
    public void testEmergencyMessage2HasCorrectText(){
        onView(allOf(withId(R.id.emergencyMessage2), withText(R.string.safetyMessage2)));
    }

    @Test
    public void testSecurityEmergencyButtonHasCorrectText(){
        onView(allOf(withId(R.id.securityEmergencyBtn), withText(R.string.securityEmergencyBtnText)));
    }

    @Test
    public void testSecurityEmergencyButtonIsClickable(){
        onView(withId(R.id.securityEmergencyBtn)).check(matches(isClickable()));
    }

    @Before
    public void stubAllExternalIntents() {
        intending(hasAction(Intent.ACTION_DIAL)).respondWith(
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void testSecurityEmergencyButtonFiresCorrectIntent(){
        onView(withId(R.id.securityEmergencyBtn)).perform(ViewActions.click());
        intended(allOf(hasAction(Intent.ACTION_DIAL), hasData(INTENT_DATA_PHONE_NUMBER)));
    }

    @Test
    public void testSecurityNonEmergencyButtonHasCorrectText(){
        onView(allOf(withId(R.id.securityNonEmergencyBtn), withText(R.string.securityNonEmergencyBtnText)));
    }

    @Test
    public void testSecurityNonEmergencyButtonIsClickable(){
        onView(withId(R.id.securityNonEmergencyBtn)).check(matches(isClickable()));
    }

    @Test
    public void testSecurityNonEmergencyButtonFiresCorrectIntent(){
        onView(withId(R.id.securityNonEmergencyBtn)).perform(ViewActions.click());
        intended(allOf(hasAction(Intent.ACTION_DIAL), hasData(INTENT_DATA_PHONE_NUMBER)));
    }

    @Test
    public void testPersonalSafetyButtonHasCorrectText(){
        onView(allOf(withId(R.id.personalSafetyBtn), withText(R.string.personalSafetyBtnText)));
    }

    @Test
    public void testPersonalSafetyButtonIsClickable(){
        onView(withId(R.id.personalSafetyBtn)).check(matches(isClickable()));
    }

    @Test
    public void testPersonalSafetyButtonClickFiresCorrectIntent(){
        onView(withId(R.id.personalSafetyBtn)).perform(ViewActions.click());

        intended(Matchers.allOf(
                hasExtra(equalTo("webpageURL"), equalTo("http://www.reading.ac.uk/web/FILES/security/secStayingSafe.pdf")),
                hasExtra(equalTo("webpageName"), equalTo("Personal Safety Guide")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));
    }

    @Test
    public void testHealthSafetyButtonHasCorrectText(){
        onView(allOf(withId(R.id.healthAndSafetyBtn), withText(R.string.healthAndSafetyBtnText)));
    }

    @Test
    public void testHealthSafetyButtonIsClickable(){
        onView(withId(R.id.healthAndSafetyBtn)).check(matches(isClickable()));
    }

    @Test
    public void testHealthSafetyButtonClickFiresCorrectIntent(){
        onView(withId(R.id.healthAndSafetyBtn)).perform(ViewActions.click());

        intended(Matchers.allOf(
                hasExtra(equalTo("webpageURL"), equalTo("http://www.reading.ac.uk/internal/health-and-safety/IncidentReportingandEmergencyProcedures/Report_an_Incident_online.aspx")),
                hasExtra(equalTo("webpageName"), equalTo("Report Health & Safety Incident")),
                hasComponent("com.example.andrewwilloughby.campus_assistant.WebpageView")));
    }

}
