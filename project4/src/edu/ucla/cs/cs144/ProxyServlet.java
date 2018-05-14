package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	String query  = request.getParameter("q");
    	BufferedReader reader = null;
    	StringBuilder stringBuilder;

    	try{
    		URL url = new URL("http://google.com/complete/search?output=toolbar&q=" + query);
    		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    		connection.setRequestMethod("GET");
    		connection.setDoOutput(true); //if uncomment: write output to this url

    		if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
    			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    			stringBuilder = new StringBuilder();
    			String line = null;
    			while((line = reader.readLine())!=null)
    				stringBuilder.append(line);

    			reader.close();
    			connection.disconnect();

    			System.out.println(stringBuilder.toString());
    			response.setContentType("text/xml");
    			response.getWriter().write(stringBuilder.toString());

    		}
    	}catch(IOException e)
        {
        	e.printStackTrace();
        }
        // your codes here
    }
}
