package kr.co.cobot.bot;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import nhj.api.PoloniexAPI;

public class ChartManager implements Runnable {
	
	public static void log(Object log){
		//System.out.println(log);
	}
	
	public static void main(String[] args) throws Throwable {
		
		String out = ChartManager.getChartData("1", "1", "ETH");
		log("out : " + out);
		
		
		String str = "{'date' : 123 , 'data' : [ 0.00009239, (0.00009139),(0.00009339), (0.00009039), 'tooltip!!', (0.00009839), (0.00009739), (0.00009850), (0.00009830),'$600K in our first year!', (0.00009239), (0.00009139),(0.00009339), (0.00009039), 'tooltip!!', (0.00009839),(0.00009739), (0.00009850), (0.00009830),'$600K in our first year!'] }";
		
		
		Gson gson = new Gson();
		
		JsonElement je = gson.fromJson(str, JsonElement.class);
		
		log(je.getAsJsonObject().get("date"));
		
	}
	
	private static Map<String, ChartManager> CHART_DATA = new HashMap();
	
	
	private String eid;
	private String unit_cid;
	private String ccd;
	private String key;
	private long LAST_CALL_TM;
	
	public static String getTickData( String eid, String unit_ccd, String ccd ) throws Throwable{
		String key = eid + "_" + unit_ccd + "_" + ccd;
		return CHART_DATA.get(key).TICK_STR;
	}
	
	public static String getChartData( String eid, String unit_ccd, String ccd ) throws Throwable{
		
		String key = eid + "_" + unit_ccd + "_" + ccd;
		
		
		
		if( CHART_DATA.containsKey(key) ){
			CHART_DATA.get(key).LAST_CALL_TM = System.currentTimeMillis();
			return CHART_DATA.get(key).CHART_STR;
		}else{
			// 최초 1회 수행
			ChartManager cm = new ChartManager( eid, unit_ccd, ccd);
			cm.LAST_CALL_TM = System.currentTimeMillis();
			
			CHART_DATA.put(key, cm);
			
			log("[CHART_BOT ADD] : key : "+key+", count : " + CHART_DATA.size() );
			
			return cm.CHART_STR;
		} 
		
		
	}
	
	private String CHART_STR;
	private String TICK_STR;
	private static String end = "9999999999";
	
	
	private ChartManager( String eid, String unit_ccd, String ccd) throws Throwable{
		this.eid = eid;
		this.unit_cid = unit_ccd;
		this.ccd = ccd.toUpperCase();
		this.key = eid + "_" + unit_ccd + "_" + ccd;
		this.CHART_STR = getChartDataStr( this );		
		
		new Thread(this).start();
	}
	
	
	
	private static String getChartDataStr( ChartManager cm ) throws Throwable{
		String unit_ccd = "BTC";
		String ccd = cm.ccd;
		
		String currencyPair = unit_ccd  + "_" + ccd;
		
		String _start = String.valueOf( System.currentTimeMillis() - ( 60000 * 61 * 6 ) );
		String start = _start.substring(0, _start.length() - 3 );
		
		String period = "900";
		
		JsonArray poloList = PoloniexAPI.returnChartData(currencyPair, start, end, period); 
		int size = poloList.size();
		
		if( size < 24){
			
			System.out.println("-----------------------------------------------------------");
			System.out.println("size is short! : " + cm.key + ", size : " + poloList.size());
			
			System.out.println(poloList);
			
			System.out.println("-----------------------------------------------------------");
			
			throw new Exception("Error!!");
		}
		
		BigDecimal maxHigh = BigDecimal.ZERO;
		BigDecimal minLow = BigDecimal.ZERO;
		
		int groupSize = 4;	// 한그룹당 사이즈 1시간 / 15분 = 4
		
		JsonArray chartArray = new JsonArray();
		JsonObject groupJo = new JsonObject();
		JsonArray groupData = new JsonArray();
		
		for(int i = 0; i < size;i++){
			
			JsonObject jo = poloList.get(i).getAsJsonObject();		
			
			BigDecimal newHigh = new BigDecimal(jo.get("high").toString()).setScale(8);
			BigDecimal newLow = new BigDecimal(jo.get("low").toString()).setScale(8);
			if( i == 0 ) { minLow = newLow; }
			
			BigDecimal open = new BigDecimal(jo.get("open").toString()).setScale(8);
			BigDecimal close = new BigDecimal(jo.get("close").toString()).setScale(8);
			groupData.add(newLow);
			groupData.add(open);
			groupData.add(close);
			groupData.add(newHigh);
			groupData.add("Date : "+jo.get("date")+"\nOpen : " + open + "\nLow : " + newLow + "\nHigh : " + newHigh + "\nClose : " + close);
			
			maxHigh = maxHigh.compareTo( newHigh ) == -1  ? newHigh : maxHigh;
			minLow = minLow.compareTo(newLow) == -1 ? minLow : newLow;
			
			Date d = new Date( Long.parseLong(jo.get("date").toString() + "000") );
			
			if( i != 0 && (i+1) % groupSize == 0 ){
				
				groupJo.addProperty("date", ((JsonObject) poloList.get(i-1)).get("date").toString() );
				groupJo.add("data", groupData);
				
				chartArray.add(groupJo);
				
				groupJo = new JsonObject();
				groupData = new JsonArray();
			}
			
			//log("date : "+d + ", maxHigh : " + maxHigh + ", newHigh : " + newHigh + ", minLow : " + minLow );
			
		}
		
		BigDecimal diff = maxHigh.subtract(minLow);
		BigDecimal buff = diff.multiply(new BigDecimal(0.1));
		
		maxHigh = maxHigh.add(buff);
		maxHigh = maxHigh.setScale(8, BigDecimal.ROUND_DOWN);
		
		minLow = minLow.subtract(buff);
		minLow = minLow.setScale(8, BigDecimal.ROUND_DOWN);
		
		diff = maxHigh.subtract(minLow);
		
		
		
		BigDecimal term = diff.divide(new BigDecimal(5), 8, BigDecimal.ROUND_DOWN);
		
		
		
		//log("maxHigh : " + maxHigh );
		//log("minLow : " + minLow );
		//log("term : " + term );
		
		BigDecimal term1 = minLow.add(term);
		BigDecimal term2 = term1.add(term);
		BigDecimal term3 = term2.add(term);
		BigDecimal term4 = term3.add(term);
		BigDecimal term5 = term4.add(term);
		
		
		JsonArray tickJa = new JsonArray();
		tickJa.add(minLow);
		tickJa.add(term1);
		tickJa.add(term2);
		tickJa.add(term3);
		tickJa.add(term4);
		tickJa.add(term5);
		
		if( cm.mainData == null ){
			cm.mainData = new JsonObject();
		}
		
		cm.mainData.add("chartArray", chartArray);
		cm.mainData.add("tick", tickJa);
		
		//log("t1 : " + minLow );
		//log("t2 : " + minLow.add(term) );
		//log("t3 : " + minLow.add(term).add(term) );
		//log("t4 : " + minLow.add(term).add(term).add(term) );
		//log("t5 : " + minLow.add(term).add(term).add(term).add(term) );
		//log("t6 : " + maxHigh );
		
		//log("chartArray :" + chartArray );
		cm.CHART_STR = chartArray.toString();
		cm.TICK_STR = tickJa.toString();
		return cm.CHART_STR;
	}
	
