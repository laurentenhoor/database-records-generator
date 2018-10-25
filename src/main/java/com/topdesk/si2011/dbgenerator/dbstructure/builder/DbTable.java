package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import java.util.ArrayList;
import java.util.List;

import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;

class DbTable implements IDbTable {
	private final String name;
	private final List<IDbColumn> columns = new ArrayList<IDbColumn>();

	private IDbColumn primaryKeyColumn = null;

	public DbTable(String name) {
		this.name = name;
	}

	public void addPrimaryKeyColumn(PrimaryKeyColumn column) {
		
		column.setTable(this);

		primaryKeyColumn = column;
		addColumn(column);
	}

	@Override
	public String getName() {
		return name;
	}

	public boolean hasPrimaryKey() {
		return primaryKeyColumn != null;
	}

	@Override
	public IDbColumn getPrimaryKeyColumn() {
		return primaryKeyColumn;
	}
	
	@Override
	public String getPrimaryKeyColumnName() {
		return primaryKeyColumn.getName();
	}

	@Override
	public List<IDbColumn> getColumns() {
		return new ArrayList<IDbColumn>(columns);
	}

	@Override
	public IDbColumn getColumnByName(String columnName) {
		for (IDbColumn column : columns) {
			if (column.getName().equals(columnName)) {
				return column;
			}
		}

		return null;
	}

	public void addColumn(IDbColumn newColumn) {
		if (newColumn instanceof DefaultColumn) {
			((DefaultColumn) newColumn).setTable(this);
		} else if (newColumn instanceof ForeignKeyColumn) {
			((ForeignKeyColumn) newColumn).setTable(this);
		}

		// Other are unsupported.....
		columns.add(newColumn);
	}

	@Override
	public String toString() {
		return "Table['" + getName() + "']:" + columns;
	}

	@Override
	public List<IDbColumn> getForeignKeyColumns() {
		List<IDbColumn> result = new ArrayList<IDbColumn>();

		for (IDbColumn column : columns) {
			if (column.isForeignKey()) {
				result.add(column);
			}
		}

		return result;
	}

	@Override
	public List<IDbColumn> getSoftDependentColumns() {
		List<IDbColumn> result = new ArrayList<IDbColumn>();

		for (IDbColumn column : columns) {
			if (column.isForeignKey()
					&& column.getDataConstraint() == DbDataConstraint.NONE) {
				result.add(column);
			}
		}

		return result;
	}

	@Override
	public List<IDbColumn> getHardDependentColumns() {
		List<IDbColumn> result = new ArrayList<IDbColumn>();

		for (IDbColumn column : columns) {
			if (column.isForeignKey()
					&& column.getDataConstraint() != DbDataConstraint.NONE) {
				result.add(column);
			}
		}

		return result;
	}

	@Override
	public boolean hasForeignKeys() {
		return !getForeignKeyColumns().isEmpty();
	}
}
