package com.topdesk.si2011.dbgenerator.configurator;

public class ConfigInterpreter {
	private static GsonConfig gsonConfig = new GsonConfig();
	
	public static ConfigReader createConfigReader() {
		return gsonConfig;
	}

	public static ConfigWriter createConfigWriter() {
		return gsonConfig;
	}
}
