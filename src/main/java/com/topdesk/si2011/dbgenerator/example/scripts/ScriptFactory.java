package com.topdesk.si2011.dbgenerator.example.scripts;

import com.topdesk.si2011.dbgenerator.core.WorkBench;

public class ScriptFactory {

	public static ConfigurationScript createUserScript(String scriptClass,
			WorkBench core) {
		ClassLoader loader = ConfigurationScript.class.getClassLoader();
		ConfigurationScript newInstance = null;

		try {
			newInstance = (ConfigurationScript) loader.loadClass(scriptClass)
					.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return newInstance;
	}

}
