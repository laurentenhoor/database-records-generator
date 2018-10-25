package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnType;
import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnTypeName;
import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;

public class DoubleColumn extends DefaultColumn {

	public DoubleColumn(String name, String defaultValue,
			DbDataConstraint constraint) {
		super(name, defaultValue, constraint);
	}

	@Override
	public DbColumnType getType() {
		return new DbColumnType(DbColumnTypeName.DOUBLE);
	}

}
