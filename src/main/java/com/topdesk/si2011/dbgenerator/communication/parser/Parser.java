package com.topdesk.si2011.dbgenerator.communication.parser;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.topdesk.si2011.dbgenerator.communication.MyAuthenticator;

public class Parser {
	private static final SAXBuilder parser = new SAXBuilder();
	
	private static Document parse(String url) throws ParseException {
		try {
			Authenticator.setDefault(new MyAuthenticator()); // TODO Check if this can go somewhere else
			return (Document) parser.build(new URL(url)).clone();
		} catch (JDOMException ex) {
			throw new ParseException(url);
		} catch (IOException ex) {
			throw new ParseException(url);
		}
	}
	
	public static ParsedEntry parseEntry(String url) throws ParseException {
		return new ParsedEntry(parse(url));
	}
	
	public static ParsedTable parseTable(String url) throws ParseException {
		return new ParsedTable(parse(url));
	}

	public static ParsedDefinition parseDefinition(String url) throws ParseException {
		return new ParsedDefinition(parse(url));
	}
	
	public static ParsedTablesList parseTablesList(String url) throws ParseException {
		return new ParsedTablesList(parse(url));
	}
	
	public static ParsedInsertResult parseInsertResult(HttpURLConnection connection) throws ParseException {
		 try {
//			 System.out.println(connection.getURL().getPath());
			return new ParsedInsertResult((Document) parser.build(connection.getInputStream()));
		} catch (JDOMException e) {
			throw new ParseException(connection.getURL().getPath());
		} catch (IOException e) {
			throw new ParseException(connection.getURL().getPath());
		}
	}
}
