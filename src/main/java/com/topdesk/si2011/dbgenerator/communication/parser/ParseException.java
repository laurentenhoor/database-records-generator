package com.topdesk.si2011.dbgenerator.communication.parser;

public class ParseException extends Exception {
	private static final long serialVersionUID = 7777669392664853036L;
	private final String url;

	public ParseException(String url) {
		this.url = url;
	} 

	@Override
	public String getMessage() {
		return "Could not parse " + url;
	}
}