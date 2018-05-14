package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.text.SimpleDateFormat;
import java.lang.Integer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;
import edu.ucla.cs.cs144.getXMLdata;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */

	private IndexSearcher searcher = null;
	private QueryParser parser = null;

	public AuctionSearch() {
        try{
        	searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1"))));
        	parser = new QueryParser("union", new StandardAnalyzer());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

	public TopDocs performSearch(String queryString, int n) throws IOException, ParseException {
		Query query = parser.parse(queryString);
        return searcher.search(query, n);
    } 

    public Document getDocument(int docId) throws IOException {
        return searcher.doc(docId);
    }
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!

		SearchResult[] res = null;

		try{
			TopDocs topDocs = performSearch(query, numResultsToReturn + numResultsToSkip);
			ScoreDoc[] hits = topDocs.scoreDocs;

			if (topDocs.totalHits< (numResultsToReturn + numResultsToSkip)){
				numResultsToReturn = Math.max(0, (topDocs.totalHits - numResultsToSkip));
			}

			res = new SearchResult[numResultsToReturn];

			for(int i=0; i<numResultsToReturn; i++){
				Document doc = getDocument(hits[i+numResultsToSkip].doc);
    			String itemID = doc.get("itemID");
    			String name = doc.get("name");
			
    			res[i] = new SearchResult(itemID, name);
			} 

			return res;

		}catch (ParseException ex) {
            System.out.println(ex);
        } catch (IOException e) {
            System.out.println(e);
        }

	    return new SearchResult[0];
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!

		//spatial search to get all itemids;
		double lx = region.getLx();
		double ly = region.getLy();
		double rx = region.getRx();
		double ry = region.getRy();
		String tmpquery = String.format("select * from itemlocation where "
			+ "MBRContains(GeomFromText('Polygon((%f %f, %f %f, %f %f, %f %f, %f %f))'), ItemGeo)",
			 lx, ly, lx, ry, rx, ry, rx, ly, lx, ly);
		Set<String> idset = new HashSet<>();	
		try{
			Connection conn = DbManager.getConnection(true);
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(tmpquery);
			while(rs.next()){
				idset.add(rs.getString("ItemID"));
			}
			conn.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		List<SearchResult> resultlist = new ArrayList<>();

		try{
			TopDocs topDocs = performSearch(query, Integer.MAX_VALUE);
			ScoreDoc[] hits = topDocs.scoreDocs;
			if(hits.length<(numResultsToReturn+numResultsToSkip)){
				System.err.println("Result number is less than expected");
			}
			for(int i=0; i<hits.length; i++){
				Document doc = getDocument(hits[i].doc);
				String itemid = doc.get("itemID");
				if(resultlist.size()==(numResultsToReturn+numResultsToSkip))
					break;
				if(idset.contains(itemid)){
					resultlist.add(new SearchResult(itemid, doc.get("name")));
				}
			}
		}
		catch(IOException | ParseException e){
			e.printStackTrace();
		}

		SearchResult[] finalresult = new SearchResult[Math.min(numResultsToReturn, resultlist.size())];
		int index=0;

		for(int i=numResultsToSkip; i<resultlist.size(); i++){
			finalresult[index++] = resultlist.get(i);
		}

		return finalresult;
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		getXMLdata getxmldata = new getXMLdata(itemId);
		return getxmldata.getXML();
	}
	
	public String echo(String message) {
		return message;
	}

}
