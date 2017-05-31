package exam_cobot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.omg.CORBA.NameValuePair;

public class test3 {
	
	public static String hmac512Digest(String msg, String keyString) {

	    Mac shaMac;
	    try {
	        shaMac = Mac.getInstance("HmacSHA512");
	        SecretKeySpec  keySpec = new SecretKeySpec(keyString.getBytes(), "HmacSHA512");

	        shaMac.init(keySpec);
	        final byte[] macData = shaMac.doFinal(msg.getBytes());
	        return Hex.encodeHexString(macData); //again with try/catch for InvalidKeyException

	    } catch (Exception e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    } 
	    return null;
	}

	
	
	public static void main(String[] args) throws Throwable {
		
		String reqUrl = "";
		
		String url = "https://poloniex.com/tradingApi";
		
		String queryArgs = "command=returnBalances&nonce=0";
		
		
		String apiKey = "3HUZNMMZ-U3KB6VHW-ND311IYV-IA8GZ65S";
		String secret = "3773062c77ba48aaba1fc8816f91bc6fa15a1e165a74cad32dc75586a674d864c7a75a86d32fb58fc1e2676abac3ec5c49cabfbc926e5e3ba336737a009087ad";
		
		Mac shaMac = Mac.getInstance("HmacSHA512"); //with try/catch for NoSuchAlgorithmException
		SecretKeySpec  keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
		
		shaMac.init(keySpec);
		final byte[] macData = shaMac.doFinal(queryArgs.getBytes());
		String result = Hex.encodeHexString(macData); //again with try/catch for InvalidKeyException
		//this result you add as header param
		
		HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
		huc.setRequestMethod("POST");
		
		huc.setRequestProperty("Key", apiKey);
		huc.setRequestProperty("Sign", result);
		
		
		
		//and the queryArgs themselves you write onto the connection's outputstream
		huc.connect();

		BufferedReader br = new BufferedReader( new InputStreamReader(huc.getInputStream(), "UTF-8"));
		
//		Map m = huc.getHeaderFields();
//		
//		
//		String cookie = "";
//        if(m.containsKey(GET)) {
//                Collection c =(Collection)m.get(GET);
//                for(Iterator i = c.iterator(); i.hasNext(); ) {
//                        cookie = (String)i.next();
//                }
//                print("server response cookie:" + cookie);
//        }
		String line = null;
		
		StringBuilder sb = new StringBuilder();
		
		while( (line = br.readLine()) != null ){
			
			//print(line);
			sb.append(line);
		}
		
		br.close();
		
		
		
		
		System.out.println(sb);
		
		
		
		
		
		
	
	}
	
	
	public static String htmlToString(String url ) throws Throwable, Throwable{
		
		
		HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
		huc.setRequestMethod("GET");
		huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		
		huc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
		huc.connect();

		
		BufferedReader br = new BufferedReader( new InputStreamReader(huc.getInputStream(), "UTF-8"));
		
//		Map m = huc.getHeaderFields();
//		
//		
//		String cookie = "";
//        if(m.containsKey(GET)) {
//                Collection c =(Collection)m.get(GET);
//                for(Iterator i = c.iterator(); i.hasNext(); ) {
//                        cookie = (String)i.next();
//                }
//                print("server response cookie:" + cookie);
//        }
		String line = null;
		
		StringBuilder sb = new StringBuilder();
		
		while( (line = br.readLine()) != null ){
			
			//print(line);
			sb.append(line);
		}
		
		br.close();
		
		return sb.toString();
		
	}

}
