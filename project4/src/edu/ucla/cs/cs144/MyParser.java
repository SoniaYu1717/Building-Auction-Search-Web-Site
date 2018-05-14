package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import java.text.SimpleDateFormat;
import java.text.ParseException;


public class MyParser {

    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }

    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }

    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }

    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }

    static String getAttributeText(Element e, String attr) {
        Node attributeNode = e.getAttributes().getNamedItem(attr);
        if (attributeNode != null) {
            return attributeNode.getNodeValue();
        } else {
            return null;
        }
    }

    static String convertDateFormat(String date) {
        try {
                SimpleDateFormat old_format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
                SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                date = new_format.format(old_format.parse(date));
        }
        catch(ParseException e) {
            System.out.println("Parse error!");
        }
        
        return date;
    }

    static ItemResult parseData(Element currentItem) throws Exception {

        String itemId = getAttributeText(currentItem, "ItemId");
        String name = getElementTextByTagNameNR(currentItem, "Name");
        String currently = getElementTextByTagNameNR(currentItem, "Currently");
        String first_bid = getElementTextByTagNameNR(currentItem, "First_Bid");
        String buy_price = getElementTextByTagNameNR(currentItem, "Buy_Price");
        String num_of_bids = getElementTextByTagNameNR(currentItem, "Number_of_Bids");
        String started = convertDateFormat(getElementTextByTagNameNR(currentItem, "Started"));
        String ends = convertDateFormat(getElementTextByTagNameNR(currentItem, "Ends"));
        String description = getElementTextByTagNameNR(currentItem, "Description");
        if (description.length() > 4000)
            description = description.substring(0, 4000); 

        String location = getElementTextByTagNameNR(currentItem, "Location");
        Element locationElement = getElementByTagNameNR(currentItem, "Location");
        String longitude = locationElement.getAttribute("Longitude");
        String latitude = locationElement.getAttribute("Latitude");
        String item_country = getElementTextByTagNameNR(currentItem, "Country");

        String seller_id = (getElementByTagNameNR(currentItem, "Seller")).getAttribute("UserID");
        String seller_rating = (getElementByTagNameNR(currentItem, "Seller")).getAttribute("Rating");

        Element[] categories = getElementsByTagNameNR(currentItem, "Category");
        ArrayList<String> categoriesList = new ArrayList<String>();
        for (Element category : categories) {
            categoriesList.add(category.getTextContent());
        }
        String[] categoryArray = new String[categoriesList.size()];
        categoryArray = categoriesList.toArray(categoryArray);

        Element bidsRoot = getElementByTagNameNR(currentItem, "Bids");
        Element[] bids = getElementsByTagNameNR(bidsRoot, "Bid");
        ArrayList<Bid> bidList = new ArrayList<Bid>();
        for (Element bid : bids) {
            
            Element bidder = getElementByTagNameNR(bid, "Bidder");
            String bidder_id = bidder.getAttribute("UserID");
            String bidder_rating = bidder.getAttribute("Rating");
            String bidder_location = getElementTextByTagNameNR(bidder, "Location");
            String country = getElementTextByTagNameNR(bidder, "Country");

            String time = convertDateFormat(getElementTextByTagNameNR(bid, "Time"));
            String amount = getElementTextByTagNameNR(bid, "Amount");

            Bid currBid = new Bid(bidder_id, bidder_rating, time, amount);
            currBid.setLocationInfo(bidder_location, country);
            bidList.add(currBid);
        }
        Collections.sort(bidList);
        Bid[] bidArray = new Bid[bidList.size()];
        bidArray = bidList.toArray(bidArray);

        ItemResult itemresult = new ItemResult(itemId, name, currently, first_bid, num_of_bids, started, ends, seller_id, seller_rating, description);
        itemresult.setBuyPrice(buy_price);
        itemresult.setLocationInfo(location, item_country, longitude, latitude);
        itemresult.setCategories(categoryArray);
        itemresult.setBids(bidArray);

        return itemresult;
    }
}
