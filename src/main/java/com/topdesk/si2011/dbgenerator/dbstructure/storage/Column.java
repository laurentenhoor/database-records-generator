package com.topdesk.si2011.dbgenerator.dbstructure.storage;

import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;

class Column {

	private String name;
	private String type;
	private String defaultValue;
	private DbDataConstraint constraint;
	private String referenceTableColumn;

	public Column(String name, String type, String defaultValue,
			DbDataConstraint constraint, String referenceTableColumn) {
		this.setName(name);
		this.setType(type);
		this.setDefaultValue(defaultValue);
		this.setConstraint(constraint);
		this.setReferenceTableColumn(referenceTableColumn);
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setConstraint(DbDataConstraint constraint) {
		this.constraint = constraint;
	}

	public DbDataConstraint getConstraint() {
		return constraint;
	}

	public void setReferenceTableColumn(String referenceTableColumn) {
		this.referenceTableColumn = referenceTableColumn;
	}

	public String getReferenceTableColumn() {
		return referenceTableColumn;
	}
}
