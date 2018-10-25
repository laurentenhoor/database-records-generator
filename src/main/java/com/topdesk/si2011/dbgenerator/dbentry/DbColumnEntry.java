package com.topdesk.si2011.dbgenerator.dbentry;

import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

class DbColumnEntry implements IDbColumnEntry {
	private final DbLocation location;
	private String value;

	public DbColumnEntry(DbLocation location) {
		this.location = location;

		this.value = null;
	}
	
	public DbColumnEntry(DbLocation location, String value) {
		this.location = location;
		
		this.value = value;
	}
	
	@Override
	public DbLocation getColumnLocation() {
		return location;
	}

	@Override
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return location + "=" + value;
	}

}
