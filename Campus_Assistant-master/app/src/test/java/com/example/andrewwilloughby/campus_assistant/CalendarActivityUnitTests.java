package com.example.andrewwilloughby.campus_assistant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CalendarActivityUnitTests {

    CalendarActivity calendarActivity = new CalendarActivity();

    @Test
    public void testGetBuildingAddress_ValidInput() {
        String expected = "Systems Engineering Building, Reading RG6 6AX";
        String actual = calendarActivity.getBuildingAddress("SYSENG");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_NonExistentBuilding(){
        String actual = calendarActivity.getBuildingAddress("xxxxxx");
        assertThat(actual, is(nullValue()));
    }
}
