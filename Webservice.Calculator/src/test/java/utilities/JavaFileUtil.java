package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * File utilities
 *
 */
public class JavaFileUtil {

	/**
	 * Read a file based on a file path and return the string.
	 */
	public static String readFile(String filePath) {

		StringBuffer request = new StringBuffer();
		BufferedReader br = null;
		try {
			InputStream fis = new FileInputStream(new File(filePath));
			br = new BufferedReader(new InputStreamReader(fis));
			String record = new String();
			while ((record = br.readLine()) != null) {
				request.append(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return request.toString();
	}
	
	
	
	public static void compareTextfiles(File File1, File File2){
		
		
		
		
		
		
	}
	
	
	

}
