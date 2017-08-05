package nhj.util;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class SecurityUtil {
	
	public static void main(String[] args) {
		for(int i = 0; i < 10;i++){
			System.out.println( getRandomID128() );
		}
	}
	
	public static String getRandomID(){
		String ts = String.valueOf(System.currentTimeMillis());
	    String rand = UUID.randomUUID().toString();
	    return DigestUtils.sha256Hex(ts + rand);		
	}
	
	public static String getRandomID128(){
		String ts = String.valueOf(System.currentTimeMillis());
	    String rand = UUID.randomUUID().toString();
	    return DigestUtils.sha1Hex(ts + rand);		
	}
}
