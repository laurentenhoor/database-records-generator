package com.topdesk.si2011.dbgenerator.task;

import com.topdesk.si2011.dbgenerator.dbcache.IDbCache;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

public class InsertTask extends TableTask {
	private final IDbCache dbCache;

	public InsertTask(DbLocation primaryKeyColumnLocation, int entryIndex,
			IDbCache dbCache) {
		super(primaryKeyColumnLocation.getTable(), entryIndex);
		this.dbCache = dbCache;
	}

	@Override
	public TaskType getType() {
		return TaskType.INSERT;
	}

	@Override
	public void perform() {
		if (!canPerform()) {
			throw new IllegalStateException("Cannot perform insert task on "
					+ getTableName());
		}

		dbCache.doInsert(getTableName(), getEntryIndex());

		super.perform();
	}
}
