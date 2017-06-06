package nhj.util;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtil {
	public static JSONObject getJson( Map m ){
		try {
			return (JSONObject) new JSONParser().parse(JSONObject.toJSONString(m));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONObject();
	}
}
