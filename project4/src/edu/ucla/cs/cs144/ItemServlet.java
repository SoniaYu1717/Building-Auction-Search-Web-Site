package edu.ucla.cs.cs144;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import javax.servlet.Servlet;
import java.io.ByteArrayInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    public static Document stringToDoc(String xmlData)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringReader reader = new StringReader(xmlData);
            InputSource is = new InputSource();
            is.setCharacterStream(reader);
            Document doc = builder.parse(is);
            return doc;
        } catch (Exception e) {
        	System.out.println(e);
        }

        return null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    	String query = "";
    	if (request.getParameter("id") != null) {
    		query = request.getParameter("id");
    	}
    	request.setAttribute("id", query);

    	String xmlData = AuctionSearch.getXMLDataForItemId(query);
    	request.setAttribute("xmlData", xmlData);
    	if (xmlData == null || xmlData.isEmpty()) {
    		request.setAttribute("xmlData", "");
    		request.getRequestDispatcher("/ItemResult.jsp").forward(request, response);
            return;
    	}

    	ItemResult item = new ItemResult();

    	try {
    		Document doc = stringToDoc(xmlData);
    		Element root = doc.getDocumentElement();
    		item = MyParser.parseData(root);
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	request.setAttribute("Item", item);
    	request.getRequestDispatcher("/ItemResult.jsp").forward(request, response);
    }
}
