package com.topdesk.si2011.dbgenerator.task;

import com.topdesk.si2011.dbgenerator.dbcache.IDbCache;

public class UpdateTask extends TableTask {

	private final IDbCache dbCache;

	public UpdateTask(String tableName, int entryIndex, IDbCache dbCache) {
		super(tableName, entryIndex);
		this.dbCache = dbCache;
	}

	@Override
	public TaskType getType() {
		return TaskType.UPDATE;
	}

	@Override
	public void perform() {
		if (!canPerform()) {
			throw new IllegalStateException("Cannot perform update task on "
					+ getTableName());
		}

		dbCache.doUpdate(getTableName(), getEntryIndex());

		super.perform();
	}
}
