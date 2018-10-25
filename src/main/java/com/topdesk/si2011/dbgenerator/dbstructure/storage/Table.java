package com.topdesk.si2011.dbgenerator.dbstructure.storage;

import java.util.ArrayList;
import java.util.List;

class Table {
	private String name;
	private String primaryKeyColumn;
	private List<Column> columns = new ArrayList<Column>();

	public Table(String name, String primaryKeyColumn) {
		this.name = name;
		this.primaryKeyColumn = primaryKeyColumn;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPrimaryKeyColumn(String primaryKeyColumn) {
		this.primaryKeyColumn = primaryKeyColumn;
	}
	
	public String getPrimaryKeyColumn() {
		return primaryKeyColumn;
	}
	
	public List<Column> getColumns() {
		return columns;
	}

	public void addColumn(Column newColumn) {
		columns.add(newColumn);
	}

}
