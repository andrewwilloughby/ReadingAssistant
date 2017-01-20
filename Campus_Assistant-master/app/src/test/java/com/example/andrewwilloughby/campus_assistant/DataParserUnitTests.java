package com.example.andrewwilloughby.campus_assistant;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by andrewwilloughby on 20/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class DataParserUnitTests {

    private DataParser testParser = new DataParser();

    @Test
    public void testUsernameValidation_ValidUsername() {
        List<HashMap<String, String>> expected;
        List<HashMap<String, String>> actual = testParser.parse("");
    }
}
