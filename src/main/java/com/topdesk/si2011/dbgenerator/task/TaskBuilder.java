package com.topdesk.si2011.dbgenerator.task;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.topdesk.si2011.dbgenerator.configurator.ColumnConfig;
import com.topdesk.si2011.dbgenerator.configurator.ConfigReader;
import com.topdesk.si2011.dbgenerator.configurator.TableConfig;
import com.topdesk.si2011.dbgenerator.dbcache.IDbCache;
import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;
import com.topdesk.si2011.dbgenerator.generator.ColumnGenerator;
import com.topdesk.si2011.dbgenerator.generator.DefaultValueGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.ForeignKeySelectorGenerator;

public class TaskBuilder {
	private final static Logger logger = LoggerFactory
			.getLogger(TaskBuilder.class);

	private final IDbStructure structure;
	private final ConfigReader configuration;

	private final List<TableTask> tableTasks = Lists.newArrayList();
	private final List<ColumnTask> columnTasks = Lists.newArrayList();

	private final IDbCache dbCache;

	private Map<DbLocation, List<Integer>> debugColumnTaskId = Maps
			.newHashMap();

	private Map<String, List<Integer>> debugInsertTaskId = Maps.newHashMap();

	public TaskBuilder(IDbStructure structure, ConfigReader configuration,
			IDbCache dbCache) {
		this.structure = structure;
		this.configuration = configuration;
		this.dbCache = dbCache;
	}

	private TableTask addInsertTableTask(DbLocation primaryKeyColumnLocation,
			int entryIndex) {
		String tableName = primaryKeyColumnLocation.getTable();
		if (!debugInsertTaskId.containsKey(tableName)) {
			debugInsertTaskId.put(tableName, Lists.<Integer> newArrayList());
		}

		if (debugInsertTaskId.get(tableName).contains(entryIndex)) {
			throw new RuntimeException(
					"Added Insert Task with name,entryIndex combination twice. "
							+ tableName + " " + entryIndex);
		} else {
			debugInsertTaskId.get(tableName).add(entryIndex);
		}

		TableTask tableTask = new InsertTask(primaryKeyColumnLocation,
				entryIndex, dbCache);
		tableTasks.add(tableTask);
		return tableTask;
	}

	private TableTask addUpdateTableTask(String tableName, int entryIndex) {
		TableTask tableTask = new UpdateTask(tableName, entryIndex, dbCache);
		tableTasks.add(tableTask);
		return tableTask;
	}

	public ColumnTask addColumnTask(DbLocation location, int entryIndex,
			ColumnGenerator generator) {
		if (!debugColumnTaskId.containsKey(location)) {
			debugColumnTaskId.put(location, Lists.<Integer> newArrayList());
		}

		if (debugColumnTaskId.get(location).contains(entryIndex)) {
			throw new RuntimeException(
					"Added Column Task with location,entryIndex combination twice. "
							+ location + " " + entryIndex + " "
							+ generator.toString());
		} else {
			debugColumnTaskId.get(location).add(entryIndex);
		}

		ColumnTask columnTask = new DefaultColumnTask(location, entryIndex,
				generator, dbCache);
		columnTasks.add(columnTask);
		return columnTask;
	}

	public ColumnTask addForeignKeyColumnTask(DbLocation location,
			int entryIndex, DbLocation referenceLocation) {
		return addForeignKeyColumnTask(location, entryIndex, referenceLocation,
				new ForeignKeySelectorGenerator(referenceLocation));
	}

	public ColumnTask addForeignKeyColumnTask(DbLocation location,
			int entryIndex, DbLocation referenceLocation,
			ColumnGenerator generator) {
		ColumnTask columnTask = new ForeignKeyColumnTask(location, entryIndex,
				referenceLocation, generator, dbCache);

		columnTasks.add(columnTask);
		return columnTask;
	}

	public List<Task> build() {
		tableTasks.clear();

		createTableTasks();
		setColumnDependencies();

		List<Task> result = Lists.<Task> newArrayList(tableTasks);
		result.addAll(columnTasks);

		return ImmutableList.<Task> copyOf(result);
	}

