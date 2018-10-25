package com.topdesk.si2011.dbgenerator.dbstructure;

import java.util.List;
import java.util.Set;

public interface IDbStructure {
	List<String> getTableNames();
	List<IDbTable> getTables();
	IDbTable getTableByName(String name);
	
	IDbColumn getColumnByLocation(DbLocation location);
	
	boolean hasCycle();
	Set<String> getCycleTableNames();
	
	List<DbLocation> getDependency(String table);
	List<DbLocation> getDependency(IDbTable table);
	List<DbLocation> getDependency(DbLocation columnLocation);
	List<DbLocation> getDependency(IDbColumn columnLocation);
	
	List<DbLocation> getDependency(String table, int depth);
	List<DbLocation> getDependency(IDbTable table, int depth);
	List<DbLocation> getDependency(DbLocation location, int depth);
	List<DbLocation> getDependency(IDbColumn column, int depth);
}