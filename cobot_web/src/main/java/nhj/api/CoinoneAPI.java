package nhj.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.socket.client.IO;
import io.socket.client.IO.Options;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import nhj.util.URLUtil;

public class CoinoneAPI implements Runnable {
	
	private static Map<String, Map<String, String>> COIN_INFO = new HashMap();
	
	private static WebClient webClient;
	private static WebConnectionWrapper wc;
    
    private static boolean COINONE_CHART_IS_READY = false;
    static{
		
	}
    
    private static long LAST_CALL_RETURN_CHART_DATA_TM = 0;
    private static long TERM_CALL_RETURN_CHART_DATA_TM = 3000;
    
    public synchronized static JsonArray returnChartData(String currencyPair, String start, String end, String period) throws Throwable {
    	
    	
    	if( COINONE_CHART_IS_READY ){
    		
    		long term = System.currentTimeMillis() - LAST_CALL_RETURN_CHART_DATA_TM;
    		if(  term < TERM_CALL_RETURN_CHART_DATA_TM ){
    			long waitTm = TERM_CALL_RETURN_CHART_DATA_TM - term;
    			System.out.println("[CoinoneAPI.returnChartData] "+currencyPair+" 연속된 호출 대기 : " + waitTm);
    			Thread.sleep(waitTm);
    		}
    		
    		currencyPair = currencyPair.toLowerCase();
    		if( currencyPair.equals("btc")){
    			currencyPair = "";
    		}
    		
    		String apiUrl = "https://coinone.co.kr/chart/olhc/?site=coinone" + currencyPair + "&type=15m";

    		WebRequest rq = new WebRequest( new URL(apiUrl) );
    		rq.setAdditionalHeader("accept", "application/json, text/javascript, */*; q=0.01");
    		rq.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
    		WebResponse rs = wc.getResponse(rq);
    		
    		if( rs.getStatusCode() != 200){
    			
    			System.out.println("CoinoneAPI.returnChartData rtnStatusCode : " + rs.getStatusCode());
    			COINONE_CHART_IS_READY = false;
    			initChart();
    			return new JsonArray();
    		}
    		
    		String jsonString = rs.getContentAsString();
    		Gson json = new Gson();
    		JsonObject jo = json.fromJson(jsonString, JsonObject.class);
    		JsonArray ja = jo.get("data").getAsJsonArray();
    		
    		JsonArray newJa = new JsonArray();
    		
    		int newSize = 24;
    		for(int i = 0; i < newSize;i++){
    			JsonObject subJo = ja.get( ja.size() - newSize + i ).getAsJsonObject();
    			
    			JsonObject newJo = new JsonObject();
    			newJo.addProperty("date", new BigDecimal(get(subJo, "DT")));
    			newJo.addProperty("high", new BigDecimal(get(subJo, "High").toString()));
    			newJo.addProperty("low", new BigDecimal(get(subJo, "Low").toString()));
    			newJo.addProperty("open", new BigDecimal(get(subJo, "Open").toString()));
    			newJo.addProperty("close", new BigDecimal(get(subJo, "Close").toString()));
    			
    			newJa.add(newJo);
    		}
    		
    		LAST_CALL_RETURN_CHART_DATA_TM = System.currentTimeMillis();
    		
    		return newJa;
    		
    	}else{
    		return new JsonArray();
    	}
    }
    
	private CoinoneAPI() {

	}

	private static CoinoneAPI api;

	public static CoinoneAPI getInstance() {
		if (api == null) {
			api = new CoinoneAPI();
		}
		return api;
	}

	private static void log(Object log) {
		System.out.println("[CoinoneAPI.log]"+log);
	}

	

	

