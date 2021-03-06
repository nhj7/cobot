package nhj.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.google.gson.Gson;

public class StringUtil {
	
	
	public static boolean isBlank( Object obj ){
		return ( obj == null || "".equals(obj)); 
	}
	
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
	
	
	
	public static boolean hasStrings(String source, String[] arrString ){
		return hasStrings(source, arrString, true);
	}

	public static boolean hasStrings(String source, String[] arrString, boolean ignoreCase ){
		if( source == null || "".equals(source)) 
			return false;
		if( arrString == null || arrString.length == 0 ) 
			return false;
		
		if( ignoreCase )
			source = source.toLowerCase();		
		
		for(String s : arrString){
			if( ignoreCase ) 
				s = s.toLowerCase();
	        if( source.indexOf(s) > -1 ) 
	        	return true;
	    }
		return false;
	}

	public static String right(String source, int cutOff) {
		if( source == null ) return "";
		if( source.length() <= cutOff ) return source;
		if( source.indexOf("USDT") > -1 ) { cutOff++; }
		return source.substring(source.length() - cutOff , source.length());
	}
	
	public static String rightCut(String source, int i) {
		if( source == null ) return "";
		if( source.length() <= i ) return source;
		if( source.indexOf("USDT") > -1 ) { i++; }
		return source.substring(0, source.length() - i );
	}
	
}
