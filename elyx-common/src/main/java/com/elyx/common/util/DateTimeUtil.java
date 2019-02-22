package com.elyx.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {

	public static String getCurrentDateTime() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
		Date date = new Date();
		String currentDateTime = sdf.format(date);
		return currentDateTime;
	}
}
