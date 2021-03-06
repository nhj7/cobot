package nhj.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.co.cobot.bot.DATA;
import nhj.util.DateUtil;
import nhj.util.URLUtil;

public class PoloniexAPI implements Runnable{
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(PoloniexAPI.class);
	private static void log(String log){
		// log(log);
	}
	
	private String apiKey;
	private String apiSecret;
	private String tradeUrl = "https://poloniex.com/tradingApi";
	private static List TICKER_DATA = new ArrayList();
	
	
	
	private PoloniexAPI(){
		
	}
	
	public static void init() {
		try {
			
			new Thread(new PoloniexAPI()).start();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PoloniexAPI(String key, String secret){
		apiKey = key;
		apiSecret = secret;
	}
	
	private String request( String query ) throws Throwable{
		Mac shaMac = Mac.getInstance("HmacSHA512");
	    SecretKeySpec keySpec = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA512");
	    shaMac.init(keySpec);
	    final byte[] macData = shaMac.doFinal(query.getBytes());
	    String sign = Hex.encodeHexString(macData);

	    CloseableHttpClient httpClient = HttpClients.createDefault();
	    HttpPost post = new HttpPost(tradeUrl);
	    post.addHeader("Key", apiKey); 
	    post.addHeader("Sign", sign);

	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    
	    String[] paramStr = query.split("&");
	    for(int i = 0; i < paramStr.length;i++){
	    	String[] vp = paramStr[i].split("=");
	    	params.add(new BasicNameValuePair(vp[0] , vp.length > 1 ? vp[1] : ""  ));
	    }
	    post.setEntity(new UrlEncodedFormEntity(params));

	    CloseableHttpResponse response = httpClient.execute(post);
	    HttpEntity responseEntity = (HttpEntity) response.getEntity();
	    return EntityUtils.toString(responseEntity);
	}
	
	public String getNonce(){
		return String.valueOf(System.currentTimeMillis());
	}
	
	
	public Map<String, Map> returnBalances() throws Throwable {
	    
		String nonce = getNonce();
	    String queryArgs = "command=returnBalances&nonce=" + nonce;
	    String jsonStr = request(queryArgs);
	    
	    JsonParser jp = new JsonParser();
	    JsonObject jo = jp.parse(( jsonStr )).getAsJsonObject();
	    
	    Iterator it = jo.entrySet().iterator();	    
	    Map rtnMap = new HashMap();
	    
	    for(int i = 1; it.hasNext();i++){
	    	Entry en = (Entry)it.next();	    	
	    	double balance = Double.parseDouble( en.getValue().toString().replaceAll("\"", "") );	    	
	    	if( balance > 0){
	    		Map m = new HashMap();
	    		m.put("ccd", en.getKey().toString());
	    		m.put("balance", balance);
	    		rtnMap.put(en.getKey().toString(), m);
	    	}
	    }
	    return rtnMap;
	}
	
	public List returnCompleteBalances() throws Throwable {
	    
		String nonce = getNonce();
	    String queryArgs = "command=returnCompleteBalances&nonce=" + nonce;
	    String jsonStr = request(queryArgs);
	    
	    JsonParser jp = new JsonParser();
	    JsonObject jo = jp.parse(( jsonStr )).getAsJsonObject();
	    
	    //log("jo : " + jo);
	    
	    Iterator it = jo.entrySet().iterator();	    
	    List li = new ArrayList();
	    
	    for(int i = 1; it.hasNext();i++){
	    	Entry en = (Entry)it.next();
	    	
	    	
	    	String ccd = (String )en.getKey();
	    	
	    	JsonObject infoJo = (JsonObject) en.getValue();
	    	
	    	double available = Double.parseDouble( infoJo.get("available").toString().replaceAll("\"", "") );	    	
	    	double onOrders = Double.parseDouble( infoJo.get("onOrders").toString().replaceAll("\"", "") );
	    	double btcValue = Double.parseDouble( infoJo.get("btcValue").toString().replaceAll("\"", "") );
	    	
	    	if( available > 0
	    			|| onOrders > 0
	    			|| btcValue > 0
	    	){
	    		Map balanceMap = new HashMap();
	    		
	    		balanceMap.put("ccd", ccd);
	    		balanceMap.put("available", available);
	    		balanceMap.put("onOrders", onOrders);
	    		balanceMap.put("btcValue", btcValue);
	    		
	    	}
	    }
	    return li;
	}

	
	
	
	public static void main(String[] args) throws Throwable {
		
		
		//Map rtnMap = api.buy("BCN", new BigDecimal( "0.00000141") , new BigDecimal(5000) );
		
		//log("rtnMap : "+rtnMap);
		
		//Map m = api.returnBalances();
		
		PoloniexAPI api = new PoloniexAPI("", "");
		
		//List poloList = api.returnTicker();
		
		String currencyPair = "BTC_ETC";
		String _start = String.valueOf( System.currentTimeMillis() - ( 60000 * 60 * 6 ) );
		String start = _start.substring(0, _start.length() - 3 );
		//String end = String.valueOf( System.currentTimeMillis() );
		String end = "9999999999";
		String period = "900";
		
		JsonArray poloList = api.returnChartData(currencyPair, start, end, period);
		
		
		
		for(int i = 0; i < poloList.size();i++){
			
			Date d = new Date();
			d.setTime( Long.parseLong( ((JsonObject) poloList.get(i)).get("date").toString() + "000") );
			log("date : " + d);
			
			//log(new Date(  ((JsonObject) poloList.get(i)).get("date").toString()+"000" )      );
			
			
			
		}
	}
	
	
	public List returnDepositAddresses() throws Throwable {
		String nonce = getNonce();
	    
	    String queryArgs = "command=returnDepositAddresses&nonce=" + nonce;

	    String jsonStr = request(queryArgs);
	    //log(response.getStatusLine());
	    
	    JsonParser jp = new JsonParser();
	    JsonObject jo = jp.parse(( jsonStr )).getAsJsonObject();
	    
	    Iterator it = jo.entrySet().iterator();
	    List li = new ArrayList();
	    
	    for(int i = 1; it.hasNext();i++){
	    	Entry en = (Entry)it.next();
	    	/*
	    	VoProduct vo = new VoProduct();
	    	vo.name = en.getKey().toString();
	    	vo.depositAddress = en.getValue().toString().replaceAll("\"", "");
	    	li.add(vo);
	    	*/
	    	
	    }
	    return li; 
	}

	
	public List returnDepositsWithdrawals() throws Throwable {
		String nonce = getNonce();
		
	    String queryArgs = "command=returnDepositsWithdrawals&start="+ "0" +"&end=" + DateUtil.getTime();
	    queryArgs += "&nonce=" + nonce;
	    String jsonStr = request(queryArgs);
	    
	    JsonParser jp = new JsonParser();
	    JsonObject jo = jp.parse(( jsonStr )).getAsJsonObject();
	    
	    
	    List li = new ArrayList();
	    
	    JsonArray ja_deposits = jo.get("deposits").getAsJsonArray();
	    
	    ja_deposits.forEach(item ->
		    {
		    	JsonObject obj = item.getAsJsonObject();
		    	/*
		    	VoDeal vo = new VoDeal();
		    	vo.type = "deposit";
		    	vo.currency = obj.get("currency").toString();
		    	vo.address = obj.get("address").toString();
		    	vo.amount = Double.parseDouble(obj.get("amount").toString().replaceAll("\"", ""));
		    	vo.confirmations = Short.parseShort(obj.get("confirmations").toString());
		    	
		    	vo.txid = obj.get("txid").toString();
		    	vo.timestamp = Long.parseLong(obj.get("timestamp").toString());
		    	vo.status = obj.get("status").toString();
		    	
		    	li.add(vo);
		    	*/
		    	
		    	
		    }
	    );
	    
	    return li; 
	}

	
	public static List returnTicker() throws Throwable {
		
		return TICKER_DATA;
	}
	
	
	
	
	
	
	public void private_returnTicker() throws Throwable {
		
		String json_str = URLUtil.htmlToString("https://poloniex.com/public?command=returnTicker");
		
		//log(html);
		
		Gson gson = new Gson();
		
		
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json_str);
		
		JsonObject jo = je.getAsJsonObject();
		Iterator list = jo.entrySet().iterator();
		List li = new ArrayList();
		
		for(int i = 0; list.hasNext();i++){
			
			Entry en = (Entry)list.next();
			String title = (String) en.getKey();
			
			
			
			//log("title : " + title);
			
			
			String[] arrTitle = title.split("_");
			
			String unit_cid = "1";
			if( "BTC".equals(arrTitle[0]) ){
				unit_cid = "1";
			}else if( "ETH".equals(arrTitle[0])  ){
				unit_cid = "2";
			}else if( "XMR".equals(arrTitle[0])  ){
				unit_cid = "4";
			}else if( "USDT".equals(arrTitle[0])  ){
				unit_cid = "9999";
			}
			
			JsonObject tickJo = ((JsonElement)en.getValue()).getAsJsonObject();
			
			Map tickMap = new HashMap();
			
			tickMap.put("cid", 1); 	// todo need mapping . 
			tickMap.put("eid", 1);	// poloniex id
			tickMap.put("ccd", arrTitle[1]);
			
			tickMap.put("unit_cid", unit_cid);
			
			tickMap.put("price", get(tickJo, "last"));
			tickMap.put("per_ch", get(tickJo, "percentChange"));
			
			if( "9999".equals(unit_cid) && "BTC".equals(arrTitle[1]) ){
				DATA.PLNX_USD_BITCOIN = get(tickJo, "last");
			}
			
			if( "1".equals(unit_cid) && "SBD".equals(arrTitle[1]) ){
				DATA.PLNX_BTC_SBD = get(tickJo, "last");
			}
			
			//tickMap.put("base_vol", get(tickJo, "baseVolume"));
			//tickMap.put("quote_vol", get(tickJo, "quoteVolume"));
			
			//tickMap.put("is_frozen", get(tickJo, "isFrozen"));
			//tickMap.put("high24hr", get(tickJo, "high24hr"));
			//tickMap.put("low24hr", get(tickJo, "low24hr"));
			
			//log("tickMap : " + tickMap);
			//log("tickJo : " + tickJo);
			//log(arrTitle[1]);
			
			
			//String serialized = gson.toJson(tickMap);
			
			li.add( tickMap );
		}
		TICKER_DATA = null;
		TICKER_DATA = li;
	}
	
	private static long LAST_CALL_RETURN_CHART_DATA_TM = 0;
    private static long TERM_CALL_RETURN_CHART_DATA_TM = 3000;
    
	
	public synchronized static JsonArray returnChartData(String currencyPair, String start, String end, String period) throws Throwable {
		
		long term = System.currentTimeMillis() - LAST_CALL_RETURN_CHART_DATA_TM;
		if(  term < TERM_CALL_RETURN_CHART_DATA_TM ){
			long waitTm = TERM_CALL_RETURN_CHART_DATA_TM - term;
			System.out.println("[PoloniexAPI.returnChartData] "+currencyPair+" 연속된 호출 대기 : " + waitTm);
			Thread.sleep(waitTm);
		}
		
		String paramStr = "currencyPair=" + currencyPair + "&start=" 
				+ start + "&end=" + end + "&period=" + period;
		String apiUrl = "https://poloniex.com/public?command=returnChartData&" + paramStr;
		System.out.println("apiUrl : "+ apiUrl);
		String json_str = URLUtil.htmlToString(apiUrl);
		
		log(json_str);
		
		Gson gson = new Gson();
		
		
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json_str);
		
		log("je.size : " + je.getAsJsonArray().size());
		
		LAST_CALL_RETURN_CHART_DATA_TM = System.currentTimeMillis();
		
		return je.getAsJsonArray();
	}

	private String get( JsonObject jo, String key ){
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
				Thread.sleep(7300);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	

	

}
