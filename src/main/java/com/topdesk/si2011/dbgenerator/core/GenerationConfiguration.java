package com.topdesk.si2011.dbgenerator.core;

import java.util.List;

import com.topdesk.si2011.dbgenerator.configurator.ConfigReader;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;
import com.topdesk.si2011.dbgenerator.generator.ColumnGenerator;

public interface GenerationConfiguration {
	/**
	 * Returns all the configurations for each table and column
	 * @return all the configurations for each table and column
	 */
	ConfigReader getConfigurations();
	
	/**
	 * Use the in-memory database structure to generate a basic configuration
	 */
	void resetToDefault();
	
	/**
	 * Resets all the configured amount of to-be-generated table entries to 0
	 */
	void resetAllEntryAmounts();

	/**
	 * Sets the configured amount of to-be-generated table entries
	 * 
	 * @param tableName Table name
	 * @param amount New amount
	 */
	void setEntryAmountByTableName(String tableName, int amount);
	
	/**
	 * Excludes a column from generation. 
	 * @param columnLocation
	 */
	void excludeColumn(DbLocation columnLocation);
	
	/**
	 * Excludes a column from generation.
	 * @param tableName
	 * @param columnName
	 */
	void excludeColumn(String tableName, String columnName);
	
	/**
	 * Excludes columns from generation
	 * @param columnLocations
	 */
	void excludeColumns(List<DbLocation> columnLocations);
	
	/**
	 * Includes a column to generation. 
	 * @param columnLocation
	 */
	void includeColumn(DbLocation columnLocation);
	
	/**
	 * Includes a column to generation.
	 * @param tableName
	 * @param columnName
	 */
	void includeColumn(String tableName, String columnName);
	
	/**
	 * Includes columns to generation. 
	 * @param columnLocations
	 */
	void includeColumns(List<DbLocation> columnLocations);
	
	/**
	 * Excludes all columns from generation
	 */
	void excludeAllColumns();
	
	/**
	 * Excludes all columns from table from generation
	 * @param tableName
	 */
	void excludeAllColumnsFromTable(String tableName);
	
	/**
	 * Sets the column generator for a specific column. Setting this function will automatically include
	 * the column for generation and also include its dependencies. 
	 * 
	 * @param columnLocation
	 * @param columnGenerator
	 */
	void setColumnGenerator(DbLocation columnLocation,
			ColumnGenerator columnGenerator);
	void setColumnGenerator(String tableName, String columName,
			ColumnGenerator columnGenerator);	
	
	
	List<DbLocation> getConfiguredDependency(String tableName, int depth);
	List<DbLocation> getConfiguredDependency(IDbTable table, int depth);	
}
