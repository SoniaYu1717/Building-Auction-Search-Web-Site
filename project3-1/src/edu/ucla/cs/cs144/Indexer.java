package edu.ucla.cs.cs144;

import java.util.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    private IndexWriter indexWriter=null;

    //Create a new index if one does not exist, otherwise, open the index and documents will be appended.
    public IndexWriter getIndexWriter(boolean create){
        if (indexWriter == null){
            try{
                Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1"));
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
                config.setOpenMode(create ? IndexWriterConfig.OpenMode.CREATE : IndexWriterConfig.OpenMode.APPEND);
                indexWriter = new IndexWriter(indexDir, config);
            }catch (IOException e) {
                System.out.println(e);
            }
        }
        return indexWriter;       
    }

    public void closeIndexWriter() {
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
   }

    public void indexItems(String itemID, String name, String description, String category) throws IOException{
        // append the documents

        IndexWriter writer=getIndexWriter(false); 
        Document doc = new Document();

        String fullSearchableText= name + " " + description + " " + category;

        doc.add(new StringField("itemID", itemID, Field.Store.YES));
        doc.add(new StringField("name", name, Field.Store.YES));
        doc.add(new TextField("union", fullSearchableText, Field.Store.NO));

        writer.addDocument(doc);
    }

    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
    	try {
    	    conn = DbManager.getConnection(true);
    	} catch (SQLException ex) {
    	    System.out.println(ex);
    	}

        // create indexes
        try{
            getIndexWriter(true);

            String itemID, name, description, category;

            Statement itemStmt = conn.createStatement();
            ResultSet itemRs = itemStmt.executeQuery("SELECT ItemID, Name, Description FROM Item");

            PreparedStatement categoryStmt=conn.prepareStatement(
                "SELECT Category FROM ItemCategory WHERE ItemID = ?"
            );

            // join category with item
            while (itemRs.next()){
                
                category="";
                itemID = itemRs.getString("ItemID");
                name = itemRs.getString("Name");
                description = itemRs.getString("Description");
                categoryStmt.setString(1, itemID);
                ResultSet categoryRs = categoryStmt.executeQuery();

                while (categoryRs.next()){
                    category += categoryRs.getString("Category") + " ";
                }

                indexItems(itemID, name, description, category);

                categoryRs.close();

            }
            itemRs.close();
            itemStmt.close();
            categoryStmt.close();
            closeIndexWriter();
            conn.close();

        }catch (IOException e){
            System.out.println(e);
        }catch (SQLException ex) {
            System.out.println(ex);
        }
	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
    /*   try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    */
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
