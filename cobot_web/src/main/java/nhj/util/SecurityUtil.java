package nhj.util;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class SecurityUtil {
	
	public static void main(String[] args) {
		for(int i = 0; i < 10000;i++){
			System.out.println( getRandomID() );
		}
	}
	
	public static String getRandomID(){
		String ts = String.valueOf(System.currentTimeMillis());
	    String rand = UUID.randomUUID().toString();
	    return DigestUtils.sha256Hex(ts + rand);
		
	}
}
