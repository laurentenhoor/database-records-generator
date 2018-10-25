package com.topdesk.si2011.dbgenerator.dbentry;

import java.util.Map;

import com.google.common.collect.Maps;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

public class DbTableEntryBuilder {
	private final Map<DbLocation, DbColumnEntry> entries = Maps.newHashMap();
	private final String tableName;
	private DbTableEntry currentEntry;

	private DbTableEntryBuilder(String tableName) {
		this.tableName = tableName;
		currentEntry = new DbTableEntry(tableName);
	}

	public static DbTableEntryBuilder create(String tableName) {
		return new DbTableEntryBuilder(tableName);
	}

	public IDbColumnEntry addColumn(String columnName, String value) {
		DbLocation location = DbLocation.create(tableName, columnName);
		if (entries.containsKey(location)) {
			throw new IllegalStateException(
					"Cannot add same column twice; Column "
							+ columnName + " is already added");
		}

		if (currentEntry != null) {
			DbColumnEntry newColumnEntry = new DbColumnEntry(location,value);

			entries.put(location, newColumnEntry);
			return newColumnEntry;
		}

		throw new IllegalStateException("Table should have been created by now");
	}

	public IDbTableEntry build() {
		if (entries.size() < 1) {
			throw new IllegalStateException(
					"Cannot build table entry without any column entries");
		}

		DbTableEntry entry = new DbTableEntry(tableName);

		for (DbLocation location : entries.keySet()) {
			entry.addColumnEntry(location.getColumn(), entries.get(location));
		}

		return entry;
	}
}
