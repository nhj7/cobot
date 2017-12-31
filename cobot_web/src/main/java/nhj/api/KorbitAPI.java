package nhj.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nhj.util.JsonUtil;
import nhj.util.URLUtil;

public class KorbitAPI implements Runnable{
	private static Map<String, Map<String, String> > COIN_INFO = new HashMap();	
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(KorbitAPI.class);

	private KorbitAPI(){
		
	}
	private static KorbitAPI api;
	
	public static KorbitAPI getInstance(){
		if( api == null ){
			api = new KorbitAPI();
		}
		return api;
	}
	
	String authUrl = "https://api.korbit.co.kr/v1/oauth2/access_token";
	// "client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&username=$EMAIL&password=$PASSWORD&grant_type=password" 
	public static void init(){
		log("[KorbitAPI].start!");
		new Thread( getInstance() ).start();
	}
	
	@Override
	public void run() {
		while(true){
			
			
			try {
				//parseUrl();
				newParseUrl();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(8000);
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
				"https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=btc_krw"
				, "https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=btc_krw"
				, "https://www.korbit.co.kr/etc_market"
				, "https://www.korbit.co.kr/xrp_market"
		};
	
		for(int i = 0; i < arr_url.length;i++){
			String html = URLUtil.htmlToString(arr_url[i]);
			System.out.println("KorbitAPI : "+html);
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
			
			log("coin_info : " + coin_map);
			
		}
		
		
		
		
	}
	
	public static String htmlToString(String url) throws Throwable {

		HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
		huc.setRequestMethod("GET");
		huc.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");

		huc.addRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		huc.addRequestProperty("Accept-Encoding",
				"gzip, deflate, br");
		huc.addRequestProperty("Accept-Language",
				"ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
		huc.addRequestProperty("Cache-Control",
				"max-age=0");
		huc.addRequestProperty("Connection",
				"keep-alive");
		huc.addRequestProperty("Host",
				"api.korbit.co.kr");
		huc.addRequestProperty("Upgrade-Insecure-Requests",
				"1");
		
		huc.connect();
		
		//String cookie = huc.getHeaderField("Set-Cookie");
		
		//System.out.println("111huc.getResponseCode() : "+huc.getResponseCode());
		
		InputStream in = null;
		
		
		if( huc.getResponseCode() != 200 ){
			in = huc.getErrorStream();
		}else{
			in = huc.getInputStream();
		}
		if ("gzip".equals(huc.getContentEncoding())) {
			in = new GZIPInputStream(in);
		}
		

		BufferedReader br = new BufferedReader(new InputStreamReader(  in , "UTF-8"));

		// Map m = huc.getHeaderFields();
		//
		//
		// String cookie = "";
		// if(m.containsKey(GET)) {
		// Collection c =(Collection)m.get(GET);
		// for(Iterator i = c.iterator(); i.hasNext(); ) {
		// cookie = (String)i.next();
		// }
		// print("server response cookie:" + cookie);
		// }
		String line = null;

		StringBuilder sb = new StringBuilder();

		while ((line = br.readLine()) != null) {

			// print(line);
			sb.append(line);
		}

		br.close();

		return sb.toString();

	}
	
	public synchronized static void newParseUrl() throws Throwable{
		
		String[] arr_url = new String[]{
				"https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=btc_krw"
				, "https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=eth_krw"
				, "https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=etc_krw"
				, "https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=xrp_krw"
		};
	
		for(int i = 0; i < arr_url.length;i++){
			
			//System.out.println("url : " + arr_url[i]);
			String html = htmlToString(arr_url[i]);
			//System.out.println("KorbitAPI : "+html);
			
			
			//log("data-props : "+data.attr("data-props"));
			
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(html, JsonObject.class);
			
			String coin = arr_url[i].substring(arr_url[i].lastIndexOf("=") + 1, arr_url[i].lastIndexOf("=") + 4).toUpperCase();
			String price = get(jo, "last").toString();
			String ch = get(jo, "change").toString();
			String per_ch = get(jo, "changePercent").toString();
			
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
			//coin_map.put("per_ch",  per_ch);
			
			//log("coin_info : " + coin_map);
			
		}
		
		
		
		
	}

	
	private static String get( JsonObject jo , String memberName ){
		return JsonUtil.get(jo, memberName);
	}
	
	
	public static void main(String[] args) throws Throwable {
		// https://www.korbit.co.kr/market
		newParseUrl();
	}


	
}
