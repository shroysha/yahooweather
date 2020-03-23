package dev.shroysha.widgets.weather;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;


public class App {


    public static void main(String[] args) throws Exception {

        URL url = new URL(" http://weather.yahooapis.com/forecastrss?w=12766700");
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(url.openStream());

        /*
        NodeList nodes = doc.getElementsByTagName("yweather:forecast");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                element.getAttribute("day");
                System.out.println("DAY:" + element.getAttribute("day"));
            }
        }*/


        InputStream is = url.openStream();
        Scanner scanner = new Scanner(is);

        String line = "";
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            Thread.sleep(50);
        }
    }

    private static String getValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodes.item(0);
        return node.getNodeValue();
    }


}
