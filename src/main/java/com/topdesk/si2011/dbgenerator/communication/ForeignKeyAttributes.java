package com.topdesk.si2011.dbgenerator.communication;

import java.util.Set;

import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.ForeignKeyColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.TableBuilder;
import com.topdesk.si2011.dbgenerator.util.StringConversions;

public class ForeignKeyAttributes extends ColumnAttributes {

	private final String link;

	public ForeignKeyAttributes(String name, String type,
			DbDataConstraint constraint, String defaultValue, String link) {
		super(name, type, constraint, defaultValue);
		this.link = link;
	}

	@Override
	public void addToBuilder(TableBuilder tableBuilder,
			Set<String> readableTables) {
		
		String referenceTable = StringConversions.urlToName(link);
		
		if (!readableTables.contains(referenceTable)) {
			throw new UnknownReferenceTableException(getName(), referenceTable);
		}
		tableBuilder.addForeignKeyColumn(new ForeignKeyColumn(getName(),
				getDefaultValue(), getConstraint()), referenceTable);
		return;
	}

}
