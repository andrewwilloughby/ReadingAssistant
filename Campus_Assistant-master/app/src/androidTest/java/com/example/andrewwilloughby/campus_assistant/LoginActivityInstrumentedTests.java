package com.example.andrewwilloughby.campus_assistant;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by andrewwilloughby on 19/01/2017.
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityInstrumentedTests {

    @Rule
    public IntentsTestRule<LoginActivity> loginActivityIntentsTestRule = new IntentsTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void loginActivityPreRequisites(){
        onView(withId(R.id.loginTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.loginTextView2)).check(matches(isDisplayed()));
        onView(withId(R.id.usernameTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.usernameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginTextViewHasCorrectText(){
        onView(allOf(withId(R.id.loginTextView), withText(R.string.loginTextViewText)));
    }

    @Test
    public void testLoginTextView2HasCorrectText(){
        onView(allOf(withId(R.id.loginTextView2), withText(R.string.loginTextView2Text)));
    }

    @Test
    public void testUsernameTextViewHasCorrectText(){
        onView(allOf(withId(R.id.usernameTextView), withText(R.string.uniUsernameTextView)));
    }

    @Test
    public void testPasswordTextViewHasCorrectText(){
        onView(allOf(withId(R.id.passwordTextView), withText(R.string.uniPasswordTextView)));
    }

    @Test
    public void testUsernameEditTextHasCorrectHintText(){
        onView(allOf(withId(R.id.usernameEditText), withHint(R.string.loginUsernameEditTextHint)));
    }

    @Test
    public void testPasswordEditTextHasCorrectHintText(){
        onView(allOf(withId(R.id.passwordEditText), withHint(R.string.loginPasswordEditTextHint)));
    }

    @Test
    public void testLoginButtonIsClickable(){
        onView(withId(R.id.loginButton)).check(matches(isClickable()));
    }

    @Test
    public void testLoginButtonHasCorrectText(){
        onView(allOf(withId(R.id.loginButton), withText(R.string.loginBtnText)));
    }

    @Test
    public void testLoginButtonClickFiresCorrectIntent(){

        /*
        Dummy data which will meet the format requirements, allowing intent
        to be issued on LoginButton click()
         */
        onView(withId(R.id.usernameEditText)).perform(typeText("ab123456"));
        closeSoftKeyboard();

        onView(withId(R.id.passwordEditText)).perform(typeText("xxxxxx"));
        closeSoftKeyboard();

        onView(withId(R.id.loginButton)).perform(click());
        intended(hasComponent("com.example.andrewwilloughby.campus_assistant.CalendarActivity"));
    }
}
