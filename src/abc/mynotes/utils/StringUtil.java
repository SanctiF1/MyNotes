package abc.mynotes.utils;

import java.text.Bidi;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class StringUtil {
	private static String strMonth = "";
	private final static String SYS_DATE_FORMAT = "yyyyMMdd HH:mm:ss";
	public final static String VAL_DATE_FORMAT = "dd/MM HH:mm";

	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat(SYS_DATE_FORMAT);
		java.util.Date date = new java.util.Date();
		return format.format(date);
	}

	public static String getDateBbyMinute(int min, int sec) {
		SimpleDateFormat format = new SimpleDateFormat(SYS_DATE_FORMAT);
		java.util.Date date = new java.util.Date();
		date.setMinutes(min);
		date.setSeconds(date.getSeconds());
		return format.format(date);
	}

	public static Date getDate(String strDate) {
		SimpleDateFormat format = new SimpleDateFormat(SYS_DATE_FORMAT);
		// SimpleDateFormat returnFormat = new SimpleDateFormat(strformat);
		java.util.Date date = new java.util.Date();
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static Calendar getCalendarDate(String strDate) {
		SimpleDateFormat format = new SimpleDateFormat(SYS_DATE_FORMAT);
		// SimpleDateFormat returnFormat = new SimpleDateFormat(strformat);
		Calendar cal = Calendar.getInstance();
		java.util.Date date = new java.util.Date();
		try {
			cal.setTime(format.parse(strDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cal;
	}

	public static String getFormatDate(String strDate, String strformat) {

		SimpleDateFormat format = new SimpleDateFormat(SYS_DATE_FORMAT);
		SimpleDateFormat returnFormat = new SimpleDateFormat(strformat);
		java.util.Date date = new java.util.Date();
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnFormat.format(date);
	}

	public static String getDatebyDate(Date d) {
		SimpleDateFormat format = new SimpleDateFormat(SYS_DATE_FORMAT);
		// SimpleDateFormat returnFormat = new SimpleDateFormat(strformat);
		String date = "";
		try {
			date = format.format(d);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

}
