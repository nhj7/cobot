package nhj.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.google.gson.Gson;

public class StringUtil {
	public static String addComma(String str){
		DecimalFormat formatter = new DecimalFormat("#,###");
		return formatter.format( new BigDecimal(str));
	}
	
	public static String addCommaFloat(String str){
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		return formatter.format( new BigDecimal(str));
	}
	
	public static String getObjToStr(Object o) throws Throwable{
		//Map<String, String> objectAsMap = BeanUtils.describe(o);
		//return objectAsMap.toString();
		//return ToStringBuilder.reflectionToString(o);
		return new Gson().toJson(o);
		
	}
	
}