	private void createTableTasks() {
		for (TableConfig tableConfig : configuration
				.getTableConfigWithEntryAmount()) {

			for (int entryIndex = 0; entryIndex < tableConfig.getEntryAmount(); entryIndex++) {
				IDbTable table = structure
						.getTableByName(tableConfig.getName());

				TableTask insertTask = addInsertTableTask(table
						.getPrimaryKeyColumn().getLocation(), entryIndex);

				for (IDbColumn column : table.getColumns()) {
					try {
						if (!column.getLocation().equals(
								table.getPrimaryKeyColumn().getLocation())) {
							ColumnConfig columnConfig;
							columnConfig = configuration
									.getColumnByLocation(column.getLocation());

							if (columnConfig.shouldGenerate()) {
								TableTask currentTask = insertTask;

								if (column.getDataConstraint() == DbDataConstraint.NONE) {
									currentTask = addUpdateTableTask(
											table.getName(), entryIndex);

									currentTask
											.addDependentTableTask(insertTask);
								}

								ColumnTask columnTask;

								if (column.isForeignKey()) {
									if (!configuration.hasGenerator(column
											.getLocation())) {
										columnTask = addForeignKeyColumnTask(
												column.getLocation(),
												entryIndex, column
														.getReferencedColumn()
														.getLocation());
									} else {
										columnTask = addForeignKeyColumnTask(
												column.getLocation(),
												entryIndex, column
														.getReferencedColumn()
														.getLocation(),
												configuration
														.getGenerator(column
																.getLocation()));
									}
								} else {
									if (!configuration.hasGenerator(column
											.getLocation())) {
										columnTask = addColumnTask(
												column.getLocation(),
												entryIndex,
												new DefaultValueGenerator(
														column.getDefaultValue()));
									} else {
										ColumnGenerator generator = configuration
												.getGenerator(column
														.getLocation());
										columnTask = addColumnTask(
												column.getLocation(),
												entryIndex, generator);
									}
								}

								currentTask.addDependentColumnTask(columnTask);
							} else {
								if (column.getDataConstraint() != DbDataConstraint.NONE) {
									throw new RuntimeException(
											"Column "
													+ column.getLocation()
													+ " is set not to generate, but is required to generate table "
													+ tableConfig.getName());
								}
							}
						}
					} catch (FileNotFoundException e) {
						throw new RuntimeException("Could not find configuration file of column " + column.getLocation());
					}
				}
			}
		}
	}

	private void setColumnDependencies() {
		for (ColumnTask columnTask : columnTasks) {
			IDbColumn column = structure.getColumnByLocation(columnTask
					.getColumnLocation());

			if (column.isForeignKey()) {
				// columnTask
				// .addDependentTask(findTableTaskByColumnLocation(column
				// .getReferencedColumn().getLocation()));
				//
				columnTask
						.addDependentTask(findLatestInsertTaskByTableName(column
								.getReferencedTable().getName()));
			} else {
				if (configuration.hasGenerator(columnTask.getColumnLocation())) {
					ColumnGenerator generator = configuration
							.getGenerator(columnTask.getColumnLocation());
					List<DbLocation> dependentColumns = generator
							.getDependentColumns();

					for (DbLocation depLocation : dependentColumns) {
						// columnTask
						// .addDependentTask(findTableTaskByColumnLocation(depLocation));

						columnTask
								.addDependentTask(findColumnEntryTaskByLocation(
										depLocation, columnTask.getEntryIndex()));
					}
				}
			}
		}
	}

	private Task findColumnEntryTaskByLocation(DbLocation location,
			int entryIndex) {
		for (ColumnTask columnTask : columnTasks) {
			if (columnTask.getColumnLocation().equals(location)
					&& columnTask.getEntryIndex() == entryIndex) {
				return columnTask;
			}
		}

		throw new RuntimeException("Could not find column task of location "
				+ location);
	}

	private Task findLatestInsertTaskByTableName(String tableName) {
		TableTask maxEntry = null;
		int maxEntryIndex = -1;

		for (TableTask tableTask : tableTasks) {
			if (tableTask.getTableName().equals(tableName)
					&& tableTask.getType() == TaskType.INSERT
					&& tableTask.getEntryIndex() > maxEntryIndex) {
				maxEntryIndex = tableTask.getEntryIndex();
				maxEntry = tableTask;
			}
		}

		if (maxEntry == null) {
			throw new RuntimeException(
					"Could not find Table INSERT task of name " + tableName);
		}

		return maxEntry;
	}
}
