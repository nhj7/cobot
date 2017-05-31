package nhj.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String getTime(){
		
		Date d = new Date();
		return String.valueOf(d.getTime());
		
	}
	
	public static String getDttm(String pattern ){
		
		if( pattern == null ) return null;
		//pattern = pattern.toLowerCase();
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddkkmmss");
		
		Date d = new Date();
		String strDttm = sf.format(d);
		
		String hour = strDttm.substring(8, 10);
		if( "24".equals( hour )){
			hour = "00";
		}
		
		pattern = pattern.replaceAll("yyyy", strDttm.substring(0, 4))
				.replaceAll("MM", strDttm.substring(4, 6)).replaceAll("dd", strDttm.substring(6, 8))
				.replaceAll("kk", hour ).replaceAll("mm", strDttm.substring(10, 12))
				.replaceAll("ss", strDttm.substring(12, 14));
		
		return pattern;
		
	}
	
	public static void main(String[] args) {
		
		 
		System.out.println(getDttm("yyyy-MMdd kkmmss"));
		
		System.out.println(getDttm("kkmmss"));
		
		
		System.out.println(getDttm("yyyyMMdd hhmmss"));
		
	}

}
