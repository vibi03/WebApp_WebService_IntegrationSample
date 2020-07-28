package utilities;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TextReader {

	public static String ReadFile(String fileName) throws IOException {
		String everything;
 		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			everything = sb.toString();

		}

		return everything;
		// return everything;
	}
	
	public static List getStringList(String filepath) throws IOException{
	    
	    List<String> strList = new ArrayList<String>(); 
	    String text;
	    try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
           
                while((text = br.readLine())!= null){
                    strList.add(text);                    
                    
                }
           
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	        
	    
	    return strList;
	}

	public static String[] TestDataCycle(String path, int cycleStep) {
		//
		String[] cycdata = new String[cycleStep];

		try {

			File targetFile = new File(path);

			BufferedReader targetBuf = new BufferedReader(new FileReader(targetFile));

			String text;

			int lines = 0;
			while (targetBuf.readLine() != null) {
				lines++;
			}

//			targetBuf.close();

			// File tempFile = new File("temp.txt");
			// PrintWriter printTemp = new PrintWriter(tempFile);
			targetBuf.close();
			
//			targetBuf.reset();
			
			targetBuf = new BufferedReader(new FileReader(targetFile));
			
			Random random = new Random();
			int number = random.nextInt(lines) + 1;

			int j = 1;
			while (j <= number) {

				targetBuf.readLine();

				if (j == number) {

					for (int i = 1; i <= cycleStep; i++) {
						cycdata[i - 1] = targetBuf.readLine();
					}

				}

				j++;

			}
			targetBuf.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cycdata;

	}

}
