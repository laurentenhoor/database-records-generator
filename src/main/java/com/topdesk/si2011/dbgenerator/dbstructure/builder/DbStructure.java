package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;

class DbStructure implements IDbStructure {
	private static final int INFINITE = -1;
	private final List<IDbTable> tables;

	public DbStructure(List<IDbTable> tables) {
		this.tables = tables;
	}

	@Override
	public List<String> getTableNames() {
		List<String> result = new ArrayList<String>();

		for (IDbTable table : tables) {
			result.add(table.getName());
		}

		return result;
	}

	@Override
	public List<IDbTable> getTables() {
		return ImmutableList.<IDbTable> copyOf(tables);
	}

	@Override
	public IDbTable getTableByName(String tableName) {
		for (IDbTable table : tables) {
			if (table.getName().equals(tableName)) {
				return table;
			}
		}

		return null;
	}

	@Override
	public IDbColumn getColumnByLocation(DbLocation location) {
		return getTableByName(location.getTable()).getColumnByName(
				location.getColumn());
	}

	@Override
	public boolean hasCycle() {
		return getCycleTableNames().size() > 0;
	}
	
	@Override
	public Set<String> getCycleTableNames() {
		Set<String> cycleTables = new HashSet<String>();
		
		for (IDbTable table : getTables()) {
			for (IDbColumn column : table.getColumns()) {
				if (column.isForeignKey()
						&& column.getDataConstraint() != DbDataConstraint.NONE) {
					if(isDependentOf(column, table)) {
						cycleTables.add(table.getName());
					}
				}
			}
		}

		return cycleTables;
	}

	private boolean isDependentOf(IDbColumn column, IDbTable dependentTable) {
		List<DbLocation> dependenctLocations = getDependency(column);
		dependenctLocations.remove(column.getLocation());
		
		for (DbLocation location : dependenctLocations) {
			if (location.getTable().equals(dependentTable.getName())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<DbLocation> getDependency(String table) {
		return getDependency(table, INFINITE);
	}

	@Override
	public List<DbLocation> getDependency(IDbTable table) {
		return getDependency(table, INFINITE);
	}

	@Override
	public List<DbLocation> getDependency(DbLocation columnLocation) {
		return getDependency(columnLocation, INFINITE);
	}

	@Override
	public List<DbLocation> getDependency(IDbColumn column) {
		return getDependency(column, INFINITE);
	}

	@Override
	public List<DbLocation> getDependency(String tableName, int depth) {
		return getDependency(getTableByName(tableName), depth);
	}

	@Override
	public List<DbLocation> getDependency(IDbTable table, int depth) {
		List<DbLocation> newArrayList = Lists.newArrayList();

		for (IDbColumn column : table.getColumns()) {
			if (column.getDataConstraint() != DbDataConstraint.NONE) {
				newArrayList.add(column.getLocation());

				if (column.isForeignKey()) {
					if (depth > 0) {
						newArrayList.addAll(getDependency(
								column.getReferencedTable(), depth - 1));
					} else if (depth == INFINITE) {
						newArrayList.addAll(getDependency(
								column.getReferencedTable(), depth));
					}
				}
			}
		}

		return newArrayList;
	}

	@Override
	public List<DbLocation> getDependency(DbLocation location, int depth) {
		List<DbLocation> newArrayList = Lists.newArrayList();

		newArrayList.add(location);
		IDbColumn columnByLocation = getColumnByLocation(location);
		if (columnByLocation.isForeignKey()) {
			if (depth > 0) {
				newArrayList.addAll(getDependency(
						columnByLocation.getReferencedTable(), depth - 1));
			} else if (depth == INFINITE) {
				newArrayList.addAll(getDependency(
						columnByLocation.getReferencedTable(), depth));
			}
		}

		return newArrayList;
	}

	@Override
	public List<DbLocation> getDependency(IDbColumn column, int depth) {
		return getDependency(column.getLocation(), depth);
	}
}
