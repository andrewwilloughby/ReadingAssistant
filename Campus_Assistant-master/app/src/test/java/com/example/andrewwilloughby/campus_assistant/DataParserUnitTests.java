package com.example.andrewwilloughby.campus_assistant;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DataParserUnitTests {

    private DataParser testParser = new DataParser();
    private DownloadUrl downloadUrl = new DownloadUrl();
    private String data;

    @Before
    public void getData(){
        data = downloadUrl.readUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=51.5978381,-1.8075576&radius=1000&type=cafe&sensor=true&key=AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
    }

    @Test
    public void testDataParser_Valid(){
        List<HashMap<String, String>> actual = testParser.parse(data);
        assertThat(actual, not(nullValue()));
    }

    @Test
    public void testDataParser_EmptyString(){
        List<HashMap<String, String>> actual = testParser.parse("");
        assertThat(actual, nullValue());
    }

    @Test
    public void testDataParser_Null(){
        List<HashMap<String, String>> actual = testParser.parse(null);
        assertThat(actual, nullValue());
    }

    @Test
    public void testGetPlaces_Valid(){
        String places = "[{\"geometry\":{\"location\":{\"lat\":51.59733199999999,\"lng\":-1.8049879},\"viewport\":{\"northeast\":{\"lat\":51.59868098029149,\"lng\":-1.803638919708498},\"southwest\":{\"lat\":51.59598301970849,\"lng\":-1.806336880291502}}},\"icon\":\"https:\\/\\/maps.gstatic.com\\/mapfiles\\/place_api\\/icons\\/shopping-71.png\",\"id\":\"11c08f2fca76ece3a07174e543ef87f589bdaa94\",\"name\":\"Asda Swindon Haydon Supercentre\",\"opening_hours\":{\"open_now\":true,\"weekday_text\":[]},\"photos\":[{\"height\":2988,\"html_attributions\":[\"<a href=\\\"https:\\/\\/maps.google.com\\/maps\\/contrib\\/114220661892375965637\\/photos\\\">Sven Winkler<\\/a>\"],\"photo_reference\":\"CoQBdwAAAA9koZPZLYZPOKNi0CINUhiYApWfkDTXCIz_hO-aGKGaw2O85EIiG8xiR7Rmhj7PLpc9sJD3FleGM7Pbggs_C3wfJLlBeMcNbwskrpctNXgZrl69UraQfi_GionEAAbEfpyU4WVZWqEeyJQuXCmt9Ao-0DKmNTn9-if1H24cQoqtEhB0mTlV2tgCw2skegHUrJd9GhRSDFclF5WO8IPdIGY9kvmEv4TjOg\",\"width\":5312}],\"place_id\":\"ChIJofR5FbtGcUgRckyispa7Qks\",\"price_level\":1,\"rating\":3.4,\"reference\":\"CmRRAAAA_3zFQg3kEJioAezRp8hbY6MN6OzMPpN2yR_KrhfTMPxKDnC47qIhMblh71HtC0kl1UJ7ASriO65OSpQ2mh9hr6PICARFzojHawO0jgdoI21dJkLGar0hjiSXtruVcZzhEhBjQb8jIqWl3jiGcgcI8jGUGhTJuoKNSdSszqPPegaImiy38aawHQ\",\"scope\":\"GOOGLE\",\"types\":[\"gas_station\",\"atm\",\"pharmacy\",\"cafe\",\"grocery_or_supermarket\",\"health\",\"food\",\"finance\",\"clothing_store\",\"store\",\"point_of_interest\",\"establishment\"],\"vicinity\":\"The Orbital Shopping Centre, Thamesdown Drive, Swindon\"},{\"geometry\":{\"location\":{\"lat\":51.5973169,\"lng\":-1.807873},\"viewport\":{\"northeast\":{\"lat\":51.59866588029149,\"lng\":-1.806524019708498},\"southwest\":{\"lat\":51.59596791970849,\"lng\":-1.809221980291502}}},\"icon\":\"https:\\/\\/maps.gstatic.com\\/mapfiles\\/place_api\\/icons\\/cafe-71.png\",\"id\":\"fe3ec8d0a428fa1dd3c3efaedfba59f2124e47f1\",\"name\":\"Starbucks\",\"opening_hours\":{\"open_now\":true,\"weekday_text\":[]},\"photos\":[{\"height\":4048,\"html_attributions\":[\"<a href=\\\"https:\\/\\/maps.google.com\\/maps\\/contrib\\/102362681043475450338\\/photos\\\">tom Lee<\\/a>\"],\"photo_reference\":\"CoQBdwAAANGLh4jJ8OOuTRuXhSHMrzJjyqokI93by6OJWvc22DC2jhUpjOMqGfvGHsQTxb0j9ebnioBc06LHrFolFMMKXu3GcZNxUpJCMqzb32G2DOZ2ZgrHyruN4vdxnJjN1x9OS5oUtF5bSd7WQTnO3p1BAeu0aB6zu49adNJ9vDLOhHs1EhDT1_Xmw0klE_GJ8pjsiVmoGhQIgXProkZq4v-RmURLNqxYDNRyag\",\"width\":3036}],\"place_id\":\"ChIJ_RVF7x9EcUgRKGUgw41xDUQ\",\"price_level\":2,\"rating\":4.2,\"reference\":\"CmRRAAAA0ImXwOX-bNZ6WC-cJaVa9FZF7604vK2AEN8llQ2l_WE9dioQKEpRWukb_LVPt3RKuh6owhTsCXxlKTxjaKu57E6aWCkZfHIHxat18POTobcS62r_sJesshS0A9EUZj6YEhCtLBSaLT6B-UxLFg7IVkK-GhRgXf5Pz4RRMIr-czLlRrodovIdFg\",\"scope\":\"GOOGLE\",\"types\":[\"cafe\",\"food\",\"store\",\"point_of_interest\",\"establishment\"],\"vicinity\":\"B1-B2, Orbital Shopping Park, Swindon\"},{\"geometry\":{\"location\":{\"lat\":51.59753509999999,\"lng\":-1.8075304},\"viewport\":{\"northeast\":{\"lat\":51.59888408029149,\"lng\":-1.806181419708498},\"southwest\":{\"lat\":51.59618611970849,\"lng\":-1.808879380291502}}},\"icon\":\"https:\\/\\/maps.gstatic.com\\/mapfiles\\/place_api\\/icons\\/cafe-71.png\",\"id\":\"ddf5f509416b2e72b1f802c195d754068a8f7e89\",\"name\":\"Costa\",\"opening_hours\":{\"open_now\":true,\"weekday_text\":[]},\"photos\":[{\"height\":3036,\"html_attributions\":[\"<a href=\\\"https:\\/\\/maps.google.com\\/maps\\/contrib\\/114499973834625213205\\/photos\\\">Darren Brooks<\\/a>\"],\"photo_reference\":\"CoQBdwAAAG65BSJUJY459PIWfOtR-lDu_lPYt_hSonVAYThiCctEgABNhLx8KWm-OE09e4t1Zth2EIwCTgcG0629Kafdv3H-8FfLKXbW8ouVXfGJuTyc-sWluwkVT_IZqwoafDj2Iy1IskIwNlTCWuDGXZwR0f2f6xoaNZ2y4Cq3D68nh3pHEhAr0BOBVixPnCftqUOAJviiGhSmzg-uWYdYqtUgj7QrsOO8MXbg9w\",\"width\":4048}],\"place_id\":\"ChIJMWNqwrpGcUgRS-_dyBBlfK0\",\"price_level\":2,\"rating\":4.4,\"reference\":\"CmRSAAAAhe2hbmW_1l3O-PQU0v_vPhs86ATTBALhCNOi9q4mw8H6n72n86ghNwEeB29bHFRZJrHQS1qIw2__TCtothwfcAjD-fmW9wy3EGSktDDXiRU5XfbKh6nj46ITMBd73dGyEhB6cP9TZMq2ULJQMxsxNz2fGhRqhrgc-sGpZPabYfjncSIFdmOCXQ\",\"scope\":\"GOOGLE\",\"types\":[\"cafe\",\"food\",\"store\",\"point_of_interest\",\"establishment\"],\"vicinity\":\"North Swindon District Centre, Thamesdown Dr, Swindon\"}]";
        JSONArray placesArray = null;

        try {
             placesArray = new JSONArray(places);
        } catch (JSONException e){/**/}

        List<HashMap<String, String>> actual = testParser.getPlaces(placesArray);
        assertThat(actual, not(nullValue()));
    }
}
