package com.topdesk.si2011.dbgenerator;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunication;
import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunicationFactory;
import com.topdesk.si2011.dbgenerator.core.GeneratorCore;
import com.topdesk.si2011.dbgenerator.example.scripts.ConfigurationScript;
import com.topdesk.si2011.dbgenerator.ui.GeneratorUI;

/**
 * Controller class with functionalities to run a generator cycle.
 */
public class Controller {
	private static final Logger logger = LoggerFactory
			.getLogger(Controller.class);

	/**
	 * Run the database generator with a user-defined script file. Only REST
	 * database communication is supported.
	 * 
	 * @param script
	 *            User-defined script file
	 * @param serverUrl
	 *            REST server url
	 */
	public static void runWithScript(ConfigurationScript script,
			String serverUrl, final String userName, final String password) {

		logger.info("Connecting to " + serverUrl);
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password
						.toCharArray());
			}
		});
		DatabaseCommunication dbComm = DatabaseCommunicationFactory
				.createRestCommunication(serverUrl);

		logger.info("Extracting database structure");
		GeneratorCore core = new GeneratorCore(dbComm);
		core.extractStructureFromDatabase();
		core.resetToDefault();
		core.saveStructureToFile("structures/rest_structure/structure.json");

		logger.info("Using script to configuration generation settings");
		script.run(core.getCurrentStructure(), core);

		logger.info("Starting to generate entries");
		core.generateDatabase();

		logger.info("Finished database generation");
	}

	/**
	 * Run the database generator with a user-defined script file and a custom
	 * structure. Only REST database communication is supported.
	 * 
	 * @param script
	 *            User-defined script file
	 * @param serverUrl
	 *            REST server url
	 * @param jsonFile
	 */
	public static void runWithScript(ConfigurationScript script,
			String serverUrl, final String userName, final String password,
			String jsonFile) {

		logger.info("Connecting to " + serverUrl);
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password
						.toCharArray());
			}
		});
		DatabaseCommunication dbComm = DatabaseCommunicationFactory
				.createRestCommunication(serverUrl);

		logger.info("Extracting database structure");
		GeneratorCore core = new GeneratorCore(dbComm);
		core.loadStructureFromFile(jsonFile);

		logger.info("Using script to configuration generation settings");
		script.run(core.getCurrentStructure(), core);

		logger.info("Starting to generate entries");
		core.generateDatabase();

		logger.info("Finished database generation");
	}

	/**
	 * Run the structure browser for structural analysis.
	 * 
	 * @param serverUrl
	 *            REST server url
	 */
	public static void runStructureBrowser(String serverUrl,
			final String userName, final String password) {
		logger.info("Connecting to " + serverUrl);
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password
						.toCharArray());
			}
		});
		DatabaseCommunication dbComm = DatabaseCommunicationFactory
				.createRestCommunication(serverUrl);

		logger.info("Starting core application");
		GeneratorCore core = new GeneratorCore(dbComm);

		logger.info("Open structure browser");
		@SuppressWarnings("unused")
		GeneratorUI ui = new GeneratorUI(core, core);
	}

	public static void runStructureBrowser(String serverUrl,
			final String userName, final String password, String jsonStructureUrl) {
		logger.info("Connecting to " + serverUrl);
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password
						.toCharArray());
			}
		});
		DatabaseCommunication dbComm = DatabaseCommunicationFactory
		.createRestCommunication(serverUrl);
		
		logger.info("Extracting database structure");
		GeneratorCore core = new GeneratorCore(dbComm);
		core.loadStructureFromFile(jsonStructureUrl);
		
		logger.info("Open structure browser");
		@SuppressWarnings("unused")
		GeneratorUI ui = new GeneratorUI(core, core);
	}
}
