package com.topdesk.si2011.dbgenerator.communication;

import java.util.Set;

import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.BigDecimalColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.BinaryColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.BooleanColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.DateColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.DefaultColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.DoubleColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.IntegerColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.LongColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.MemoColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.TableBuilder;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.TextColumn;

public class ColumnAttributes {

	private final String name;
	private final String type;
	private final DbDataConstraint constraint;
	private final String defaultValue;

	public ColumnAttributes(String name, String type,
			DbDataConstraint constraint, String defaultValue) {
		this.name = name;
		this.type = type;
		this.constraint = constraint;
		this.defaultValue = defaultValue;

	}

	public String getName() {
		return name;
	}
	
	String getType() {
		return type;
	}

	public DefaultColumn getDefaultColumn() {
		if (type.startsWith("text")) {
			int parseInt = Integer.parseInt(type.substring(
					type.indexOf('(') + 1, type.length() - 1));
			return new TextColumn(name, parseInt, defaultValue, constraint);
		} else if (type.equals("memo")) {
			return new MemoColumn(name, defaultValue, constraint);
		} else if (type.equals("bigdecimal")) {
			return new BigDecimalColumn(name, defaultValue, constraint);
		} else if (type.equals("binary")) {
			return new BinaryColumn(name, defaultValue, constraint);
		} else if (type.equals("boolean")) {
			return new BooleanColumn(name, defaultValue, constraint);
		} else if (type.equals("date")) {
			return new DateColumn(name, defaultValue, constraint);
		} else if (type.equals("double")) {
			return new DoubleColumn(name, defaultValue, constraint);
		} else if (type.equals("integer")) {
			return new IntegerColumn(name, defaultValue, constraint);
		} else if (type.equals("long")) {
			return new LongColumn(name, defaultValue, constraint);
		}

		throw new RuntimeException("Unknown type " + type + " for column "
				+ name);
	}

	public DbDataConstraint getConstraint() {
		return constraint;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void addToBuilder(TableBuilder tableBuilder,
			Set<String> readableTables) {
			tableBuilder.addColumn(getDefaultColumn());

	}
}