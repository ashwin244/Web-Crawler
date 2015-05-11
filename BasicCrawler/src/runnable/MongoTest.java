package runnable;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;

import datarepository.DatabaseClient;
import filemanager.CustomFileReader;
import filemanager.FileUtilities;
public class MongoTest 
{
	public static void main(String[] args)
	{
		try
		{	
			MongoClient mongoClient = DatabaseClient.getClient();
			mongoClient.setWriteConcern(WriteConcern.JOURNALED);
			
            DB database = DatabaseClient.GetDatabase("ICS");
		 	DBCollection collection = DatabaseClient.GetDBCollection(database, "cICSData2");
			collection.remove(new BasicDBObject());
		 	
			ArrayList<String> filePathList = new ArrayList<String>();
			filePathList = FileUtilities.GetListOfFiles("C:\\D\\data\\finalCrawled\\datas");
			CustomFileReader fileReader = new CustomFileReader(collection);
			fileReader.WriteFilesIntoDatabase(filePathList);
			
			//Stage 1
			String indexreduce1 = readFile("wc_index_reduce_1.js");
			String indexMap1 = readFile("wc_index_map_1.js");
			
			//Stage 1
			DBCollection indexStorageCollection = DatabaseClient.GetDBCollection(database, "indexStorage3");
			indexStorageCollection.remove(new BasicDBObject());
			MapReduceOutput outIndexStage1 = collection.mapReduce(indexMap1, indexreduce1, "indexStorage3", MapReduceCommand.OutputType.REPLACE, null);
			indexStorageCollection = DatabaseClient.GetDBCollection(database, "indexStorage3");
			System.out.println("done with map reduce");
		}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * http://stackoverflow.com/questions/950513/how-to-copy-one-stream-to-a-byte-array-with-the-smallest-c-sharp-code
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static String readFile(String fileName) throws IOException { 
        InputStream fileStream = MongoTest.class.getResourceAsStream(fileName);
        byte[] buffer = new byte[8192];
        int size = fileStream.read(buffer);
        fileStream.close();
        return new String(buffer, 0, size);
    }
}
