package com.topdesk.si2011.dbgenerator.util;

public class StringConversions {

	public static String urlToName(String url) {
		return url.split("/")[url.split("/").length - 1];
	}
	
	public static String stringUnidConversion(String url){
		return urlToName(url);
	}
	
	public static String integerUnidConversion(String url) {
		return url.split("/")[url.split("/").length - 2];
	}


}
