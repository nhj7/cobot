package nhj.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Ticker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.co.cobot.bot.DATA;
import nhj.util.JsonUtil;
import nhj.util.URLUtil;

public class BittrexAPI implements Runnable{
	
	private static void log(String log){
		System.out.println(log);
	}
	
	private String apiKey;
	private String apiSecret;
	private String tradeUrl = "https://poloniex.com/tradingApi";
	private static List TICKER_DATA = new ArrayList();
	
	
	private BittrexAPI(){
		
	}
	
	public BittrexAPI(String key, String secret){
		apiKey = key;
		apiSecret = secret;
	}
	
	
	public String getNonce(){
		return String.valueOf(System.currentTimeMillis());
	}
	
	
	public static void init() {
		new Thread(new BittrexAPI()).start();
	}
	
	
	public static void main(String[] args) throws Throwable {
		
		
		//Map rtnMap = api.buy("BCN", new BigDecimal( "0.00000141") , new BigDecimal(5000) );
		
		//log("rtnMap : "+rtnMap);
		
		//Map m = api.returnBalances();
		
		BittrexAPI api = new BittrexAPI("", "");
		
		List list = api.returnTicker();
		
		log(list.toString());
		
		
		
	}
	
	
	
	
	

	
	public static List returnTicker() throws Throwable {
		
		return TICKER_DATA;
	}
	
	public static void private_returnTicker() throws Throwable {
		
		String json_str = URLUtil.getUrlJsonData("https://bittrex.com/api/v1.1/public/getmarketsummaries");
		
		//System.out.println(""+json_str);
		
		//JsonObject result = JsonUtil.getJsonObject(json_str);
		
		Gson gson = new Gson();
		
		JsonElement je = gson.fromJson(json_str, JsonElement.class);
		
		
		JsonObject result = je.getAsJsonObject();
		
		List li = new ArrayList();
		if( !"true".equals( JsonUtil.get(result, "success")) ){
			return;
		}

		JsonArray ja = result.get("result").getAsJsonArray();
		String priceName = "Last";
		for(int i = 0; i < ja.size();i++){
			
			JsonObject market = ja.get(i).getAsJsonObject();
				
			/* 
			 * {"MarketName":"BITCNY-BTC","High":30830.91000000,"Low":27447.50000000,"Volume":1.77441319,"Last":28696.10700000,"BaseVolume":51220.23339095,"TimeStamp":"2017-08-19T17:51:38.207","Bid":27000.00000001,"Ask":28696.10700000,"OpenBuyOrders":176,"OpenSellOrders":68,"PrevDay":29669.86000001,"Created":"2015-12-11T06:31:40.653"}
			 * */
			
			
			
			
			String marketName = JsonUtil.get(market, "MarketName");
			
			//log("marketName : " + marketName);
			if( "BITCNY-BTC".equals(marketName)){
				continue;
			}
			
			String[] arrTitle = JsonUtil.get(market, "MarketName").split("-");
			
			String unit_cid = "1";
			
			int divTitle = 0;
			
			if( "BTC".equals(arrTitle[divTitle]) ){
				unit_cid = "1";
			}else if( "ETH".equals(arrTitle[divTitle])  ){
				unit_cid = "2";
			}else if( "XMR".equals(arrTitle[divTitle])  ){
				unit_cid = "4";
			}else if( "USDT".equals(arrTitle[divTitle])  ){
				unit_cid = "9999";
			}
			
			
			Map tickMap = new HashMap();
			
			tickMap.put("cid", 1); 	// todo need mapping . 
			tickMap.put("eid", 5);	// poloniex id
			tickMap.put("ccd", arrTitle[1]);
			
			tickMap.put("unit_cid", unit_cid);
			
			String Last = get(market, priceName );
			String PrevDay = get(market, "PrevDay");
			
			tickMap.put("price", Last);
			
			double per_ch = 0.0;
			
			if( !"0".equals(Last)){
				per_ch = ((int) (( Double.parseDouble(Last) / Double.parseDouble(PrevDay) - 1 ) * 10000) ) / 10000.0 ;
			}
			
			//System.out.println("marketName : "+marketName + " , per_ch : " + per_ch + ", unit_cid : " + unit_cid);
			
			tickMap.put("per_ch", per_ch);
			
			if( "9999".equals(unit_cid) && "BTC".equals(arrTitle[1]) ){
				DATA.BTRX_USD_BITCOIN = get(market, priceName);
			}
			
			if( "1".equals(unit_cid) && "SBD".equals(arrTitle[1]) ){
				DATA.BTRX_BTC_SBD = get(market, priceName);
			}			
			
			li.add( tickMap );
		}
		TICKER_DATA = null;
		TICKER_DATA = li;
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
				Thread.sleep(4900);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	

	

}
