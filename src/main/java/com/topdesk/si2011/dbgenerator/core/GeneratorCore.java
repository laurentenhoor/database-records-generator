package com.topdesk.si2011.dbgenerator.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunication;
import com.topdesk.si2011.dbgenerator.configurator.ConfigInterpreter;
import com.topdesk.si2011.dbgenerator.configurator.ConfigReader;
import com.topdesk.si2011.dbgenerator.configurator.ConfigWriter;
import com.topdesk.si2011.dbgenerator.configurator.GsonConfig;
import com.topdesk.si2011.dbgenerator.configureddbstructure.ConfiguredDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;
import com.topdesk.si2011.dbgenerator.dbstructure.storage.JSonStructureInterpreter;
import com.topdesk.si2011.dbgenerator.generator.ColumnGenerator;
import com.topdesk.si2011.dbgenerator.generator.DatabaseGenerator;
import com.topdesk.si2011.dbgenerator.generator.ScheduledDatabaseGenerator;

public class GeneratorCore implements WorkBench, GenerationConfiguration {
	private static final String CONFIG_DIRECTORY = "config/";
	private static final String STRUCTURE_DIRECTORY = "structures/";
	private IDbStructure currentStructure;
	private IDbStructure currentConfiguredStructure;

	private DatabaseCommunication dbComm;

	private void setCurrentStructure(IDbStructure newStructure) {
		if (newStructure == null) {
			currentStructure = null;
			currentConfiguredStructure = null;
		} else {
			if (newStructure.hasCycle()) {
				throw new RuntimeException("Cycle detected in structure. "
						+ newStructure.getCycleTableNames());
			}

			currentStructure = newStructure;
			currentConfiguredStructure = new ConfiguredDbStructure(
					currentStructure, ConfigInterpreter.createConfigReader());
		}

		dbComm.setStructure(newStructure);
	}

	public GeneratorCore(DatabaseCommunication dbComm) {
		this.dbComm = dbComm;
		setCurrentStructure(null);
	}

	@Override
	public void clear() {
		setCurrentStructure(null);
	}

	@Override
	public IDbStructure getCurrentStructure() {
		return currentStructure;
	}

	@Override
	public GenerationConfiguration getCurrentConfiguration() {
		return this;
	}

	@Override
	public void extractStructureFromDatabase() {
		createDirectory(CONFIG_DIRECTORY);
		createDirectory(GsonConfig.CONFIG_DIRECTORY);

		createDirectory(STRUCTURE_DIRECTORY);
		createDirectory(JSonStructureInterpreter.REST_STRUCTURE_JSON_DIRECTORY);

		IDbStructure newStructure = dbComm.createStructure();

		if (newStructure == null) {
			throw new NullPointerException(
					"Analyzing database structure failed; no database structure was returned");
		}

		setCurrentStructure(newStructure);
	}

	private void createDirectory(String directoryPath) {
		File configDir = new File(directoryPath);
		if (!configDir.exists()) {
			if (!configDir.mkdir()) {
				throw new RuntimeException(
						"Could not create configuration folder "
								+ configDir.getAbsolutePath());
			}
		}
	}

	@Override
	public void saveStructureToFile(String filename) {
		if (currentStructure == null) {
			throw new NullPointerException(
					"Cannot add column without initializing a structure. Try IGeneratorCore.extractStructureFromDatabase()");
		}

		JSonStructureInterpreter saver = new JSonStructureInterpreter(filename);
		saver.backupStructure(currentStructure);
	}

	@Override
	public void loadStructureFromFile(String fileLocation) {
		setCurrentStructure(new JSonStructureInterpreter(fileLocation)
				.createStructure());
	}

	@Override
	public ConfigReader getConfigurations() {
		return ConfigInterpreter.createConfigReader();
	}

	@Override
	public void resetToDefault() {
		if (currentStructure == null) {
			throw new IllegalStateException(
					"Cannot generate basic configuration without initializing a structure. Try IGeneratorCore.extractStructureFromDatabase()");
		}

		ConfigInterpreter.createConfigWriter().generateConfiguration(
				currentStructure);
	}

	@Override
	public void resetAllEntryAmounts() {
		ConfigInterpreter.createConfigWriter().resetAllEntryAmounts();
	}

	@Override
	public void setEntryAmountByTableName(String tableName, int amount) {
		ConfigWriter writer = ConfigInterpreter.createConfigWriter();
		try {
			writer.setEntryAmountByTableName(tableName, amount);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not find configuration of table "
					+ tableName);
		}

		includeColumns(currentStructure.getDependency(tableName));
	}

	@Override
	public void excludeColumn(DbLocation columnLocation) {
		try {
			ConfigInterpreter.createConfigWriter().setIncludeColumn(
					columnLocation, false);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not find configuration of table "
					+ columnLocation.getTable());
		}
	}

	@Override
	public void excludeColumn(String tableName, String columnName) {
		excludeColumn(new DbLocation(tableName, columnName));
	}

	@Override
	public void excludeColumns(List<DbLocation> columnLocations) {
		for (DbLocation location : columnLocations) {
			excludeColumn(location);
		}
	}

	@Override
	public void includeColumn(DbLocation columnLocation) {
		try {
			ConfigInterpreter.createConfigWriter().setIncludeColumn(
					columnLocation, true);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not find configuration of table "
					+ columnLocation.getTable());
		}
	}

	@Override
	public void includeColumn(String tableName, String columnName) {
		includeColumn(new DbLocation(tableName, columnName));
	}

	@Override
	public void includeColumns(List<DbLocation> columnLocations) {
		for (DbLocation location : columnLocations) {
			includeColumn(location);
		}
	}

	@Override
	public void excludeAllColumns() {
		ConfigInterpreter.createConfigWriter().excludeAllColumns();
	}

	@Override
	public void excludeAllColumnsFromTable(String tableName) {
		try {
			ConfigInterpreter.createConfigWriter().excludeAllColumnsFromTable(
					tableName);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not find configuration of table "
					+ tableName);
		}
	}

	@Override
	public void setColumnGenerator(String tableName, String columnName,
			ColumnGenerator columnGenerator) {
		setColumnGenerator(new DbLocation(tableName, columnName),
				columnGenerator);
	}

	@Override
	public void setColumnGenerator(DbLocation columnLocation,
			ColumnGenerator columnGenerator) {
		if(columnGenerator == null) {
			throw new IllegalArgumentException("Column Generator cannot be null");
		}
		
		try {
			ConfigInterpreter.createConfigWriter().setColumnGenerator(
					columnLocation, columnGenerator);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not find configuration of table "
					+ columnLocation.getTable());
		}

		includeColumns(currentStructure.getDependency(columnLocation));
		includeColumn(columnLocation);
	}

	@Override
	public List<DbLocation> getConfiguredDependency(String tableName, int depth) {
		return currentConfiguredStructure.getDependency(tableName, depth);
	}

	@Override
	public List<DbLocation> getConfiguredDependency(IDbTable table, int depth) {
		return currentConfiguredStructure.getDependency(table, depth);
	}

	@Override
	public void generateDatabase() {
		if (currentStructure == null) {
			throw new IllegalStateException(
					"Cannot generate without initialized structure. Try extractStructureFromDatabase() or loadStructureFromFile(...)");
		}

		ConfigInterpreter.createConfigReader();

		DatabaseGenerator generator = new ScheduledDatabaseGenerator();
		generator.generate(ConfigInterpreter.createConfigReader(),
				currentStructure, dbComm);
	}

}
