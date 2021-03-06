package nhj.api;

import java.io.BufferedInputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.util.ByteArrayBuffer;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.co.cobot.bot.DATA;

public class BithumbAPI implements Runnable{
	
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(BithumbAPI.class);
	
	private List tickList;
	
	private BithumbAPI() {
		
	}

	public static void log( String log ){
		System.out.println(log);
	}
	
	public static String replaceDQuote( Object jo ){
		if( jo == null ){
			return "";
		}else if(jo.toString().length() == 0 ) {
			return "";
		}
		
		return jo.toString().replaceAll("\"", "");
	}
	
	public static JsonObject transJsonObject( JsonObject jo ){
		JsonObject newJo = new JsonObject();
		
		newJo.addProperty("ccd", "BTC");
		newJo.addProperty("unit_cid", "9998");
		
		String str_price = replaceDQuote(jo.get("buy_price"));
		String str_opening_price = replaceDQuote(jo.get("opening_price"));
		newJo.addProperty("price", str_price );
		//newJo.addProperty("opening_price", str_opening_price );
	    
	    BigDecimal opening_price = new BigDecimal( str_opening_price ) ;
	    BigDecimal price = new BigDecimal( str_price ) ;
	    
	    BigDecimal upndown_price = price.subtract(opening_price);
	    
	    newJo.addProperty("upndown_price", upndown_price );
	    
	    BigDecimal ch = upndown_price.divide(opening_price  , 8 ,  BigDecimal.ROUND_DOWN );
	    //ch = ch.add(new BigDecimal(100));
	    
	    //System.out.println( "per_ch" + ch);
	    
	    newJo.addProperty("per_ch", ch );
	    
	    
	    //System.out.println("newBtc : " + newJo );
	    
	    return newJo;
	    
	}
	
	private static boolean switchPrice = true;
	public static Map transMap( String ccd, JsonObject jo ){
		Map newJo = new HashMap();
		newJo.put("eid", "2");
		newJo.put("ccd", ccd);
		newJo.put("unit_cid", "9998");
		
		BigDecimal sellPrice = new BigDecimal(replaceDQuote(jo.get("sell_price")));
		BigDecimal buyPrice = new BigDecimal( replaceDQuote(jo.get("buy_price")) );
		
		String str_price = sellPrice.add(buyPrice).divide(new BigDecimal(2), BigDecimal.ROUND_FLOOR).toString();
		
		//String str_price = replaceDQuote( switchPrice ? jo.get("sell_price") : jo.get("buy_price") );
		
		
		
		String str_opening_price = replaceDQuote(jo.get("opening_price"));
		newJo.put("price", str_price );
		//newJo.put("opening_price", str_opening_price );
	    
	    BigDecimal opening_price = new BigDecimal( str_opening_price ) ;
	    BigDecimal price = new BigDecimal( str_price ) ;
	    
	    BigDecimal upndown_price = price.subtract(opening_price);
	    
	    newJo.put("upndown_price", upndown_price );
	    
	    BigDecimal ch = upndown_price.divide(opening_price  , 8 ,  BigDecimal.ROUND_DOWN );
	    //ch = ch.add(new BigDecimal(100));
	    
	    //System.out.println( "per_ch" + ch);
	    
	    newJo.put("per_ch", ch );
	    
	    
	    //System.out.println("newBtc : " + newJo );
	    
	    return newJo;
	    
	}
	
	
	private void private_returnTicker(  ) throws Throwable{
		Date d = new Date();
		long time = d.getTime();

		// BTC
		String url = "https://www.bithumb.com/json/ticker/?tick=" + time
				+ "&csrf_xcoin_name=ad7ec083722d28b3821f1091862ec319&_=1496522830310";
		String btc_json = reqToStringForBithumb(url);
		
		JsonParser jp = new JsonParser();
		
		List list = new ArrayList();
		{
			JsonObject btc_jo = jp.parse(( btc_json )).getAsJsonObject();
		    JsonObject btc_info = btc_jo.get("data").getAsJsonObject();
		    
		    log("BTC : " + btc_info);
		    
		    
		    Map newBtc = transMap( "BTC",btc_info );
		    
		    
		    
		    list.add( newBtc );
		}
	    
		{
			// ETH, DASH, LTC, ETC
			url = "https://www.bithumb.com/trade/getCoinsTicker/DASH?_=" + time;
			
			//System.out.println(url  );
			
			String etc_str = reqToStringForBithumb(url);
			
			//System.out.println("etc_json: " + etc_str);
			
			JsonObject etc_jo = jp.parse(( etc_str )).getAsJsonObject();
			JsonObject etc_info = etc_jo.get("data").getAsJsonObject();
			
			//System.out.println("etc_info: " + etc_info);
			
			
			
			
			
			for( Iterator it = etc_info.entrySet().iterator(); it.hasNext();){
				
				Entry en = (Entry)it.next();
				String title = (String) en.getKey();
				
				//System.out.println("title : " + title);
				
				
				String[] arrTitle = title.split("_");
				
				String unit_cid = "9998";
				
				
				JsonObject tickJo = ((JsonElement)en.getValue()).getAsJsonObject();
				
				log( title + " : " + tickJo);
				
				Map newCoin = transMap( title, tickJo );				
				list.add( newCoin );
			}
			
		}
		
		switchPrice = switchPrice ? false : true;
		
		DATA.setBitthumb_LIST(list);
	}
	
	
	public static void private_returnTicker_new() throws Throwable{
		Date d = new Date();
		long time = d.getTime();

		// BTC
		String url = "https://api.bithumb.com/public/ticker/ALL";
		String jsonStr = reqToStringForBithumb(url);
		
		JsonParser jp = new JsonParser();
		
		JsonObject jsonObj = jp.parse(( jsonStr )).getAsJsonObject();
		JsonObject coinObj = jsonObj.get("data").getAsJsonObject();
		
		List list = new ArrayList();
		for(Iterator it = coinObj.entrySet().iterator();it.hasNext();) {
			Entry coinDetail = (Entry) it.next();
			Object valueObj = coinDetail.getValue();
			if( valueObj instanceof com.google.gson.JsonObject) {
				Map coinMap = transMap( coinDetail.getKey().toString() , (JsonObject) coinDetail.getValue() );
				list.add( coinMap );
			}
		}
		switchPrice = switchPrice ? false : true;
		
		DATA.setBitthumb_LIST(list);
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
		//System.out.println(returnTicker());
		
		private_returnTicker_new();
		
		System.out.println(DATA.getBitthumb_LIST());
		
	}

	@Override
	public synchronized void run() {
		while(true){
			try {
				private_returnTicker_new();
				Thread.sleep(4000);
			} catch (Throwable e) {				
				e.printStackTrace();				
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}

	public static void init() {
		// TODO Auto-generated method stub
		new Thread(new BithumbAPI()).start();
	}
}
