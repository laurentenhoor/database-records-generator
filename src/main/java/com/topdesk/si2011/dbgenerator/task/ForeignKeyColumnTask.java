package com.topdesk.si2011.dbgenerator.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.topdesk.si2011.dbgenerator.dbcache.IDbCache;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.ColumnGenerator;

public class ForeignKeyColumnTask extends ColumnTask {

	private final static Logger logger = LoggerFactory
			.getLogger(ForeignKeyColumnTask.class);

	private final DbLocation referenceColumnLocation;
	private final ColumnGenerator generator;
	private final IDbCache dbCache;

	public ForeignKeyColumnTask(DbLocation columnLocation, int entryIndex,
			DbLocation referenceColumnLocation, ColumnGenerator generator,
			IDbCache dbCache) {
		super(columnLocation, entryIndex);
		this.referenceColumnLocation = referenceColumnLocation;
		this.dbCache = dbCache;
		this.generator = generator;
	}

	@Override
	public void perform() {
		logger.debug("Generating " + getColumnLocation() + " from "
				+ referenceColumnLocation);

		HashMap<DbLocation, Object> referenceColumnEntries = Maps.newHashMap();
		ArrayList<Object> values = new ArrayList<Object>();
		values.addAll(dbCache.getColumnValue(referenceColumnLocation));

		referenceColumnEntries.put(referenceColumnLocation, values);

		Map<String, Integer> distribution = generator
				.createDistribution(referenceColumnEntries);
		dbCache.setColumnValue(getColumnLocation(), getEntryIndex(),
				generator.pickItem(distribution));
		super.perform();
	}

}
