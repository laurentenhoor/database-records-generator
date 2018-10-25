package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnType;
import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;

public class ForeignKeyColumn implements IDbColumn {
	private final String name;
	private final String defaultValue;
	private final DbDataConstraint constraint;
	private IDbTable table;
	
	private IDbColumn referenceColumn = null;
	
	public ForeignKeyColumn(String name, String defaultValue, DbDataConstraint constraint) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.constraint = constraint;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public DbColumnType getType() {
		if(referenceColumn == null)
			return null;
		
		return referenceColumn.getType();
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public boolean isPrimaryKey() {
		return false;
	}

	@Override
	public boolean isForeignKey() {
		return true;
	}
	
	@Override
	public DbLocation getLocation() {
		return new DbLocation(table.getName(), name);
	}

	public void setReferenceColumn(IDbColumn column) {
		this.referenceColumn = column;
	}
	
	@Override
	public IDbColumn getReferencedColumn() {
		return referenceColumn;
	}

	@Override
	public IDbTable getReferencedTable() {
		return referenceColumn.getTable();
	}
	
	@Override
	public DbDataConstraint getDataConstraint() {
		return constraint;
	}

	@Override
	public IDbTable getTable() {
		return table;
	}
	
	void setTable(IDbTable table) {
		this.table = table;
	}
	
	@Override
	public String toString() {
		return "{'" + name + "' " + isPrimaryKey() + " "
				+ isForeignKey() + " " + getReferencedTable().getName() + " " + getReferencedColumn().getName() + " " + constraint + " " + " (" + table.getName() + ")}\n";
	}
}
