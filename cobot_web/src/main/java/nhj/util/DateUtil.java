package nhj.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	
	
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
