package com.topdesk.si2011.dbgenerator.configurator;

import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

public interface ColumnConfig {
	String getName();
	String getTableName();
	DbLocation getLocation();
	
	boolean shouldGenerate();
}
