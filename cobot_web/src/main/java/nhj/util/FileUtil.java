package nhj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtil {
	public static String getFileToStr( String absolutePath) throws Throwable{
		File f = new File(absolutePath);
		BufferedReader br = new BufferedReader ( new InputStreamReader( new FileInputStream(absolutePath)) );
		String line = null;
		StringBuilder sb = new StringBuilder();
		while( null != ( line = br.readLine())  ){
			sb.append(line);
		}
		return sb.toString();
	}
}
