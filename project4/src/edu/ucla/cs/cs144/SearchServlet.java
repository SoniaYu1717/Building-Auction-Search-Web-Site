package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}


    public int stringToInt(String s, int defaultValue) {
    	int result;
    	try {
    		result = Integer.parseInt(s);
    	} catch (NumberFormatException e) {
    		result = defaultValue;
    	}
    	return result;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        int defaultSkip = 0;
        int defaultReturn = 10;
        int numResultsToSkip = 0;
        int numResultsToReturn = 10;

        String query = "";

        if(request.getParameter("q") != null) {
            query = request.getParameter("q");
        }
        if(request.getParameter("numResultsToSkip") != null) {
        	numResultsToSkip = stringToInt(request.getParameter("numResultsToSkip"), defaultSkip);
        }
        if(request.getParameter("numResultsToReturn") != null) {
        	numResultsToReturn = stringToInt(request.getParameter("numResultsToReturn"), defaultReturn);
        }

        if(numResultsToSkip < 0) 
        	numResultsToSkip = defaultSkip;
        if(numResultsToReturn < 0)
        	numResultsToReturn = defaultReturn;

        SearchResult[] res = AuctionSearch.basicSearch(query, numResultsToSkip, numResultsToReturn);
        SearchResult[] moreRes = AuctionSearch.basicSearch(query, numResultsToSkip + numResultsToReturn, 1);

        request.setAttribute("q", query);
        request.setAttribute("numResultsToSkip", numResultsToSkip);
        request.setAttribute("numResultsToReturn", numResultsToReturn);
        request.setAttribute("results", res);
        request.setAttribute("requestMore", moreRes.length == 1);
        request.getRequestDispatcher("/SearchResult.jsp").forward(request, response);
    }

}
