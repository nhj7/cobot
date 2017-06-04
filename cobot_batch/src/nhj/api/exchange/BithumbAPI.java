package nhj.api.exchange;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.apache.http.util.ByteArrayBuffer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BithumbAPI {

	public static void getTickData(String coinType) throws Throwable {

		Date d = new Date();
		long time = d.getTime();

		// BTC
		String url = "https://www.bithumb.com/json/ticker/?tick=" + time
				+ "&csrf_xcoin_name=ad7ec083722d28b3821f1091862ec319&_=1496522830310";
		String btc_json = reqToStringForBithumb(url);
		
		JsonParser jp = new JsonParser();
	    JsonObject btc_jo = jp.parse(( btc_json )).getAsJsonObject();
	    
	    
	    System.out.println("btc_jo : " + btc_jo );
	    
		
		// ETH, DASH, LTC, ETC
		url = "https://www.bithumb.com/trade/getCoinsTicker/DASH?_=" + time;
				
		String etc_json = reqToStringForBithumb(url);
		
		
		
		System.out.println("html : " + btc_json + ", " + etc_json );
		
	}

	public static String reqToStringForBithumb( String url ) throws Throwable, Throwable {

		HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
		huc.setRequestMethod("GET");

		huc.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Whale/0.7.33.5 Safari/537.36");

		huc.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		// huc.addRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
		huc.addRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
		huc.addRequestProperty("Connection", "keep-alive");
		huc.addRequestProperty("X-Requested-With", "XMLHttpRequest");

		huc.connect();

		BufferedInputStream bis = new BufferedInputStream(huc.getInputStream());
		ByteArrayBuffer baf = new ByteArrayBuffer(50);
		int read = 0;
		int bufSize = 512;
		byte[] buffer = new byte[bufSize];
		while (true) {
			read = bis.read(buffer);
			if (read == -1) {
				break;
			}
			baf.append(buffer, 0, read);
		}
		String queryResult = new String(baf.toByteArray());
		// System.out.println("queryResult : " + queryResult);

		return queryResult.toString();

	}

	public static void main(String[] args) throws Throwable {
		getTickData("");
	}
}
