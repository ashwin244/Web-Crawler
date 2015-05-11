package filemanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * 
 *
 */
public class FileUtilities {
	

	/**
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> GetListOfFiles(String path) throws Exception {
		ArrayList<String> filePathList = new ArrayList<String>();
		try {
			Files.walk(Paths.get(path)).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					filePathList.add(filePath.toString());
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return filePathList;
	}

	
	
	/**
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static String ReadFile(String filepath) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filepath));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			
			reader.close();
			return stringBuilder.toString();
		} 
		catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
}
