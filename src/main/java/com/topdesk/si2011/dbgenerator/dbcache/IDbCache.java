package com.topdesk.si2011.dbgenerator.dbcache;

import java.util.List;

import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

public interface IDbCache {
	void setColumnValue(DbLocation columnLocation, int entryIndex, Object value);
	Object getColumnValue(DbLocation columnLocation, int entryIndex);
	List<Object> getColumnValue(DbLocation columnLocation); // Returns ALL values of this column	
	
	void doInsert(String tableName, int entryIndex);
	void doUpdate(String tableName, int entryIndex);
	
	boolean hasColumnValue(DbLocation columnLocation, int entryIndex);
}
