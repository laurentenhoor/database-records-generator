package com.topdesk.si2011.dbgenerator.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdesk.si2011.dbgenerator.communication.parser.ParseException;
import com.topdesk.si2011.dbgenerator.communication.parser.Parser;

public class RestRequests {

	public static final int HTTP_OK = 200;
	private final static Logger logger = LoggerFactory.getLogger(RestRequests.class);
	
	public static boolean doDelete(String tableName, String unid)
			throws IOException {

		URL url = new URL(ServerUrl.instance().getEntryUrl(tableName, unid));
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("DELETE");

		if (httpCon.getResponseCode() == HTTP_OK) {
			return true;
		} 
		
		logger.error("Entry " + unid + " in table "
				+ tableName + " is referenced from other tables. HTTP Response: " + httpCon.getResponseMessage());
		
		return true;
	}

	public static boolean doPut(String tableName, String doc, String unid)
			throws IOException {

		URL url = new URL(ServerUrl.instance().getEntryUrl(tableName, unid));
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
		OutputStreamWriter out = new OutputStreamWriter(
				httpCon.getOutputStream());
		out.write(doc);
		out.close();

		if (httpCon.getResponseCode() == HTTP_OK) {
//			read(httpCon);
			return true;
		}
		throw new RuntimeException("FAILED TO EDIT AN EXISTING ENTRY: "
				+ httpCon.getResponseMessage());
	}

	public static String doPost(String tableName, String doc)
			throws IOException, ParseException {

		URL url = new URL(ServerUrl.instance().getTableUrl(tableName));
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("POST");
		OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
		out.write(doc);
		out.close();
		
		if (httpCon.getResponseCode() == HTTP_OK) {
			String newUnid = Parser.parseInsertResult(httpCon).getNewUnid();
			logger.info("NEW UNID: " + newUnid + "; IN TABLE: " + tableName);
			return newUnid;
		}
		throw new RuntimeException("FAILED TO CREATE NEW ENTRY: "
				+ httpCon.getResponseMessage());

	}

	@SuppressWarnings("unused")
	private static void read(HttpURLConnection httpCon) throws IOException {
		// Get the response
		BufferedReader in = new BufferedReader(new InputStreamReader(
				httpCon.getInputStream()));
		String line;

		while ((line = in.readLine()) != null) {
			// Process line...
			System.out.println(line);
		}
		in.close();
	}
}
