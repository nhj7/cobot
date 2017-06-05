package kr.co.cobot.bot;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class DATA {
	private static Map<String, JSONObject> EXCH_INFO;	// 거래소 리스트
	
	public void setExchs(Map newExchList){
		synchronized (EXCH_INFO) {
			EXCH_INFO = newExchList;
		}
	}
	public Map getExchs(){
		return EXCH_INFO;
	}
	
	public static void main(String[] args) {
		
		Map m = new HashMap();
		m.put("1", "1");
		
		m.put("1", "2");
		
		System.out.println("m : " + m);
		
		
	}
	
}
