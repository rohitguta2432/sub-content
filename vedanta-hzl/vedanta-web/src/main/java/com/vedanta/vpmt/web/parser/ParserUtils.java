package com.vedanta.vpmt.web.parser;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

public class ParserUtils {

	public static String getMonthString(int monthId) {
		String monthFullName = new DateFormatSymbols().getMonths()[monthId];
		return monthFullName;
	}

	public static String getSortMonthString(int monthId) {
		String monthShortName = new DateFormatSymbols().getShortMonths()[monthId];
		return monthShortName;
	}

	public static String getDateString(Date date) {
		DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static int compareDateStringToCurrentDate(String dateString) {

		try {

			if (ObjectUtils.isEmpty(dateString)) {
				return 1;
			}

			DateFormat formatter;
			Date date;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			date = formatter.parse(dateString);
			Long dateTime = date.getTime();
			Date currentDate = new Date();
			Long currentDateTime = currentDate.getTime();

			if (dateTime < currentDateTime) {
				return -1;
			} else if (dateTime > currentDateTime) {
				return 1;
			} else {
				return 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static String formatDecimalDivideByTenLackNumber(Double number) {

		Double value = number.doubleValue() / 1000000;
		value = Double.parseDouble(new DecimalFormat("##.#").format(value));
		String pattern = "#,###.#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		decimalFormat.setGroupingSize(3);
		String number1 = decimalFormat.format(value);

		return number1;
	}

	public static String formatDecimalDivideByTenCrNumber(Double number) {

		Double value = number.doubleValue() / 100000000;
		value = Double.parseDouble(new DecimalFormat("##.#").format(value));

		String pattern = "#,###.#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		decimalFormat.setGroupingSize(3);
		String number1 = decimalFormat.format(value);

		return number1;
	}

	public static String formatDecimalNumber(Double number) {
		String pattern = "#,###.#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		decimalFormat.setGroupingSize(3);
		String number1;
		if (number != null && number > 0) {
			number1 = decimalFormat.format(number);
		} else
			number1 = "0.0";
		return number1;
	}

	public static String formatDecimalNumber(String numberString) {

		try {
			if (numberString == null) {
				numberString = "0.0";
			}

			Double number = Double.parseDouble(numberString);
			if (number > 0.0) {
				String pattern = "#,###.#";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);
				decimalFormat.setGroupingSize(3);
				String number1 = decimalFormat.format(number);
				return number1;
			} else {
				return numberString;
			}
		} catch (Exception ee) {
			return "--";
		}
	}

	public static String formatDecimalNumber(Float number) {
		String pattern = "#,###.#";

		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		decimalFormat.setGroupingSize(3);
		String number1 = "";
		if (number != null)
			number1 = decimalFormat.format(number);
		return number1;
	}

	public static String digitFixSizeFormatter(Double number) {
		return String.format("%.16f", number);
	}

	public static int doubleToIneger(Double number) {

		return number.intValue();
	}

	public static String scorecardAssigneUserName(String userName, String userOffice, String userDepartment) {

		String name = userName;

		name = StringUtils.isEmpty(userOffice) ? name : name + " : " + userOffice;

		name = StringUtils.isEmpty(userDepartment) ? name : name + " : " + userDepartment;

		return name;
	}
}
