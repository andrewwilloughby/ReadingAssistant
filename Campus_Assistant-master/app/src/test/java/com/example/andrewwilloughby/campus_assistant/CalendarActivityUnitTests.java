package com.example.andrewwilloughby.campus_assistant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CalendarActivityUnitTests {

    private CalendarActivity calendarActivity = new CalendarActivity();

    @Test
    public void testGetBuildingAddress_Agr() {
        String expected = "Agriculture building, Reading RG6 6BZ";
        String actual = calendarActivity.getBuildingAddress("AGR");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Arch() {
        String expected = "Archaeology Building, Reading RG6 6AX";
        String actual = calendarActivity.getBuildingAddress("ARCH");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_All() {
        String expected = "The Allen Laboratory, Earley, Reading RG6 6AX";
        String actual = calendarActivity.getBuildingAddress("ALL");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Bus() {
        String expected = "Centre for Entrepreneurship, Henley Business School, Reading RG6 6UD";
        String actual = calendarActivity.getBuildingAddress("BUS");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Car() {
        String expected = "Carrington Building, Reading RG6 6UA";
        String actual = calendarActivity.getBuildingAddress("CAR");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Cha() {
        String expected = "51.440210, -0.949907";
        String actual = calendarActivity.getBuildingAddress("CHA");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Che() {
        String expected = "Chemistry and Pharmacy Building, Reading RG6 6LA";
        String actual = calendarActivity.getBuildingAddress("CHE");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Whi() {
        String expected = "Whiteknights House, Reading RG6";
        String actual = calendarActivity.getBuildingAddress("WHI");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Eng() {
        String expected = "Engineering Building, Pepper Lane, Earley";
        String actual = calendarActivity.getBuildingAddress("ENG");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Foo() {
        String expected = "Food Biosciences Building, Reading RG6 6LA";
        String actual = calendarActivity.getBuildingAddress("FOO");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Fox() {
        String expected = "Foxhill House, Reading RG6 7BY Reading RG6 6LA";
        String actual = calendarActivity.getBuildingAddress("FOX");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Har() {
        String expected = "Harbone Building, Reading, RG6 6LA";
        String actual = calendarActivity.getBuildingAddress("Har");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Hop() {
        String expected = "Hopkins Building, Reading RG6 6LA";
        String actual = calendarActivity.getBuildingAddress("HOP");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Hum() {
        String expected = "Humss Building, Earley";
        String actual = calendarActivity.getBuildingAddress("HUM");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_JJT() {
        String expected = "JJ Thomson Building, Reading RG6 6AX";
        String actual = calendarActivity.getBuildingAddress("JJT");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Kni() {
        String expected = "Knight Building, Reading RG6 6UA";
        String actual = calendarActivity.getBuildingAddress("KNI");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Lyl() {
        String expected = "Philip Lyle Building, Reading RG6 6LA";
        String actual = calendarActivity.getBuildingAddress("LYL");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Mat() {
        String expected = "Mathematics and IT Services, Reading RG6 6AX";
        String actual = calendarActivity.getBuildingAddress("MAT");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Met() {
        String expected = "Meteorology building, Reading RG6 6BB";
        String actual = calendarActivity.getBuildingAddress("MET");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Mil() {
        String expected = "Miller Building, Reading RG6";
        String actual = calendarActivity.getBuildingAddress("MIL");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Pal() {
        String expected = "Palmer Building, Reading, Wokingham RG6 6UA, England";
        String actual = calendarActivity.getBuildingAddress("PAL");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_Sys() {
        String expected = "Systems Engineering Building, Reading RG6 6AX";
        String actual = calendarActivity.getBuildingAddress("SYSENG");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_TOB2() {
        String expected = "TOB2, Reading RG6 6BZ";
        String actual = calendarActivity.getBuildingAddress("TPY");
        assertThat(actual, is(expected));
    }

    @Test
    public void testGetBuildingAddress_NonExistentBuilding(){
        String actual = calendarActivity.getBuildingAddress("xxxxxx");
        assertThat(actual, is(nullValue()));
    }
}
