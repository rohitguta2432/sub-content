package com.vedanta.vpmt.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.vedanta.vpmt.exception.VedantaException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtil {

	public static int getWeekOfMoth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	public static int getWeekOfYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getMonthOfYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH);
	}

	public static int getPreviouseMonthOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);

		return cal.get(Calendar.MONTH);
	}

	public static int getYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static Date getDateFromString(String dateString) {

		if (StringUtils.isEmpty(dateString)) {
			log.info("Error while converting String into Date.");
			throw new VedantaException("Error while converting String into Date.");
		}

		try {
			DateFormat formatter;
			Date date;
			formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			date = formatter.parse(dateString);

			return date;
		} catch (VedantaException | ParseException e) {
			log.info("Error while converting String into Date.");
			throw new VedantaException("Error while converting String into Date.");
		}
	}

	public static Date getDateFromStringForEdit(String dateString) {

		// String dateString = "Tue Sep 12 14:15:08 IST 2017"; Sample format

		if (StringUtils.isEmpty(dateString)) {
			log.info("Error while converting String into Date.");
		}

		try {
			DateFormat formatter;
			Date date;
			formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			date = formatter.parse(getDateString(dateString));

			return date;
		} catch (Exception e) {
			log.info("Error while converting String into Date.");
		}
		return null;
	}

	public static String getDateString(String string) {

		/*
		 * Format should be String string = "Tue Sep 12 14:15:08 IST 2017";
		 */
		try {

			String year = string.substring((string.length() - 4));
			String month = string.substring(4, 7);
			String day = string.substring(8, 10);
			String time = string.substring(11, 19);
			return day + "-" + month + "-" + year + " " + time;
		} catch (VedantaException e) {
			log.info("Error while converting String(Tue Sep 12 14:15:08 IST 2017) into DateString.");
			throw new VedantaException("Error while converting String(Tue Sep 12 14:15:08 IST 2017) into DateString.");
		}
	}

	public static int checkStartDate(Calendar cal1, Calendar cal2) {
		return cal1.compareTo(cal2);
	}

	public static boolean isScoreCardPopulate(Calendar populateDate, Calendar contractExpirationDate) {
		if (populateDate.before(contractExpirationDate)
				|| (populateDate.get(Calendar.MONTH) == contractExpirationDate.get(Calendar.MONTH))) {
			return true;
		}
		return false;
	}
}
