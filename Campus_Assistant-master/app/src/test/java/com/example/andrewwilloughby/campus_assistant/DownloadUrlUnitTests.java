package com.example.andrewwilloughby.campus_assistant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DownloadUrlUnitTests {

    private DownloadUrl downloadUrl = new DownloadUrl();

    @Test
    public void testUrlDownload_emptyPage() {
        String expected = null;
        String actual = downloadUrl.readUrl("file:///Users/andrewwilloughby/Documents/FYP%20Files/empty_page.txt");

        assertThat(actual, is(expected));
    }

    @Test
    public void testUrlDownload_PageWithText(){
        String expected = "line1line2line3line4line5";
        String actual = downloadUrl.readUrl("http://www-eng-x.llnl.gov/documents/tests/pc.txt");

        assertThat(actual, is(expected));
    }
}
