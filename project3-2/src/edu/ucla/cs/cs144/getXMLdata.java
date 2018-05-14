package edu.ucla.cs.cs144;

import java.sql.*;
import java.lang.*;

import java.text.DateFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import edu.ucla.cs.cs144.DbManager;

public class getXMLdata {
	private String itemID;
	private Connection conn;
	private String xmldata;

	public getXMLdata(String itemID){
		this.itemID = itemID;
		xmldata = "";
		try{
			conn = DbManager.getConnection(true);
			convertXML();
			conn.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void convertXML() throws SQLException{
		String query = "select * from Item where ItemID=" + itemID;
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		if(rs.next()){
			xmldata += String.format("<Item ItemID=\"%s\">\n", rs.getString("ItemID"));
			xmldata += String.format("<Name>%s</Name>\n", escapeXMLtag(rs.getString("Name")));
			getcategory();
			xmldata += String.format("<Currently>$%s</Currently>\n", rs.getString("Currently"));
			String buyprice = rs.getString("Buy_Price");
			if(buyprice!=null){
				xmldata += String.format("<Buy_Price>$%s</Buy_Price>\n", buyprice);
			}
			xmldata += String.format("<First_Bid>$%s<First_Bid>\n", rs.getString("First_Bid"));
			int bidnum = rs.getInt("Number_of_Bids");
			xmldata += String.format("<Number_of_Bids>%d<Number_of_Bids>\n", bidnum);
			if(bidnum!=0){
				getbids();
			}
			else{
				xmldata += "<Bids />\n";
			}
			String location = rs.getString("Location");
			String latitude = rs.getString("Latitude");
			String longitude = rs.getString("Longitude");
			if(latitude==null && longitude==null){
				xmldata += String.format("<Location>%s</Location>\n", escapeXMLtag(location));
			}
			else{
				if(latitude==null)
					xmldata += String.format("<Location Longitude=\"%s\">%s</Location>\n", longitude, location);
				else if(longitude==null)
					xmldata += String.format("<Location Latitude=\"%s\">%s</Location>\n", latitude, location);
				else
					xmldata += String.format("<Location Latitude=\"%s\" Longitude=\"%s\">%s</Location>\n", latitude, longitude, location);
			}
			xmldata += String.format("<Country>%s</Country>\n", escapeXMLtag(rs.getString("Country")));
			xmldata += String.format("<Started>%s</Started>\n", getdate(rs.getString("Started")));
			xmldata += String.format("<Ends>%s</Ends>\n", getdate(rs.getString("Ends")));
			String sellerid = rs.getString("SellerID");
			String tmpquery = "select Rating from Seller where UserID='" + sellerid + "'";
			Statement tmpstatement = conn.createStatement();
			ResultSet sellerrs = tmpstatement.executeQuery(tmpquery);
			if(sellerrs.next()){
				xmldata += String.format("<Seller Rating=\"%s\" UserID=\"%s\" />\n", sellerrs.getString("Rating"), sellerid);
			}

			String description = escapeXMLtag(rs.getString("Description"));
			if(description.equals(""))
				xmldata += "<Description />\n";
			else
				xmldata += String.format("<Description>%s</Description>\n", escapeXMLtag(rs.getString("Description")));

			xmldata += "</Item>\n";
		}
	}

	private String escapeXMLtag(String xmlstring){
		if(xmlstring == null)
			return null;
		String revised = "";
		for(char e: xmlstring.toCharArray()){
			switch(e){
				case '\"': revised += "&quot;"; break;
				case '\'': revised += "&apos;"; break;
				case '<': revised += "&lt;"; break;
				case '>': revised += "&gt;"; break;
				case '&': revised += "&amp;"; break;
				default: revised += e; break;
			}
		}
		return revised;
	}

	private void getcategory() throws SQLException{
		String query = "select Category from ItemCategory where ItemID=" + itemID;
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		while(rs.next()){
			xmldata += String.format("<Category>%s</Category>\n", escapeXMLtag(rs.getString("Category")));
		}
		return;
	}

	private void getbids() throws SQLException{
		xmldata += "<Bids>";
		String query1 = "select UserID, Time, Amount from Bid where ItemID=" + itemID;
		Statement statement1 = conn.createStatement();
		Statement statement2 = conn.createStatement();
		ResultSet rs = statement1.executeQuery(query1);
		while(rs.next()){
			xmldata += "<Bid>";

			//bidder information
			String userid = rs.getString("UserID");
			String query2 = "select Rating, Location, Country from Bidder where UserID='" + userid + "'";
			ResultSet userrs = statement2.executeQuery(query2);
			if(userrs.next()){
				xmldata += String.format("<Bidder Rating=\"%s\" UserID=\"%s\">\n", userrs.getString("Rating"), userid);
				String location = userrs.getString("Location");
				String country = userrs.getString("Country");
				if(location!=null)
					xmldata += ( "<Location>" + escapeXMLtag(location) + "</Location>\n" );
				if(country!=null)
					xmldata += ("<Country>" + escapeXMLtag(country) + "</Country>\n");
				xmldata += "</Bidder>\n";
			}


			xmldata += String.format("<Time>%s</Time>\n", getdate(rs.getString("Time")));
			xmldata += String.format("<Amount>$%s</Amount>\n", rs.getString("Amount"));
			xmldata += "</Bid>";
		}
		xmldata += "</Bids>";
		return;
	}

    private String getdate(String date) {
        try {
                SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat new_format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");

                date = new_format.format(old_format.parse(date));
        }
        catch(ParseException e) {
            System.out.println("Parse error!");
        }
        
        return date;
    }

    public String getXML(){
    	return xmldata;
    }
}