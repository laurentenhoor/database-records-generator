package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnType;
import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;

public class PrimaryKeyColumn implements IDbColumn {
	private final String name;
	private final DbColumnType type;
	private final DbDataConstraint constraint;
	private IDbTable table;

	public PrimaryKeyColumn(String name, DbColumnType type,
			DbDataConstraint constraint) {
		this.name = name;
		this.type = type;
		this.constraint = constraint;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DbColumnType getType() {
		return type;
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public boolean isPrimaryKey() {
		return true;
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
		return "{'" + name + "' "+ type + " " + isPrimaryKey() + " " + isForeignKey() + " "
				+ constraint + " (" + table.getName() + ")}\n";
	}
}
