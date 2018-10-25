package com.topdesk.si2011.dbgenerator.configurator;

import java.util.List;

public interface TableConfig {

	String getName();

	List<ColumnConfig> getColumnConfigs();
	ColumnConfig getColumnConfigByName(String name);

	int getEntryAmount();
}
