package com.topdesk.si2011.dbgenerator.task;

import java.util.HashMap;
import java.util.Map;

import com.topdesk.si2011.dbgenerator.dbcache.IDbCache;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.ColumnGenerator;

public class DefaultColumnTask extends ColumnTask {
	
	private final ColumnGenerator generator;
	private final IDbCache dbCache;

	public DefaultColumnTask(DbLocation columnLocation, int entryIndex,
			ColumnGenerator generator, IDbCache dbCache) {
		super(columnLocation, entryIndex);

		this.generator = generator;
		this.dbCache = dbCache;
	}

	public ColumnGenerator getGenerator() {
		return generator;
	}

	@Override
	public void perform() {

		HashMap<DbLocation, Object> generatedValues = new HashMap<DbLocation, Object>();

		for (DbLocation depLocation : generator.getDependentColumns()) {
			generatedValues.put(depLocation, dbCache.getColumnValue(depLocation, getEntryIndex()));
		}

		Map<String, Integer> distribution = generator.createDistribution(generatedValues);
		dbCache.setColumnValue(getColumnLocation(), getEntryIndex(), generator.pickItem(distribution));

		super.perform();
	}
}
