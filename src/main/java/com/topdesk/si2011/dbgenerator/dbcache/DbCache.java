package com.topdesk.si2011.dbgenerator.dbcache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunication;
import com.topdesk.si2011.dbgenerator.dbentry.DbTableEntryBuilder;
import com.topdesk.si2011.dbgenerator.dbentry.IDbTableEntry;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;

public class DbCache implements IDbCache {
	private final DatabaseCommunication dbComm;
	private final IDbStructure dbStructure;

	private Map<String, List<Map<String, Object>>> dbCache = new HashMap<String, List<Map<String, Object>>>();

	public DbCache(DatabaseCommunication dbComm, IDbStructure dbStructure) {
		this.dbComm = dbComm;
		this.dbStructure = dbStructure;
	}

	@Override
	public void setColumnValue(DbLocation columnLocation, int entryIndex,
			Object value) {

		if (!tableExists(columnLocation)) {
			dbCache.put(columnLocation.getTable(),new ArrayList<Map<String, Object>>());
		}

		if (!entryExists(columnLocation, entryIndex)) {
			dbCache.get(columnLocation.getTable()).add(entryIndex, new HashMap<String, Object>());
		}

		if (!columnExists(columnLocation, entryIndex)) {
			dbCache.get(columnLocation.getTable()).get(entryIndex).put(columnLocation.getColumn(), value);
		} else {
			throw new RuntimeException("Column " + columnLocation.getColumn() 
					+ " in table " + columnLocation.getTable() + "(" + entryIndex + ")" + " is already cached!");
		}
	}

	@Override
	public boolean hasColumnValue(DbLocation columnLocation, int entryIndex) {
		return columnExists(columnLocation, entryIndex);
	}

	@Override
	public Object getColumnValue(DbLocation columnLocation, int entryIndex) {
		if (columnExists(columnLocation, entryIndex)){
			return dbCache.get(columnLocation.getTable()).get(entryIndex).get(columnLocation.getColumn());
		} 
		throw new RuntimeException("Column " + columnLocation.getColumn() 
				+ " in table " + columnLocation.getTable() + "doesn't exist!");
	}

	@Override
	public void doInsert(String tableName, int entryIndex) {
		
		IDbTableEntry newEntry = dbComm.insertTableEntry(buildEntry(tableName, entryIndex));
		
		for (String columnName : newEntry.getColumnNames()){
			if (columnName.equals(dbStructure.getTableByName(tableName).getPrimaryKeyColumn().getName())) {
				dbCache.get(tableName).get(entryIndex).put(columnName, newEntry.getColumnEntry(columnName).getValue());
			} 
		}
	}
	
	@Override
	public void doUpdate(String tableName, int entryIndex) {
		dbComm.updateTableEntry(buildEntry(tableName, entryIndex));
	}
	
	
	@Override
	public List<Object> getColumnValue(DbLocation columnLocation) {
		
		List<Object> columnValues = new ArrayList<Object>();
		
		for (int i = 0; i < dbCache.get(columnLocation.getTable()).size(); i++) {
			columnValues.add(dbCache.get(columnLocation.getTable()).get(i).get(columnLocation.getColumn()));
		}
		return columnValues;
	}

	private IDbTableEntry buildEntry(String tableName, int entryIndex) {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create(tableName);
		
		Map<String, Object> rowEntry = dbCache.get(tableName).get(entryIndex);
		for (String columnName : rowEntry.keySet()) {
			builder.addColumn(columnName, "" + rowEntry.get(columnName));
		}
		
		return builder.build();
	}

	private boolean tableExists(DbLocation columnLocation) {

		if (dbCache.containsKey(columnLocation.getTable())) {
			return true;
		}
		return false;

	}

	private boolean entryExists(DbLocation columnLocation, int entryIndex) {

		if (tableExists(columnLocation)) {
			return (dbCache.get(columnLocation.getTable()).size() > entryIndex);
		}
		return false;
	}

	private boolean columnExists(DbLocation columnLocation, int entryIndex) {

		if (entryExists(columnLocation, entryIndex)) {
			return dbCache.get(columnLocation.getTable()).get(entryIndex)
					.containsKey(columnLocation.getColumn());
		}

		return false;
	}

}
