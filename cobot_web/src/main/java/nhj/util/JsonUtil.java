package nhj.util;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtil {
	private static Gson gson = new Gson();
	
	
	public static JsonObject getJsonObject(String jsonStr){
		return gson.fromJson(jsonStr, JsonObject.class);
	}
	
	public static JsonObject getJsonObject(Object obj){
		return gson.toJsonTree(obj).getAsJsonObject();
	}
	
	public static String get(JsonObject jo, String memberName){
		if( jo.get(memberName) == null ) return "";
		return jo.get(memberName).toString().replaceAll("\"", "");
	}
	
	public static JsonObject getJsonFromMap(Map<String, Object> map) {
		JsonObject jsonData = new JsonObject();
	    for (String key : map.keySet()) {
	        Object value = map.get(key);
	        if (value instanceof Map<?, ?>) {
	            value = getJsonFromMap((Map<String, Object>) value);
	            
	            jsonData.add(key, (JsonElement) value);
	            
	        }else if( value instanceof List ){
	        	//System.out.println("111");
	        	List list = (List)value;
	        	
	        	JsonArray ja = new JsonArray();
	        	
	        	for(int i = 0; i < list.size();i++){
	        		ja.add( getJsonFromMap( (Map<String, Object>) list.get(i)) );
	        		
	        		
	        	}
	        	
	        	jsonData.add(key,  ja );
	        }else{
	        	jsonData.addProperty(key, value!=null?value.toString():"");
	        }
	        
	    }
	    return jsonData;
	}

}