	public static void init() {
		System.out.println("[CoinoneAPI].start!");
		// new Thread( getInstance() ).start();
		try {

			// Ticker Data Register Start
			{
				Options opts = new Options();
				opts.transports = new String[] { "websocket", "polling" };
				Socket socket = IO.socket("https://push.coinone.co.kr/ticker", opts);
				System.out.println("socket : " + socket);
				// {transports:['websocket','polling']}
				socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

					@Override
					public void call(Object... args) {
						// socket.emit("foo", "hi");
						// socket.disconnect();

						for (int i = 0; i < args.length; i++) {
							System.out.println("CoinoneAPI.EVENT_CONNECT : " + args[i]);
						}

					}

				}).on("update", new Emitter.Listener() {

					@Override
					public void call(Object... args) {

						// System.out.println(DateUtil.getCurDate("[yyyy-MM-dd
						// kk:mm:ss]") + " update : "+args[0]);

						setPrice(args[0].toString());
					}

				}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

					@Override
					public void call(Object... args) {
						System.out.println("CoinoneAPI.EVENT_DISCONNECT : " + args[0]);
						socket.connect();
					}

				});
				socket.connect();
			}
			// Ticker Data Register End

			// virtual selenium chrome driver start
			{

			}
			// virtual selenium chrome driver End

			// Thread Start !!
			// 1. get usd every 3 second
			new Thread(getInstance()).start();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void initChart(){
		// Chart Init
		{
			try {
				webClient = new WebClient(BrowserVersion.CHROME);
				wc = new WebConnectionWrapper(webClient) {
			        public WebResponse getResponse(WebRequest request) throws IOException {
			            WebResponse response = super.getResponse(request);
			            /*
			            if (request.getUrl().toExternalForm().startsWith("https://coinone.co.kr/chart/olhc/")) {
			                String content = response.getContentAsString();

			                //change content

			                WebResponseData data = new WebResponseData(content.getBytes(),
			                        response.getStatusCode(), response.getStatusMessage(), response.getResponseHeaders());
			                response = new WebResponse(data, request, response.getLoadTime());
			            }
			            */
			            
			            return response;
			        }
			    };
			    final Logger logger = LoggerFactory.getLogger("com.gargoylesoftware.htmlunit");
			    
			    System.out.println("logger : "+logger);
				
				
				System.out.println("[CoinoneAPI]Please wait 20 seconds to pass CloudFlare DDOS Check...");		
				webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
				String url = "https://doc.coinone.co.kr/";
				HtmlPage htmlPage = webClient.getPage(url);
				webClient.waitForBackgroundJavaScript(10_000);
				CookieManager cm = webClient.getCookieManager();
				
				//System.out.println("cookies1 : " + cm.getCookies());
				//System.out.println(htmlPage.asText());
				// Thread.sleep(10000);
				
				htmlPage = webClient.getPage(url);
				webClient.waitForBackgroundJavaScript(10_000);
				//System.out.println(htmlPage.asText());

				cm = webClient.getCookieManager();

				Set cookies = cm.getCookies();

				//System.out.println("cookies2 : " + cm.getCookies());

				CookieStore cs = new BasicCookieStore();

				Map header = new HashMap();
				com.gargoylesoftware.htmlunit.util.Cookie cookie = null;
				for (java.util.Iterator it = cookies.iterator(); it.hasNext();) {
					cookie = (com.gargoylesoftware.htmlunit.util.Cookie) it.next();
					//System.out.println(cookie);
					// cf_clearance

					//System.out.println("getName() : " + cookie.getName() + ", " + cookie.getValue());
					header.put(cookie.getName(), cookie.getValue());
					org.apache.http.impl.cookie.BasicClientCookie aCookie = new org.apache.http.impl.cookie.BasicClientCookie(
							cookie.getName(), cookie.getValue());
					aCookie.setDomain(".coinone.co.kr");

					cs.addCookie(aCookie);

				}
				
				System.out.println("[CoinoneAPI] CloudFlare DDOS ByPass Complete!!!");
				
				COINONE_CHART_IS_READY = true;
				
				//returnChartData( currencyPair, start, end, period );
				/*
				JsonArray ja = returnChartData( "", "", "", "" );
				
				for(int i = 0; i < ja.size();i++){
					JsonObject jo = ja.get(i).getAsJsonObject();
					Date d = new Date( Long.parseLong(jo.get("date").toString()) );
					System.out.println(DateUtil.getDateString(d, "yyyy-MM-dd kk:mm:ss"));
				}
				*/
				
				/*
				
				log("wait 5 sec");
				Thread.sleep(5000);
				
				rs = wc.getResponse(rq);
				//webClient.waitForBackgroundJavaScript(10_000);
				System.out.println(rs.getContentAsString());
				
				*/
				
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}

	public synchronized static void setPrice(String json_str) {
		Gson gson = new Gson();
		JsonObject jo = gson.fromJson(json_str, JsonObject.class);

		for (Iterator it = jo.entrySet().iterator(); it.hasNext();) {

			Entry en = (Entry) it.next();
			String title = en.getKey().toString();
			String value = en.getValue().toString().replaceAll("\"", "");
			String[] arrTitle = title.split("_");

			String currency = arrTitle[0].toUpperCase();

			Map coin_info;
			if (COIN_INFO.get(currency) == null) {
				coin_info = new HashMap<String, String>();
				COIN_INFO.put(currency, coin_info);
			} else {
				coin_info = COIN_INFO.get(currency);
			}

			coin_info.put(title.replaceAll(arrTitle[0] + "_", ""), value);

		}

		// System.out.println("[WS_COIN_INFO] : "+WS_COIN_INFO);

	}

	public static String getPerKrw() {
		return per_krw;
	}

	private static String per_krw = "";

	@Override
	public void run() {
		if( !COINONE_CHART_IS_READY ){
			initChart();
		}

		while (true) {

			try {
				per_krw = getUSDCurrency();
				Thread.sleep(3000);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	public static List returnTicker() throws Throwable {

		List list = new ArrayList();

		for (Iterator it = COIN_INFO.keySet().iterator(); it.hasNext();) {

			String ccd = it.next().toString();

			Map tickMap = new HashMap();

			tickMap.put("cid", 1); // todo need mapping .
			tickMap.put("eid", 3); // Coinone id
			tickMap.put("ccd", ccd);

			tickMap.put("unit_cid", "9998"); // KRW

			BigDecimal last = new BigDecimal(COIN_INFO.get(ccd).get("price"));

			BigDecimal first = BigDecimal.ZERO;
			if( COIN_INFO.get(ccd).get("yesterday_price") == null){
				first = last;
			} else{
				first = new BigDecimal(COIN_INFO.get(ccd).get("yesterday_price"));
			}
			

			BigDecimal ch = last.subtract(first);

			BigDecimal per_ch = ch.divide(first, 4, BigDecimal.ROUND_DOWN);
			tickMap.put("first", first);
			tickMap.put("ch", last.subtract(first));
			tickMap.put("price", last);
			tickMap.put("per_ch", per_ch);

			list.add(tickMap);

		}
		return list;
	}

	private static String getUSDCurrency() throws Throwable {

		String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%3D%22USDKRW%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
		String rtnJsonStr = URLUtil.htmlToString(url);

		//System.out.println("[rtnJsonStr] : "+rtnJsonStr);
		
		JsonObject jo = new Gson().fromJson(rtnJsonStr, JsonObject.class);
		String rate = "1128.5";
		
		try{
			rate = get(jo.get("query").getAsJsonObject().get("results").getAsJsonObject().get("rate").getAsJsonObject(), "Rate");
		}catch(Exception e){
			
		}
		
		
		return rate;

	}

	public static List returnTickerBak() throws Throwable {

		String url = "https://api.coinone.co.kr/ticker/?format=json&currency=all";
		String rtnJsonStr = URLUtil.htmlToString(url);

		JsonObject jo = new Gson().fromJson(rtnJsonStr, JsonObject.class);

		List list = new ArrayList();

		if ("0".equals(get(jo, ("errorCode")))) {
			jo.remove("timestamp");
			jo.remove("errorCode");
			jo.remove("result");

			for (Iterator it = jo.entrySet().iterator(); it.hasNext();) {
				Entry en = (Entry) it.next();
				String currency = en.getKey().toString().toUpperCase();

				JsonObject tickJo = ((JsonElement) en.getValue()).getAsJsonObject();

				System.out.println("tickJo : " + tickJo);

				Map tickMap = new HashMap();

				tickMap.put("cid", 1); // todo need mapping .
				tickMap.put("eid", 3); // Coinone id
				tickMap.put("ccd", get(tickJo, "currency").toUpperCase());

				tickMap.put("unit_cid", "9998"); // KRW

				BigDecimal last = new BigDecimal(get(tickJo, "last"));

				BigDecimal first = new BigDecimal(get(tickJo, "first"));

				BigDecimal ch = last.subtract(first);

				BigDecimal per_ch = ch.divide(first, 4, BigDecimal.ROUND_DOWN);
				tickMap.put("first", first);
				tickMap.put("ch", last.subtract(first));
				tickMap.put("price", last);
				tickMap.put("per_ch", per_ch);

				list.add(tickMap);
			}
		}
		System.out.println("list : " + list);

		return list;

	}

	private static String get(JsonObject jo, String key) {
		return jo.get(key).toString().replaceAll("\"", "");
	}
	
	public static void main(String[] args) throws Throwable {
		CoinoneAPI api = new CoinoneAPI();
		
		String usd = api.getUSDCurrency();
		
		
		if(true)return;
		
		api.init();
		new Thread(api).start();
		
		while(true){
			JsonArray ja = api.returnChartData("", "", "", "");
			
			System.out.println("ja : " + ja);
			
			Thread.sleep(3000);
		}
		
		
		
		
	}

	
}

