package nhj.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.socket.client.IO;
import io.socket.client.IO.Options;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import nhj.util.URLUtil;

public class CoinoneAPI implements Runnable{
	private static Map<String, Map<String, String> > WS_COIN_INFO = new HashMap();
	
	private CoinoneAPI(){
		
	}
	private static CoinoneAPI api;
	
	public static CoinoneAPI getInstance(){
		if( api == null ){
			api = new CoinoneAPI();
		}
		return api;
	}
	
	public static void init(){
		new Thread( getInstance() ).start();
	}
	
	
	
	public synchronized static void setPrice( String json_str ){
		Gson gson = new Gson();
		JsonObject jo = gson.fromJson(json_str, JsonObject.class);
		
		for(Iterator it = jo.entrySet().iterator();it.hasNext();){
			
			Entry en = (Entry) it.next();
			String title = en.getKey().toString();
			String value = en.getValue().toString().replaceAll("\"", "");
			String[] arrTitle  = title.split("_");
			
			String currency = arrTitle[0].toUpperCase();
			
			Map coin_info;
			if( WS_COIN_INFO.get(currency) == null ){
				coin_info = new HashMap<String, String>();
				WS_COIN_INFO.put(currency, coin_info );
			}else{
				coin_info = WS_COIN_INFO.get(currency);
			}
			
			if( arrTitle.length == 2 ){
				coin_info.put("price", value);
			}else{
				coin_info.put("yesterday_price", value);
			}
		}
		
		//System.out.println("[WS_COIN_INFO] : "+WS_COIN_INFO);
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		

		try {
			Options opts = new Options();
			opts.transports = new String[]{ "websocket", "polling"};
			Socket socket = IO.socket("https://push.coinone.co.kr/ticker", opts);
			System.out.println("socket : "+ socket);
			// {transports:['websocket','polling']}
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			  @Override
			  public void call(Object... args) {
			    //socket.emit("foo", "hi");
			    //socket.disconnect();
				System.out.println("EVENT_CONNECT : "+args);
			  }

			}).on("update", new Emitter.Listener() {

			  @Override
			  public void call(Object... args) {
				  
				  //System.out.println(DateUtil.getCurDate("[yyyy-MM-dd kk:mm:ss]") + " update : "+args[0]);
				  
				  setPrice( args[0].toString() );
			  }

			}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			  @Override
			  public void call(Object... args) {
				  System.out.println("EVENT_DISCONNECT : "+args[0]);
			  }

			});
			socket.connect();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static List returnTicker() throws Throwable {
		
		List list = new ArrayList();
		
		for(Iterator it = WS_COIN_INFO.keySet().iterator();it.hasNext();){
			
			String ccd = it.next().toString();
			
			
			Map tickMap = new HashMap();
			
			tickMap.put("cid", 1); 	// todo need mapping . 
			tickMap.put("eid", 3);	// Coinone id
			tickMap.put("ccd", ccd );
			
			tickMap.put("unit_cid", "9998");	// KRW
			
			BigDecimal last = new BigDecimal( WS_COIN_INFO.get(ccd).get("price") );
			
			BigDecimal first = new BigDecimal( WS_COIN_INFO.get(ccd).get("yesterday_price") );
			
			BigDecimal ch = last.subtract(first);
			
			BigDecimal per_ch = ch.divide(first, 4, BigDecimal.ROUND_DOWN );
			tickMap.put("first", first);
			tickMap.put("ch", last.subtract(first));
			tickMap.put("price", last);
			tickMap.put("per_ch", per_ch);
			
			list.add(tickMap);
			
		}
		return list;
	}
	
	
	public static String getUSDCurrency() throws Throwable{
		
		String url = "https://api.coinone.co.kr/currency/?format=json&currencyType=JP";
		String rtnJsonStr = URLUtil.htmlToString(url);
		
		JsonObject jo = new Gson().fromJson(rtnJsonStr, JsonObject.class);
		
		return jo.get("currency").toString();	
		
	}
	
	public static List returnTickerBak() throws Throwable {
		
		String url = "https://api.coinone.co.kr/ticker/?format=json&currency=all";
		String rtnJsonStr = URLUtil.htmlToString(url);
		
		JsonObject jo = new Gson().fromJson(rtnJsonStr, JsonObject.class);
		
		List list = new ArrayList();
		
		if( "0".equals( get(jo, ("errorCode")) )){
			jo.remove("timestamp");
			jo.remove("errorCode");
			jo.remove("result");
			
			for(Iterator it = jo.entrySet().iterator(); it.hasNext() ;){
				Entry en = (Entry) it.next();
				String currency = en.getKey().toString().toUpperCase();
				
				JsonObject tickJo = ((JsonElement)en.getValue()).getAsJsonObject();
				
				
				System.out.println("tickJo : "+tickJo);
				
				Map tickMap = new HashMap();
				
				tickMap.put("cid", 1); 	// todo need mapping . 
				tickMap.put("eid", 3);	// Coinone id
				tickMap.put("ccd", get(tickJo, "currency").toUpperCase());
				
				tickMap.put("unit_cid", "9998");	// KRW
				
				BigDecimal last = new BigDecimal(get(tickJo, "last"));
				
				BigDecimal first = new BigDecimal(get(tickJo, "first"));
				
				BigDecimal ch = last.subtract(first);
				
				BigDecimal per_ch = ch.divide(first, 4, BigDecimal.ROUND_DOWN );
				tickMap.put("first", first);
				tickMap.put("ch", last.subtract(first));
				tickMap.put("price", last);
				tickMap.put("per_ch", per_ch);
				
				list.add(tickMap);
			}
		}
		System.out.println("list : "+list);
		
		return list;
		
	}
	
	private static String get( JsonObject jo, String key ){
		return jo.get(key).toString().replaceAll("\"", "");
	}
	
	
	
	public static void main(String[] args) throws Throwable {
		//System.out.println( returnTicker() );
		init();
		
		Thread.sleep(1000);
		
		List l = returnTicker();
		
		
		System.out.println("list : "+l);
	}
}
