package nhj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String getDateToString(Date date, String pattern) throws Throwable{
		SimpleDateFormat transFormat = new SimpleDateFormat(pattern);
		String str = transFormat.format(date);

		
		return str;
	}
	
	// yyyy-MM-dd HH:mm:ss
	public static Date getStringToDate(String str, String pattern) throws Throwable{
		SimpleDateFormat transFormat = new SimpleDateFormat(pattern);
		Date d = transFormat.parse(str);

		
		return d;
		
		/*
		int yyyy = Integer.parseInt(str.substring( pattern.indexOf("yyyy"), pattern.indexOf("yyyy") + 4 ));
		int mm = Integer.parseInt(str.substring( pattern.indexOf("mm"), pattern.indexOf("mm") + 2 ));
		int dd = Integer.parseInt(str.substring( pattern.indexOf("dd"), pattern.indexOf("dd") + 2 ));
		
		int hh = 0;
		if( pattern.indexOf("hh") > -1 ){
			hh = Integer.parseInt(str.substring( pattern.indexOf("hh"), pattern.indexOf("hh") + 2 ));
		}
		int mi = 0;
		if( pattern.indexOf("mi") > -1 ){
			mi = Integer.parseInt(str.substring( pattern.indexOf("mi"), pattern.indexOf("mi") + 2 ));
		}
		int ss = 0;
		if( pattern.indexOf("ss") > -1 ){
			ss = Integer.parseInt(str.substring( pattern.indexOf("ss"), pattern.indexOf("ss") + 2 ));
		}
		
		Date d = new Date();
		d.setYear(yyyy-1900);
		d.setMonth(mm-1);
		d.setDate(dd);
		d.setHours(hh);
		d.setMinutes(mi);
		//d.setSeconds(ss);
		 * 
		 */
		
		
	}
	
	public static String getCurDate(String pattern) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		String formattedDate = sdf.format(date);

		return formattedDate;
	}
	public static String getDateString(Date date , String pattern) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		String formattedDate = sdf.format(date);

		return formattedDate;
	}

	public static void main(String[] args) {

		System.out.println(getCurDate("YYYY-MM"));

	}

	public static String getTime() {

		Date d = new Date();
		return String.valueOf(d.getTime());

	}

}