	private JsonObject mainData;
	
	
	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		
		int LAST_DATA_UPDATE_INTERVAL = 2000;
		int CHART_DATA_UPDATE_INTERVAL = 1000 * 60 * 1;
		int CAL_INTERVAL = 0;
		
		int CHART_BOT_DOWN_TERM = 1000 * 60;	// 1분간 요청이 없는 쓰레드는 종료한다.
		while(true){
			
			try {
				Thread.sleep( LAST_DATA_UPDATE_INTERVAL );// 2초 쉬어간다.
				
				CAL_INTERVAL += LAST_DATA_UPDATE_INTERVAL;
				
				JsonArray chartArray = mainData.get("chartArray").getAsJsonArray();
				
				List eidList = (List)DATA.getCoinInfo().get("eid_" + this.eid );
				
				log("[eidList] : "+eidList);
				int size = eidList.size();
				for(int i = 0; i < size;i++){
					Map coinMap = (Map)eidList.get(i);
					if( coinMap.get("unit_cid").toString().equals(this.unit_cid) && this.ccd.equals(coinMap.get("ccd").toString())) {
						JsonArray chartLast = chartArray.get(chartArray.size()-1).getAsJsonObject().get("data").getAsJsonArray();
						
						
						int cSize = chartLast.size();
						
						BigDecimal newHigh = chartLast.get( cSize - 2 ).getAsBigDecimal();
						BigDecimal close = chartLast.get( cSize - 3 ).getAsBigDecimal();
						BigDecimal open = chartLast.get( cSize - 4 ).getAsBigDecimal();
						BigDecimal newMin = chartLast.get( cSize - 5 ).getAsBigDecimal();
						
						BigDecimal price = new BigDecimal(coinMap.get("price").toString());
						
						log("[chart data(before)] : open" + open + ", newMin : " + newMin + " , newHigh : " + newHigh + ", close : " + close + ", cur_price : " +  coinMap.get("price") );
						
						close = price;
						if( newHigh.compareTo(price) < 0){
							newHigh = price;
						}
						
						if( newMin.compareTo(price) > 0 ){
							newMin = price;
						}
						
						chartLast.set(cSize - 1, new JsonPrimitive("Open : " + open + "\nLow : " + newMin + "\nHigh : " + newHigh + "\nClose : " + close) );
						chartLast.set(cSize - 2, new JsonPrimitive(newHigh));
						chartLast.set(cSize - 3, new JsonPrimitive(close));
						chartLast.set(cSize - 5, new JsonPrimitive(newMin));
						
							
						log("[chart data(after)] : open" + open + ", newMin : " + newMin + " , newHigh : " + newHigh + ", close : " + close + ", cur_price : " +  coinMap.get("price") );
						
						this.CHART_STR = chartArray.toString();
					}
					
				}
				
				if( CAL_INTERVAL > CHART_DATA_UPDATE_INTERVAL ){
					CAL_INTERVAL = 0;
					
					if( System.currentTimeMillis() - this.LAST_CALL_TM > CHART_BOT_DOWN_TERM ){
						System.out.println("[ChartMaanager] Threads : " + CHART_DATA.size() + ", Curren Thread Close : " + this.key);
						CHART_DATA.remove(key);
						break;
					}else{
						System.out.println("[ChartMaanager] Threads : " + CHART_DATA.size() + ", curren update data : " + this.key);
						getChartDataStr(this);
					}
					
					
					
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
		}
	}

}
