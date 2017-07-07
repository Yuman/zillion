package com.yu.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Calendar;


public class Iso8601Util {
	private static final String Iso8601Format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private TimeZone tz = TimeZone.getTimeZone("UTC");
	private  DateFormat df = new SimpleDateFormat(Iso8601Format);

	public Iso8601Util() {
		df.setTimeZone(tz);
	}

	public  String now() {
		return df.format(new Date());
	}
	
	public String format(long time){
		return df.format(new Date(time));
	}
	
	public String back(int years){
		Calendar cal = new GregorianCalendar();
		cal.roll(Calendar.YEAR, years);
		return df.format(cal.getTime());
	}
	
	public static long parseTime(String dateTime) throws ParseException{
		return OffsetDateTime.parse(dateTime).toEpochSecond()*1000;

	}
	
}
