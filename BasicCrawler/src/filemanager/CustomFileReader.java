package filemanager;

import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
public class CustomFileReader {
	DBCollection collection;

	public CustomFileReader(DBCollection dbCollection) {
		collection = dbCollection;
	}

	public boolean WriteFilesIntoDatabase(ArrayList<String> listOfFiles)
			throws Exception {
		String inputText = "";
		int j = 0;
		try {
			for (String fp : listOfFiles) {
				inputText = FileUtilities.ReadFile(fp);
				collection.insert((DBObject) JSON.parse(inputText));
				System.out.println("File written into database! " + j++);
			}
			System.out.println("Files stored successfully!!!!");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
}
