package com.example.andrewwilloughby.campus_assistant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by andrewwilloughby on 20/01/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class DownloadUrlUnitTests {

    private DownloadUrl downloadUrl = new DownloadUrl();

    @Test
    public void testUrlDownload_emptyPage() {
        String expected = null;
        String actual = "1234";

        try{
            actual = downloadUrl.readUrl("file:///Users/andrewwilloughby/Documents/FYP%20Files/empty_page.txt");
        } catch (IOException e) {
            actual = "test failed";
        }

        System.out.println(actual);
        assertThat(actual, is(expected));
    }

    @Test
    public void testUrlDownload_PageWithText(){
        String expected = "line1line2line3line4line5";
        String actual;

        try{
            actual = downloadUrl.readUrl("http://www-eng-x.llnl.gov/documents/tests/pc.txt");
        } catch (IOException e) {
            actual = "test failed";
        }

        System.out.println(actual);
        assertThat(actual, is(expected));
    }
}
