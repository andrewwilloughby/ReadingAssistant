package com.example.andrewwilloughby.campus_assistant;

/**
 * Created by andrewwilloughby on 16/01/2017.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityUnitTests {

    LoginActivity loginActivity = new LoginActivity();

    @Test
    public void testUsernameValidation_ValidUsername() {
        String expected = "valid";
        String actual = loginActivity.validateUserNameEditText("bc016938");
        assertThat(actual, is(expected));
    }

    @Test
    public void testUsernameValidation_IncorrectLength(){
        String expected = "invalid length";
        String actual = loginActivity.validateUserNameEditText("bc016938000");
        assertThat(actual, is(expected));
    }

    @Test
    public void testUsernameValidation_IncorrectFormat(){
        String expected = "invalid format";
        String actual = loginActivity.validateUserNameEditText("00016938");
        assertThat(actual, is(expected));
    }

    @Test
    public void testUsernameValidation_ContainsSpecialCharacters(){
        String expected = "special characters";
        String actual = loginActivity.validateUserNameEditText("$$016938");
        assertThat(actual, is(expected));
    }
}
