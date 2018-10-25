package com.topdesk.si2011.dbgenerator.communication;

public class ServerUrl {
	private static final String DEFAULT_TAS_SERVER = "http://pc1595/tas/rest/";

	private static ServerUrl instance;

	private String serverUrl;

	private ServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public static ServerUrl instance() {
		if (instance == null) {
			instance = new ServerUrl(DEFAULT_TAS_SERVER);
		}

		return instance;
	}

	void setServerUrl(String newServerUrl) {
		this.serverUrl = newServerUrl;
	}

	public String getServerUrl() {
		if (serverUrl == null) {
			throw new NullPointerException("No server URL set yet");
		}

		return String.format("%s", serverUrl);
	}

	public String getTableUrl(String tableName) {
		return String.format("%s%s", serverUrl, tableName);
	}

	public String getDefinitionUrl(String tableName) {
		return String.format("%s%s%s", serverUrl, tableName, "/definition");
	}

	public String getEntryUrl(String tableName, String unid) {
		return String.format("%s%s%s%s", serverUrl, tableName, "/id/", unid);
	}
	
	public String getEntryUrlIntegerId(String tableName, String unid) {
		return String.format("%s%s%s%s%s", serverUrl, tableName, "/id/", unid, "/integer");
	}
}
