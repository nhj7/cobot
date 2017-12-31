package nhj.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.co.cobot.bot.DATA;
import kr.co.cobot.bot.SteemitBot;
import nhj.util.JsonUtil;
import nhj.util.StringUtil;
import nhj.util.URLUtil;

public class BitfinexAPI implements Runnable{
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(BitfinexAPI.class);
	
	private static void log(String log){
		System.out.println(log);
	}
	
	private String apiKey;
	private String apiSecret;
	private static List DATA = new ArrayList();
	
	private BitfinexAPI(){
		
	}
	
	
	
	
	public static void init() {
		try {
			setSymvols();
			new Thread(new BitfinexAPI()).start();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public BitfinexAPI(String key, String secret){
		apiKey = key;
		apiSecret = secret;
	}
	
	
	public String getNonce(){
		return String.valueOf(System.currentTimeMillis());
	}
	
	
	
	
	
	public static void main(String[] args) throws Throwable {
		
		
		//Map rtnMap = api.buy("BCN", new BigDecimal( "0.00000141") , new BigDecimal(5000) );
		
		//log("rtnMap : "+rtnMap);
		
		//Map m = api.returnBalances();
		
		
		init();
		
		if(true)return;
		setSymvols();
		
		private_returnTicker();
		
		List list = returnTicker();
		
		log(list.toString());
		
	}
	
	public static String SYMBOLS_STR = "";
	
	public static void setSymvols() throws Throwable {
		String json_str = URLUtil.getUrlJsonData("https://api.bitfinex.com/v1/symbols");
		Gson gson = new Gson();
		
		JsonElement je = gson.fromJson(json_str, JsonElement.class);
		JsonArray ja = je.getAsJsonArray();
		
		for(int i = 0; i < ja.size();i++) {
			if( i == 0 )
				SYMBOLS_STR += "t"+replaceToUpperCase(ja.get(i));
			else
				SYMBOLS_STR += ",t"+replaceToUpperCase(ja.get(i));
		}
	}
	
	
	
	public static List returnTicker() {
		return DATA;
	}
	

	
	public static void private_returnTicker() throws Throwable {
		
		String json_str = URLUtil.getUrlJsonData("https://api.bitfinex.com/v2/tickers?symbols=" + SYMBOLS_STR);
		
		System.out.println(""+json_str);
		
		//JsonObject result = JsonUtil.getJsonObject(json_str);
		
		Gson gson = new Gson();
		
		JsonElement je = gson.fromJson(json_str, JsonElement.class);
		
		
		JsonArray ja = je.getAsJsonArray();
		
		List li = new ArrayList();
		
		
		String priceName = "Last";
		for(int i = 0; i < ja.size();i++){
			
			if(!ja.get(i).isJsonArray())
				continue;
			
			JsonArray market = ja.get(i).getAsJsonArray();
						
			String marketName = replaceToUpperCase(market.get(0));
			
			//log("marketName : " + marketName);
			
			if( "BITCNY-BTC".equals(marketName)){
				continue;
			}
			
			String tmpMarketName = marketName.substring(1, marketName.length());
			
			String[] arrTitle = new String[] {
					StringUtil.right(tmpMarketName, 3)
					, StringUtil.rightCut(tmpMarketName, 3)
					
					 
			};
			
			String unit_cid = "1";
			
			int divTitle = 0;
			
			if( "BTC".equals(arrTitle[divTitle]) ){
				unit_cid = "1";
			}else if( "ETH".equals(arrTitle[divTitle])  ){
				unit_cid = "2";
				continue;
			}else if( "XMR".equals(arrTitle[divTitle])  ){
				unit_cid = "4";
				continue;
			}else if( arrTitle[divTitle].indexOf("USD") > -1  ){
				unit_cid = "9999";
			}else {
				continue;
			}
			
			if( "IOT".equals(arrTitle[1])) {
				System.out.println("IOT");
			}
			
			if( "1".equals(unit_cid) && "BTC".equals(arrTitle[1]) ) {
				continue;
			}			
			if( !"BTC".equals(arrTitle[1]) && !"1".equals(unit_cid) ) {
				continue;
			}			
			Map tickMap = new HashMap();			
			tickMap.put("cid", 1); 	// todo need mapping . 
			tickMap.put("eid", 6);	// bitfinex id
			tickMap.put("ccd", arrTitle[1]);			
			tickMap.put("unit_cid", unit_cid);			
			/*
			 [
		    SYMBOL,0
		    BID, 1
		    BID_SIZE, 2
		    ASK, 3
		    ASK_SIZE, 4
		    DAILY_CHANGE, 5
		    DAILY_CHANGE_PERC, 6
		    LAST_PRICE, 7
		    VOLUME, 8
		    HIGH, 9
		    LOW 10
		  ],
			 
			 */
			
			String Last = market.get(7).toString();
			tickMap.put("price", Last);
			
//			String PrevDay = get(market, "PrevDay");
//			

			String ch_str = market.get(6).toString();
			
			double per_ch = Double.parseDouble(ch_str);
			tickMap.put("per_ch", per_ch );
//			
//			if( !"0".equals(Last)){
//				per_ch = ((int) (( Double.parseDouble(Last) / Double.parseDouble(PrevDay) - 1 ) * 10000) ) / 10000.0 ;
//			}
//			
//			//System.out.println("marketName : "+marketName + " , per_ch : " + per_ch + ", unit_cid : " + unit_cid);
//			
			
//			
//			if( "9999".equals(unit_cid) && "BTC".equals(arrTitle[1]) ){
//				DATA.BTRX_USD_BITCOIN = get(market, priceName);
//			}
//			
//			if( "1".equals(unit_cid) && "SBD".equals(arrTitle[1]) ){
//				DATA.BTRX_BTC_SBD = get(market, priceName);
//			}			
			
			li.add( tickMap );
		}
		DATA = null;
		DATA = li;
	}
	
	
	private static String replaceToUpperCase( Object value ){
		if(value == null ) return "";
		return value.toString().replaceAll("\"", "").toUpperCase();
	}
	
	
	private static String get( JsonObject jo, String key ){
		return jo.get(key).toString().replaceAll("\"", "");
	}

	
	public void buy() throws Throwable {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void run() {
		while(true){
			try {
				private_returnTicker();
				System.out.println("DATA : "+DATA);
				Thread.sleep(6100);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				
				logger.error("BitfinexAPI", e);
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					
					logger.error("BitfinexAPI", e1);
				}
			}
		}
	}

	

	

}
