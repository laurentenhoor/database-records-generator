package com.topdesk.si2011.dbgenerator.communication;

import java.util.Set;

import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnType;
import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.PrimaryKeyColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.TableBuilder;

public class PrimaryKeyAttributes extends ColumnAttributes {
	
	private static final DbColumnType UNID_PK_TYPE = DbColumnType
			.getByName("text(6)");
	private static final DbColumnType ID_PK_TYPE = DbColumnType
			.getByName("integer");

	public PrimaryKeyAttributes(String name, String type) {
		super(name, type, DbDataConstraint.UNIQUE, "null");
	}

	@Override
	public void addToBuilder(TableBuilder tableBuilder, Set<String> readableTables){
		if (getType() != null) {
			tableBuilder.addPrimaryKeyColumn(new PrimaryKeyColumn(getName(), DbColumnType.getByName(getType()), getConstraint()));
		} else {
			if (getName().equalsIgnoreCase("unid")) {
				tableBuilder.addPrimaryKeyColumn(new PrimaryKeyColumn(getName(), UNID_PK_TYPE, getConstraint()));
			} else if (getName().equalsIgnoreCase("id")) {
				tableBuilder.addPrimaryKeyColumn(new PrimaryKeyColumn(getName(), ID_PK_TYPE, getConstraint()));
			} else {
				throw new PrimaryKeyTypeException(getName());
			}
		}
	}
	
	public static class PrimaryKeyTypeException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public PrimaryKeyTypeException(String name) {
			super(name);
		}
	}
}
