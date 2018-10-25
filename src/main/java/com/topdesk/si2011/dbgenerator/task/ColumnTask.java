package com.topdesk.si2011.dbgenerator.task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

public abstract class ColumnTask implements Task {
	private final static Logger logger = LoggerFactory
	.getLogger(ColumnTask.class);
	
	private final List<Task> dependentTask = new ArrayList<Task>();
	private boolean performed = false;
	private final DbLocation columnLocation;
	private final int entryIndex;

	public ColumnTask(DbLocation columnLocation, int entryIndex) {
		this.columnLocation = columnLocation;
		this.entryIndex = entryIndex;
	}

	public DbLocation getColumnLocation() {
		return columnLocation;
	}

	public void addDependentTask(Task task) {
		logger.debug("Adding " + task.getDescription() + " to " + getDescription());
		dependentTask.add(task);
	}

	@Override
	public boolean canPerform() {
		if(isPerformed()) {
			return false;
		}
		
		for (Task depTask : dependentTask) {
			if (!depTask.isPerformed()) {
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
		return columnLocation.toString() + "(" + entryIndex + ")";
	}

	public int getEntryIndex() {
		return entryIndex;
	}

}
