package com.topdesk.si2011.dbgenerator.configureddbstructure;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.topdesk.si2011.dbgenerator.configurator.ConfigReader;
import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;
import com.topdesk.si2011.dbgenerator.generator.ColumnGenerator;

public class ConfiguredDbStructure implements IDbStructure {
	private static final int INFINITE = -1;
	private final IDbStructure structure;
	private final ConfigReader configuration;

	public ConfiguredDbStructure(IDbStructure structure,
			ConfigReader configuration) {
		this.structure = structure;
		this.configuration = configuration;
	}

	@Override
	public List<String> getTableNames() {
		return structure.getTableNames();
	}

	@Override
	public List<IDbTable> getTables() {
		return structure.getTables();
	}

	@Override
	public IDbTable getTableByName(String name) {
		return structure.getTableByName(name);
	}

	@Override
	public IDbColumn getColumnByLocation(DbLocation location) {
		return structure.getColumnByLocation(location);
	}

	@Override
	public boolean hasCycle() {
		if (structure.hasCycle()) {
			return true;
		}

		return getCycleTableNames().size() > 0;
	}

	@Override
	public Set<String> getCycleTableNames() {
		Set<String> cycleTables = new HashSet<String>();

		for (IDbTable table : getTables()) {
			for (IDbColumn column : table.getColumns()) {
				try {
					if ((column.isForeignKey()
							&& column.getDataConstraint() != DbDataConstraint.NONE) || configuration
									.getColumnByLocation(column.getLocation())
									.shouldGenerate()) {
						if (isDependentOf(column, table)) {
							cycleTables.add(table.getName());
						}
					}
				} catch (FileNotFoundException e) {
					throw new RuntimeException("Could not find configuration file of " + column.getLocation());
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
		List<DbLocation> dependentLocations = Lists.newArrayList();

		for (IDbColumn column : table.getColumns()) {
			Set<DbLocation> referenceLocations = Sets.newHashSet();

			try {
				if (column.getDataConstraint() != DbDataConstraint.NONE
						|| configuration.getColumnByLocation(column.getLocation())
								.shouldGenerate()) {
					dependentLocations.add(column.getLocation());

					if (column.isForeignKey()) {
						referenceLocations.add(column.getReferencedColumn()
								.getLocation());
					} else {
						if (configuration.hasGenerator(column.getLocation())) {
							ColumnGenerator generator = configuration
									.getGenerator(column.getLocation());

							referenceLocations.addAll(generator
									.getDependentColumns());
						}
					}
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Could not find configuration file of " + column.getLocation());
			}

			for (DbLocation referenceLocation : referenceLocations) {
				if (depth > 0) {
					dependentLocations.addAll(getDependency(referenceLocation,
							depth - 1));
				} else if (depth == INFINITE) {
					dependentLocations.addAll(getDependency(referenceLocation,
							depth));
				}
			}
		}

		return dependentLocations;
	}

	@Override
	public List<DbLocation> getDependency(DbLocation location, int depth) {
		List<DbLocation> dependentLocations = Lists.newArrayList();

		dependentLocations.add(location);
		IDbColumn columnByLocation = getColumnByLocation(location);
		if (columnByLocation.isForeignKey()) {
			if (depth > 0) {
				dependentLocations.addAll(getDependency(
						columnByLocation.getReferencedTable(), depth - 1));
			} else if (depth == INFINITE) {
				dependentLocations.addAll(getDependency(
						columnByLocation.getReferencedTable(), depth));
			}
		}

		return dependentLocations;
	}

	@Override
	public List<DbLocation> getDependency(IDbColumn column, int depth) {
		return getDependency(column.getLocation(), depth);
	}
}
