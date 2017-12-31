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
import nhj.util.StringUtil;
import nhj.util.URLUtil;

public class BinanceAPI implements Runnable{
	
	private static void log(String log){
		System.out.println(log);
	}
	
	private String apiKey;
	private String apiSecret;
	private String tradeUrl = "https://poloniex.com/tradingApi";
	private static List TICKER_DATA = new ArrayList();
	
	
	private BinanceAPI(){
		
	}
	
	public BinanceAPI(String key, String secret){
		apiKey = key;
		apiSecret = secret;
	}
	
	
	public String getNonce(){
		return String.valueOf(System.currentTimeMillis());
	}
	
	
	public static void init() {
		new Thread(new BinanceAPI()).start();
	}
	
	
	public static void main(String[] args) throws Throwable {
		
		
		//Map rtnMap = api.buy("BCN", new BigDecimal( "0.00000141") , new BigDecimal(5000) );
		
		//log("rtnMap : "+rtnMap);
		
		//Map m = api.returnBalances();
		
		BinanceAPI.init();
		
		//BinanceAPI.private_returnTicker();
		Thread.sleep(3000);
		
		List list = BinanceAPI.returnTicker();
		
		log(list.toString());
		
		
		
	}
	public static List returnTicker() throws Throwable {
		
		return TICKER_DATA;
	}
	
	public static void private_returnTicker() throws Throwable {
		
		String json_str = URLUtil.getUrlJsonData("https://api.binance.com/api/v1/ticker/24hr");
		
		//System.out.println(""+json_str);
		
		//JsonObject result = JsonUtil.getJsonObject(json_str);
		
		Gson gson = new Gson();
		
		JsonArray ja = gson.fromJson(json_str, JsonElement.class).getAsJsonArray();
		
		List li = new ArrayList();
		
		for(int i = 0; i < ja.size();i++){
			
			JsonObject market = ja.get(i).getAsJsonObject();
			String symbol = JsonUtil.get(market, "symbol");			
			String symbol1 = StringUtil.rightCut(symbol, 3);
			String symbol2 = StringUtil.right(symbol, 3);
			
			if( !"BTC".equals(symbol1) ) {
				if( !"BTC".equals(symbol2) ) {
					continue;
				}
			}
			
			
			
			String unit_cid = "1";
			
			int divTitle = 0;
			
			if( "BTC".equals(symbol2) ){
				unit_cid = "1";
			}else if( "ETH".equals(symbol2)  ){
				unit_cid = "2";
			}else if( "XMR".equals(symbol2)  ){
				unit_cid = "4";
			}else if( "USDT".equals(symbol2)  ){
				unit_cid = "9999";
			}
			//log("symbol1 : " + symbol1 + ", " + symbol2 + " : unit_cid : " + unit_cid);
			
			Map tickMap = new HashMap();
			
			tickMap.put("cid", 1); 	// todo need mapping . 
			tickMap.put("eid", 7);	// poloniex id
			tickMap.put("ccd", symbol1);
			tickMap.put("unit_cid", unit_cid);
			String Last = get(market, "lastPrice" );
			tickMap.put("price", Last);
			
			double per_ch = Double.parseDouble(get(market, "priceChangePercent" )) / 100 ;
			//System.out.println("marketName : "+marketName + " , per_ch : " + per_ch + ", unit_cid : " + unit_cid);
			
			tickMap.put("per_ch", per_ch);
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
				Thread.sleep(15000);
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
