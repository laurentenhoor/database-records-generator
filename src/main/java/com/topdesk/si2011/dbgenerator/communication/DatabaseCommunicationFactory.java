package com.topdesk.si2011.dbgenerator.communication;

public class DatabaseCommunicationFactory {
	public static DatabaseCommunication createRestCommunication(String restServerUrl) {
		ServerUrl.instance().setServerUrl(restServerUrl);
		return new RestCommunication();
	}
}
