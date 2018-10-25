package com.topdesk.si2011.dbgenerator.dbentry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DbTableEntry implements IDbTableEntry {
	private Map<String, IDbColumnEntry> columnEntry;
	private final String tableName;

	public DbTableEntry(String tableName) {
		this.tableName = tableName;
		columnEntry = new HashMap<String, IDbColumnEntry>();
	}

	public void addColumnEntry(String columnName, IDbColumnEntry newEntry) {
		columnEntry.put(columnName, newEntry);
	}

	@Override
	public List<String> getColumnNames() {
		return new ArrayList<String>(columnEntry.keySet());
	}

	@Override
	public IDbColumnEntry getColumnEntry(String columnName) {
		return columnEntry.get(columnName);
	}

	@Override
	public String getTableName() {
		return tableName;
	}
	
	@Override
	public String toString() {
		String result = tableName + ":";
		
		for(String columnName : getColumnNames()) {
			result += getColumnEntry(columnName) + ", ";
		}
		
		return result;
	}
}
