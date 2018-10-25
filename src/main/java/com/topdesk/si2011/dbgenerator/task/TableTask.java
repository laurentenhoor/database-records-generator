package com.topdesk.si2011.dbgenerator.task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TableTask implements Task {
	private final static Logger logger = LoggerFactory
			.getLogger(TableTask.class);

	private final List<TableTask> dependentTableTasks = new ArrayList<TableTask>();
	private final List<ColumnTask> dependentColumnTasks = new ArrayList<ColumnTask>();
	private boolean performed = false;
	private final String tableName;
	private final int entryIndex;

	public TableTask(String tableName, int entryIndex) {
		this.tableName = tableName;
		this.entryIndex = entryIndex;
	}

	public String getTableName() {
		return tableName;
	}

	public int getEntryIndex() {
		return entryIndex;
	}

	public abstract TaskType getType();

	public List<TableTask> getDependentTableTasks() {
		return dependentTableTasks;
	}

	public void addDependentTableTask(TableTask task) {
		dependentTableTasks.add(task);
	}

	public List<ColumnTask> getDependentColumnTasks() {
		return dependentColumnTasks;
	}

	public void addDependentColumnTask(ColumnTask task) {
		logger.debug("Adding " + task.getDescription() + " to "
				+ getDescription());
		dependentColumnTasks.add(task);
	}

	@Override
	public boolean canPerform() {
		if (isPerformed()) {
			return false;
		}

		for (TableTask depTableTask : dependentTableTasks) {
			if (!depTableTask.isPerformed()) {
				return false;
			}
		}

		for (ColumnTask depColumnTask : dependentColumnTasks) {
			if (!depColumnTask.isPerformed()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void perform() {
		performed = true;
	}

	@Override
	public boolean isPerformed() {
		return performed;
	}

	@Override
	public String getDescription() {
		return tableName + "[" + getType() + "](" + entryIndex + ")";
	}

}
