package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnType;
import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnTypeName;
import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;

public class TextColumn extends DefaultColumn {
	private final int size;

	public TextColumn(String name, int size, String defaultValue, DbDataConstraint constraint) {
		super(name, defaultValue, constraint);
		this.size = size;
	}

	@Override
	public DbColumnType getType() {
		return new DbColumnType(DbColumnTypeName.TEXT, size);
	}
}
