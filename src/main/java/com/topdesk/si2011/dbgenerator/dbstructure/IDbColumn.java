package com.topdesk.si2011.dbgenerator.dbstructure;

public interface IDbColumn {
	String getName();
	DbColumnType getType();
	String getDefaultValue();
	IDbTable getTable();
	
	boolean isPrimaryKey();
	boolean isForeignKey();
	DbLocation getLocation();
	
	IDbColumn getReferencedColumn();
	IDbTable getReferencedTable();
	
	DbDataConstraint getDataConstraint();
}