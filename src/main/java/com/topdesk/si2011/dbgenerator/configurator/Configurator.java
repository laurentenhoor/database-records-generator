package com.topdesk.si2011.dbgenerator.configurator;

import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;

public class Configurator {
	public static void generateConfiguration(IDbStructure structure) {
		for(IDbTable table : structure.getTables()) {
			GSonTableConfig newTableConfig = new GSonTableConfig();
			newTableConfig.setName(table.getName());
			newTableConfig.setEntryAmount(0);
			
			for(IDbColumn column: table.getColumns()) {
				GSonColumnConfig newColumnConfig = new GSonColumnConfig(table.getName());
				newColumnConfig.setName(column.getName());
				newColumnConfig.setShouldGenerate(false);
				
				newTableConfig.addColumnConfig(newColumnConfig);
			}
		}
	}
}
