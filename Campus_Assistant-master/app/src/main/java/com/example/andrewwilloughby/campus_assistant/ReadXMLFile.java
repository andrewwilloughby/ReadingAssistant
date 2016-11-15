package com.example.andrewwilloughby.campus_assistant;

/**
 * Created by andrewwilloughby on 15/11/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.net.URL;

//This code from https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/ and http://stackoverflow.com/questions/12065951/how-can-i-parse-xml-from-url-in-android

public class ReadXMLFile extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_times);

        /** Create a new layout to display the view */
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        /** Create a new textview array to display the results */
        TextView route[];
        TextView destination[];
        TextView displaytime[];

        try {
            URL url = new URL("http://opendata.reading-travelinfo.co.uk/api/1/bus/calls/rdgagta.xml?key=d604e8f1-6e22-4fff-8b6c-24d082dfae50");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("call");

            /* Assign textview array lenght by arraylist size */

            route = new TextView[nodeList.getLength()];
            destination = new TextView[nodeList.getLength()];
            displaytime = new TextView[nodeList.getLength()];

            for (int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);

                route[i] = new TextView(this);
                destination[i] = new TextView(this);
                displaytime[i] = new TextView(this);

                Element firstElement = (Element) node;

                NodeList routeList = firstElement.getElementsByTagName("PublishedServiceName");
                Element routeElement = (Element) routeList.item(0);
                routeList = routeElement.getChildNodes();
                route[i].setText("Route = " + (routeList.item(0)).getNodeValue());

                NodeList destinationList = firstElement.getElementsByTagName("Destination");
                Element destinationElement = (Element) destinationList.item(0);
                destinationList = destinationElement.getChildNodes();
                destination[i].setText("Destination = " + (destinationList.item(0)).getNodeValue());

                NodeList displayTimeList = firstElement.getElementsByTagName("DisplayTime");
                Element displayTimeElement = (Element) displayTimeList.item(0);
                displayTimeList = displayTimeElement.getChildNodes();
                displaytime[i].setText("Destination = " + (displayTimeList.item(0)).getNodeValue());

                layout.addView(route[i]);
                layout.addView(destination[i]);
                layout.addView(displaytime[i]);
            }

        } catch (Exception e){
            System.out.println("XML Parsing Exception = " + e);
        }
    }

    private static String busAPIKey = "d604e8f1-6e22-4fff-8b6c-24d082dfae50";

    public ReadXMLFile() {
    }

    public static void read(String url){
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(new URL(url + busAPIKey).openStream());


            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("Call");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println("Route: " + eElement.getElementsByTagName("PublichedServiceName").item(0).getTextContent());
                    System.out.println("Destination: " + eElement.getElementsByTagName("Destination").item(0).getTextContent());
                    System.out.println("Display Time: " + eElement.getElementsByTagName("DisplayTime").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
