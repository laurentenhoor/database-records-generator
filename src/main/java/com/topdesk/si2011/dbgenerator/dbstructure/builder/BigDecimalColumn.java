package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnType;
import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnTypeName;
import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;

public class BigDecimalColumn extends DefaultColumn {

	public BigDecimalColumn(String name, String defaultValue,
			DbDataConstraint constraint) {
		super(name, defaultValue, constraint);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DbColumnType getType() {
		return new DbColumnType(DbColumnTypeName.BIGDECIMAL);
	}

}
