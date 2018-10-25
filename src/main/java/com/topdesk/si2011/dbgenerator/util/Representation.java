package com.topdesk.si2011.dbgenerator.util;

import java.text.DecimalFormat;

public class Representation {
	/**
	 * Round a double to two decimals
	 * 
	 * @param d
	 *            a double
	 * @return a rounded double
	 */
	public static double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
}
