package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;

public abstract class DefaultColumn implements IDbColumn {

	private final String name;
	private final String defaultValue;
	private final DbDataConstraint constraint;
	private IDbTable table;

	public DefaultColumn(String name, String defaultValue,
			DbDataConstraint constraint) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.constraint = constraint;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isPrimaryKey() {
		return false;
	}

	@Override
	public boolean isForeignKey() {
		return false;
	}

	@Override
	public DbLocation getLocation() {
		return new DbLocation(table.getName(), name);
	}
	
	@Override
	public IDbColumn getReferencedColumn() {
		return null;
	}

	@Override
	public IDbTable getReferencedTable() {
		return null;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public DbDataConstraint getDataConstraint() {
		return constraint;
	}

	void setTable(IDbTable table) {
		this.table = table;
	}

	@Override
	public IDbTable getTable() {
		return table;
	}

	@Override
	public String toString() {
		return "{'" + name + "' " + isPrimaryKey() + " "
				+ isForeignKey() + " " + constraint + " " + " (" + table.getName() + ")}\n";
	}
}
