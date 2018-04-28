package com.vedanta.vpmt.web.util;

public class NumberFormatter {

	/**
	 * Formats a BigInteger to a thousand grouped String
	 * 
	 * @param number
	 * @return
	 */
	public static String formatNumber(Double number) {
		return String.format("%.10f", number);
	}

	public static String formatNumberTwoDecimal(Double number) {

		if (number == null)
			return "0.0";
		if (!(number > 0.0))
			return "0.0";
		return String.format("%.2f", number);

	}

	public static String formatNumberSixteenDigit(Double number) {
		return String.format("%.16f", number);
	}

}