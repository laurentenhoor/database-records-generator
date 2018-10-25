package com.topdesk.si2011.dbgenerator.configurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;
import com.topdesk.si2011.dbgenerator.generator.ColumnGenerator;

public class GsonConfig implements ConfigWriter {
	private final static Logger logger = LoggerFactory
			.getLogger(GsonConfig.class);

	private static final String JSON_EXTENTION = ".json";
	public static final String CONFIG_DIRECTORY = "config/tableConfig/";

	private final Map<DbLocation, ColumnGenerator> generatorMap = new HashMap<DbLocation, ColumnGenerator>();

	@Override
	public GSonTableConfig getTableConfigByName(String name)
			throws FileNotFoundException {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}

		return readConfig(CONFIG_DIRECTORY + name + JSON_EXTENTION);
	}

	private GSonTableConfig readConfig(String fileName)
			throws FileNotFoundException {
		if (!new File(fileName).exists()) {
			throw new FileNotFoundException(
					"No table configuration file found at "
							+ fileName
							+ ". Try generating configurations from a databse structure.");
		}

		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(fileName));

			return new Gson().fromJson(bufferedReader, GSonTableConfig.class);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public List<GSonTableConfig> getWritableTableConfigs() {
		return readAllConfig(CONFIG_DIRECTORY);
	}

	@Override
	public List<TableConfig> getTableConfigs() {
		return ImmutableList
				.<TableConfig> copyOf(readAllConfig(CONFIG_DIRECTORY));
	}

	private List<GSonTableConfig> readAllConfig(String dirName) {
		List<GSonTableConfig> result = Lists.newArrayList();
		File dir = new File(dirName);

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(JSON_EXTENTION);
			}
		};

		for (String configFileName : dir.list(filter)) {
			try {
				result.add(readConfig(dir.getAbsolutePath() + "/"
						+ configFileName));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(
						"FileNotFoundException was caugh, while looping over existing configuration files. Problem with file "
								+ configFileName);
			}
		}

		return result;
	}

	@Override
	public boolean shouldGenerate(String name) throws FileNotFoundException {
		return readConfig(CONFIG_DIRECTORY + name + JSON_EXTENTION)
				.getEntryAmount() > 0;
	}

	@Override
	public void resetAllEntryAmounts() {
		List<GSonTableConfig> tableConfigs = getWritableTableConfigs();
		for (GSonTableConfig tableConfig : tableConfigs) {
			tableConfig.setEntryAmount(0);
			writeConfig(tableConfig);
		}
	}

	void writeConfig(GSonTableConfig config) {
		writeConfig(config, CONFIG_DIRECTORY + config.getName()
				+ JSON_EXTENTION);
	}

	private void writeConfig(GSonTableConfig config, String fileName) {
		Writer writer = null;

		try {

			writer = new OutputStreamWriter(new FileOutputStream(fileName));

			new GsonBuilder().setPrettyPrinting().create()
					.toJson(config, GSonTableConfig.class, writer);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void excludeAllColumns() {
		List<GSonTableConfig> tableConfigs = getWritableTableConfigs();
		for (GSonTableConfig tableConfig : tableConfigs) {
			for (GSonColumnConfig columnConfig : tableConfig
					.getWritableColumnConfigs()) {
				columnConfig.setShouldGenerate(false);
			}

			writeConfig(tableConfig);
		}
	}

	@Override
	public void excludeAllColumnsFromTable(String tableName) throws FileNotFoundException {
		GSonTableConfig tableConfig = getTableConfigByName(tableName);
		for (GSonColumnConfig columnConfig : tableConfig
				.getWritableColumnConfigs()) {
			columnConfig.setShouldGenerate(false);
		}

		writeConfig(tableConfig);
	}

	@Override
	public void setIncludeColumn(DbLocation columnLocation, boolean include) throws FileNotFoundException {
		GSonTableConfig tableConfig = getTableConfigByName(columnLocation
				.getTable());
		tableConfig.getColumnConfigByName(columnLocation.getColumn())
				.setShouldGenerate(include);

		writeConfig(tableConfig);
	}

	@Override
	public void setEntryAmountByTableName(String tableName, int amount) throws FileNotFoundException {
		GSonTableConfig tableConfig = getTableConfigByName(tableName);
		tableConfig.setEntryAmount(amount);
		writeConfig(tableConfig);
	}

	@Override
	public void generateConfiguration(IDbStructure structure) {
		for (IDbTable table : structure.getTables()) {
			logger.debug("considering " + table.getName());
			GSonTableConfig newTableConfig = new GSonTableConfig();
			newTableConfig.setName(table.getName());
			newTableConfig.setEntryAmount(0);

			for (IDbColumn column : table.getColumns()) {
				GSonColumnConfig newColumnConfig = new GSonColumnConfig(
						table.getName());
				newColumnConfig.setName(column.getName());
				newColumnConfig.setShouldGenerate(false);
				newTableConfig.addColumnConfig(newColumnConfig);
			}

			writeConfig(newTableConfig);
		}
	}

	@Override
	public void setColumnGenerator(DbLocation columnLocation,
			ColumnGenerator columnGenerator) {
		generatorMap.put(columnLocation, columnGenerator);
	}

	@Override
	public ColumnConfig getColumnByLocation(DbLocation location) throws FileNotFoundException {
		return getTableConfigByName(location.getTable()).getColumnConfigByName(
				location.getColumn());
	}

	@Override
	public ColumnGenerator getGenerator(DbLocation location) {
		return generatorMap.get(location);
	}

	public boolean hasGenerator(DbLocation location) {
		return getGenerator(location) != null;
	}

	@Override
	public List<TableConfig> getTableConfigWithEntryAmount() {
		ArrayList<TableConfig> result = Lists.newArrayList();

		for (TableConfig tableConfig : getTableConfigs()) {
			if (tableConfig.getEntryAmount() > 0) {
				result.add(tableConfig);
			}
		}

		return ImmutableList.<TableConfig> copyOf(result);

	}
}
