package webCrawler;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.util.JSON;

import datarepository.DatabaseClient;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	
    public final static String file_store_folder_text = "C:/D/data/finalCrawled/files/";
    
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|csv" 
                                                      + "|png|tiff?|mid|mp2|mp3|mp4"
                                                      + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
                                                      + "|rm|smil|wmv|swf|wma|zip|rar|gz|xml|java|cpp))$");

   
    private DBCollection collection;
    BasicDBObject document = null;
    @Override
    public void onStart(){
    	
    	MongoClient mongoClient;
		try {
			
			mongoClient = DatabaseClient.getClient();
			mongoClient.setWriteConcern(WriteConcern.JOURNALED);
       
            DB database = DatabaseClient.GetDatabase("ICS");
		    collection = DatabaseClient.GetDBCollection(database, "inLinks1");
			//collection.remove(new BasicDBObject());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * You should implement this function to specify whether
     * the given url should be crawled or not (based on your
     * crawling logic).
     */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && href.contains("ics.uci.edu")
				&& !(href.contains("calendar"))
				&& !(href.contains("archive.ics.uci.edu"))
				&& !(href.contains("drzaius.ics.uci.edu"))
				&& !(href.contains("flamingo.ics.uci.edu"))
				&& !(href.contains("fano.ics.uci.edu"))
				&& !(href.contains("ironwood.ics.uci.edu"))
				&& !(href.contains("duttgroup.ics.uci.edu"))
				&& !(href.contains("wics.ics.uci.edu"))
				&& !(href.contains("physics.uci.edu"))
				&& !(href.contains("djp3-pc2.ics.uci.edu"))
				&& !(href.contains("informatics.uci.edu"));
	}

    /**
     * This function is called when a page is fetched and ready 
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {          
            String url = page.getWebURL().getURL();
            String subDomain = page.getWebURL().getSubDomain();
            System.out.println("URL: " + url);

            if (page.getParseData() instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    String text = htmlParseData.getText();
                    String html = htmlParseData.getHtml();
                    String title = parseTitle(html);
                    List<WebURL> links = htmlParseData.getOutgoingUrls();

                    try {
                    	//String fileName = url.replace("http://", "").concat(".txt");
                    	
						PrintWriter outText = new PrintWriter(new File(file_store_folder_text + 	java.net.URLEncoder.encode(url, "UTF-8").concat(".txt")));
						String[] arr = text.toLowerCase().split("[^a-zA-Z0-9]+");
						String processedStr = Arrays.toString(arr).replaceAll("[,\\[\\]]+", ""); 
			            String print = "{'text' : '"+ processedStr +"', 'title' : '"+ title +"', 'url' : '"+ url+"'} ";
						outText.print(print);
						outText.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
                    
                    System.out.println("Text length: " + text.length());
                    System.out.println("Html length: " + html.length());
                    System.out.println("Number of outgoing links: " + links.size());
                    
                    //BulkWriteOperation bulk = collection.initializeUnorderedBulkOperation();
                    for(WebURL inUrl: links) {
                    	 String json = "{'inUrl' : '"+ inUrl +"'}";
                    	DBObject dbObject = (DBObject)JSON.parse(json);
                    	collection.insert(dbObject);
                    }
            }
    }
    
    
    private String parseTitle(String html){
    	Pattern p = Pattern.compile(Pattern.quote("<title>") + "(.*?)" + Pattern.quote("</title>"));
    	String processedStr = null;
    	Matcher m = p.matcher(html);
    	while (m.find()) {
    	  String[] arr = m.group(1).toLowerCase().split("[^a-zA-Z0-9]+");
		 processedStr = Arrays.toString(arr).replaceAll("[,\\[\\]]+", ""); 
    	}
		return processedStr;   
    	
    }
}