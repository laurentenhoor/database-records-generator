package com.topdesk.si2011.dbgenerator.configurator;

import java.io.FileNotFoundException;
import java.util.List;

import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.ColumnGenerator;

public interface ConfigReader {
	/**
	 * Returns the configuration specification of a table with the given name
	 * 
	 * @param name Name of the table
	 * @return 
	 * @throws FileNotFoundException 
	 */
	TableConfig getTableConfigByName(String name) throws FileNotFoundException;
	
	/**
	 * 
	 * @return
	 */
	List<TableConfig> getTableConfigs();
	List<TableConfig> getTableConfigWithEntryAmount();

	/**
	 * 
	 * @param name
	 * @return {@code true} when 
	 * @throws FileNotFoundException When no configuration file was found of the given table name
	 */
	boolean shouldGenerate(String name) throws FileNotFoundException;
	ColumnConfig getColumnByLocation(DbLocation location) throws FileNotFoundException;
	
	boolean hasGenerator(DbLocation location);
	ColumnGenerator getGenerator(DbLocation location);
}
