package exam_cobot;

import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nhj.util.URLUtil;

public class test2 {
	public static void main(String[] args) throws Throwable {
		
		
		String html = URLUtil.htmlToString("https://poloniex.com/public?command=returnTicker");
		
		System.out.println(html);
		
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(html);
		
		System.out.println(je);
		
		JsonObject jo = je.getAsJsonObject();
		
		Iterator list = jo.entrySet().iterator();
		
		for(int i = 0; list.hasNext();i++){
			
			Entry en = (Entry)list.next();
			String title = (String) en.getKey();
			
			System.out.println("title : " + title);
			System.out.println("value : " + ((JsonElement)en.getValue()).getAsJsonObject());
			
		}
		
		
	}
}
