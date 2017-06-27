package nhj.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class StringUtil {
	public static String addComma(String str){
		DecimalFormat formatter = new DecimalFormat("#,###");
		return formatter.format( new BigDecimal(str));
	}
	
	public static String addCommaFloat(String str){
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		return formatter.format( new BigDecimal(str));
	}
	
}
