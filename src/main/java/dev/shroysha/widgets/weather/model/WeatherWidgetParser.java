package dev.shroysha.widgets.weather.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WeatherWidgetParser {

    private final URL url;
    private Document doc;
    private CurrentForecast current;
    private FutureForecast[] forecasts;

    public WeatherWidgetParser(URL url) {
        super();
        this.url = url;
        reload();
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.println("ONE X");
        URL url = new URL(" http://weather.yahooapis.com/forecastrss?w=12766700");
        WeatherWidgetParser p = new WeatherWidgetParser(url);
        System.out.println(p);
    }

    private static String getValue(Element element) {
        NodeList nodes = element.getElementsByTagName("title").item(0).getChildNodes();
        Node node = nodes.item(0);
        return node.getNodeValue();
    }

    public void reload() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(url.openStream());
            parse();
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            Logger.getLogger(WeatherWidgetParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parse() {
        current = parseCurrent();
        forecasts = parseForecasts();
    }

    private CurrentForecast parseCurrent() {


        NodeList itemNode = doc.getElementsByTagName("item");
        Element itemElement = (Element) itemNode.item(0);
        String title = getValue(itemElement);
        Element currentEle = (Element) itemElement.getElementsByTagName("yweather:condition").item(0);
        String condition = currentEle.getAttribute("text");
        int temp = Integer.parseInt(currentEle.getAttribute("temp"));


        /*
        if(imageLoc.getNodeType() == Node.ELEMENT_NODE) {
            System.out.println(imageLoc);
        } else {
            System.out.println(imageLoc.getNodeName());
        }*/

        return new CurrentForecast(title, condition, temp);
    }

    private FutureForecast[] parseForecasts() {
        NodeList itemNode = doc.getElementsByTagName("item");
        Element itemElement = (Element) itemNode.item(0);
        NodeList futureList = itemElement.getElementsByTagName("yweather:forecast");
        FutureForecast[] ff = new FutureForecast[futureList.getLength()];

        for (int i = 0; i < ff.length; i++) {
            Element element = (Element) futureList.item(i);
            String date = element.getAttribute("date"); //date
            String condition = element.getAttribute("text"); //text
            int low = Integer.parseInt(element.getAttribute("low")); //low
            int high = Integer.parseInt(element.getAttribute("high")); //high
            ff[i] = new FutureForecast(date, condition, low, high);
        }

        return ff;
    }

    public String toString() {
        StringBuilder text = new StringBuilder(current.toString() + "\n");
        for (FutureForecast f : forecasts) {
            text.append(f.toString()).append("\n");
        }

        return text.toString();
    }

    public static class CurrentForecast {

        private final String title;
        private final String condition;
        private final int temperature;

        public CurrentForecast(String title, String condition, int temperature) {
            super();
            this.title = title;
            this.condition = condition;
            this.temperature = temperature;
        }

        public String getCondition() {
            return condition;
        }

        public int getTemperature() {
            return temperature;
        }

        public String getTitle() {
            return title;
        }


        public String toString() {
            return "CurrentForecast{" + "title=" + title + ", condition=" + condition + ", temperature=" + temperature + '}';
        }


    }

    public static class FutureForecast {

        private final String date;
        private final String condition;
        private final int low;
        private final int high;

        public FutureForecast(String date, String condition, int low, int high) {
            super();
            this.date = date;
            this.condition = condition;
            this.low = low;
            this.high = high;
        }


        public String toString() {
            return "FutureForecast{" + "date=" + date + ", condition=" + condition + ", low=" + low + ", high=" + high + '}';
        }


    }
}
