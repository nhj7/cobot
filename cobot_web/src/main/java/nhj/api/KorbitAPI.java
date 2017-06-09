package nhj.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nhj.util.JsonUtil;
import nhj.util.URLUtil;

public class KorbitAPI implements Runnable{
private static Map<String, Map<String, String> > COIN_INFO = new HashMap();
	
	private KorbitAPI(){
		
	}
	private static KorbitAPI api;
	
	public static KorbitAPI getInstance(){
		if( api == null ){
			api = new KorbitAPI();
		}
		return api;
	}
	
	public static void init(){
		log("[KorbitAPI].start!");
		new Thread( getInstance() ).start();
	}
	
	@Override
	public void run() {
		while(true){
			
			
			try {
				parseUrl();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	// https://www.korbit.co.kr/market
	public static List returnTicker(){
		List list = new ArrayList();
		
		for(Iterator it = COIN_INFO.keySet().iterator();it.hasNext();){
			
			String ccd = it.next().toString();
			
			
			
			
			list.add(COIN_INFO.get(ccd));
			
		}
		return list;
		
		
	}
	
	public static void log(String log){
		System.out.println(log);
	}
	
	
	public synchronized static void parseUrl() throws Throwable{
		
		String[] arr_url = new String[]{
				"https://www.korbit.co.kr/market"
				, "https://www.korbit.co.kr/eth_market"
				, "https://www.korbit.co.kr/etc_market"
				, "https://www.korbit.co.kr/xrp_market"
		};
	
		for(int i = 0; i < arr_url.length;i++){
			String html = URLUtil.htmlToString(arr_url[i]);
			
			Document dom = Jsoup.parse(html);
			Elements arr_elements = 
					 dom.getElementsByClass("market-contents");
			
			Element market_contents = (Element)arr_elements.get(0);
			
			Element data = market_contents.getElementsByAttributeValue("data-component-name", "MarketDataBlock").get(0);
			
			
			//log("data-props : "+data.attr("data-props"));
			
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(data.attr("data-props"), JsonObject.class);
			JsonObject market_data = jo.get("market_data").getAsJsonObject();
			String coin = get(jo, "base").toUpperCase();
			String price = get(market_data, "last_price").toString();
			String ch = get(market_data, "signed_distance");
			String per_ch = get(market_data, "signed_distance_pct");
			
			/*
			log("coin : " + coin );		
			log("last_price : " + price );
			log("ch : " + ch );
			log("per_ch : " + per_ch );
			log("first_price : " + get(market_data, "first_price").toString() );
			
			*/
			
			
			Map coin_map;
			if( COIN_INFO.get(coin) == null ){
				coin_map = new HashMap<String, String>();
				COIN_INFO.put(coin, coin_map );
			}else{
				coin_map = COIN_INFO.get(coin);
			}
			
			coin_map.put("eid", "4");	// 4 : korbit. 
			coin_map.put("ccd", coin);
			coin_map.put("unit_cid", "9998");			
			coin_map.put("price", price);
			coin_map.put("ch", ch);
			coin_map.put("per_ch",  new BigDecimal( per_ch.replaceAll("%", "") ).divide(new BigDecimal(100), 4, BigDecimal.ROUND_DOWN ) );
			
			//log("coin_info : " + coin_map);
			
		}
		
		
		
		
	}
	
	private static String get( JsonObject jo , String memberName ){
		return JsonUtil.get(jo, memberName);
	}
	
	
	public static void main(String[] args) throws Throwable {
		// https://www.korbit.co.kr/market
		parseUrl();
	}


	
}
