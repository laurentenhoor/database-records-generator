package com.topdesk.si2011.dbgenerator.dbstructure;

import java.util.List;

/**
 * Interface which describes the structural information about a database table  
 *
 */
public interface IDbTable {

	String getName();
	
	List<IDbColumn> getColumns();
	IDbColumn getColumnByName(String columnName);
	
	boolean hasPrimaryKey();
	IDbColumn getPrimaryKeyColumn();
	String getPrimaryKeyColumnName();
	
	boolean hasForeignKeys();
	List<IDbColumn> getForeignKeyColumns();
	
	List<IDbColumn> getSoftDependentColumns();
	List<IDbColumn> getHardDependentColumns();

}