package com.example.andrewwilloughby.campus_assistant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DataParserUnitTests {

    private DataParser testParser = new DataParser();

    @Test
    public void testDataParser_ValidParse(){

    }

    @Test
    public void testDataParser_EmptyString(){
        System.out.println("start");
        List<HashMap<String, String>> expected = null;
        List<HashMap<String, String>> actual = testParser.parse("");

        assertThat(actual, is(expected));
    }
}
