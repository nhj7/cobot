package nhj.util;

import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtil {
	public static void removeChar(JsonObject jo, String removeStr ){
		for( Iterator it = jo.entrySet().iterator(); it.hasNext() ; ){
			Entry en = (Entry) it.next();
			String key = en.getKey().toString();
			JsonElement value = (JsonElement) en.getValue();
			
			
			//en.setValue(value.replaceAll( removeStr , "" ));
		}
		
		
	}
}
