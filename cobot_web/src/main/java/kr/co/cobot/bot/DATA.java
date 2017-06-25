package kr.co.cobot.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

public class DATA {
	private static Map<String, JsonObject> EXCH_INFO = new HashMap();	// 거래소 리스트
	
	private static Map<String, Object> COIN_INFO = new HashMap(); // 거래소별 코인 리스트
	public static String COIN_INFO_STR = "";
	
	private static List Bitthumb_LIST = new ArrayList();
	
	public synchronized static void setBitthumb_LIST( List list ){
		Bitthumb_LIST = list;
	}
	
	public static List getBitthumb_LIST(){
		return Bitthumb_LIST;
	}
	
	public static <EXCH_INFO> void setCoinInfo( Map NEW_COIN_INFO ){
		synchronized (EXCH_INFO) {
			COIN_INFO = NEW_COIN_INFO;
		}
	}
	
	public static Map<String, Object> getCoinInfo(){
		return COIN_INFO;
	}
	
	public static void setExchs(Map newExchList){
		synchronized (EXCH_INFO) {
			EXCH_INFO = newExchList;
		}
	}
	public static Map getExchs(){
		return EXCH_INFO;
	}
	
	public static void main(String[] args) {
		
		Map m = new HashMap();
		m.put("1", "1");
		
		m.put("1", "2");
		
		System.out.println("m : " + m);
		
		
	}
	
}
