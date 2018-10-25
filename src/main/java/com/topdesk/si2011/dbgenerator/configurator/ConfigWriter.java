package com.topdesk.si2011.dbgenerator.configurator;

import java.io.FileNotFoundException;

import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.generator.ColumnGenerator;

public interface ConfigWriter extends ConfigReader {
	void generateConfiguration(IDbStructure structure);
	
	void resetAllEntryAmounts();
	void excludeAllColumns();
	void excludeAllColumnsFromTable(String tableName) throws FileNotFoundException;
	
	void setEntryAmountByTableName(String tableName, int amount) throws FileNotFoundException;
	void setIncludeColumn(DbLocation columnLocation, boolean include) throws FileNotFoundException;
	void setColumnGenerator(DbLocation columnLocation,
			ColumnGenerator columnGenerator) throws FileNotFoundException;
	
}
