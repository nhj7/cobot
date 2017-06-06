package nhj.api;

import java.util.ArrayList;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nhj.util.DateUtil;
import nhj.util.URLUtil;

public class PoloniexAPI {
	
	
	
	private String apiKey;
	private String apiSecret;
	private String tradeUrl = "https://poloniex.com/tradingApi";
	
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
	    
	    //System.out.println("jo : " + jo);
	    
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
		
		//System.out.println("rtnMap : "+rtnMap);
		
		//Map m = api.returnBalances();
		
		PoloniexAPI api = new PoloniexAPI("", "");
		
		List poloList = api.returnTicker();
		
		System.out.println(poloList);
	}
	
	
	public List returnDepositAddresses() throws Throwable {
		String nonce = getNonce();
	    
	    String queryArgs = "command=returnDepositAddresses&nonce=" + nonce;

	    String jsonStr = request(queryArgs);
	    //System.out.println(response.getStatusLine());
	    
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

	
	public List returnTicker() throws Throwable {
		
		String json_str = URLUtil.htmlToString("https://poloniex.com/public?command=returnTicker");
		
		//System.out.println(html);
		
		Gson gson = new Gson();
		
		
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json_str);
		
		JsonObject jo = je.getAsJsonObject();
		Iterator list = jo.entrySet().iterator();
		List li = new ArrayList();
		
		for(int i = 0; list.hasNext();i++){
			
			Entry en = (Entry)list.next();
			String title = (String) en.getKey();
			
			
			
			//System.out.println("title : " + title);
			
			
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
			
			//tickMap.put("base_vol", get(tickJo, "baseVolume"));
			//tickMap.put("quote_vol", get(tickJo, "quoteVolume"));
			
			//tickMap.put("is_frozen", get(tickJo, "isFrozen"));
			//tickMap.put("high24hr", get(tickJo, "high24hr"));
			//tickMap.put("low24hr", get(tickJo, "low24hr"));
			
			//System.out.println("tickMap : " + tickMap);
			//System.out.println("tickJo : " + tickJo);
			//System.out.println(arrTitle[1]);
			
			
			//String serialized = gson.toJson(tickMap);
			
			li.add( tickMap );
		}
		return li;
	}

	private String get( JsonObject jo, String key ){
		return jo.get(key).toString().replaceAll("\"", "");
	}

	
	public void buy() throws Throwable {
		// TODO Auto-generated method stub
		
	}

	

	

}
