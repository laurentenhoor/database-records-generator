package com.topdesk.si2011.dbgenerator.configurator;

import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

public class GSonColumnConfig implements ColumnConfig {
	private String name;
	private boolean shouldGenerate;
	private final String tableName;
	
	public GSonColumnConfig(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String getTableName() {
		return tableName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setShouldGenerate(boolean shouldGenerate) {
		this.shouldGenerate = shouldGenerate;
	}

	@Override
	public boolean shouldGenerate() {
		return shouldGenerate;
	}
	
	@Override
	public DbLocation getLocation() {
		return new DbLocation(tableName, name);
	}


	@Override
	public String toString() {
		return "ColumnConfig(" + name + ")" + shouldGenerate;
	}
}
