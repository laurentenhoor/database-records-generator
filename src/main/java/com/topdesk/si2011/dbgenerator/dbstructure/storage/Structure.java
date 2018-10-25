package com.topdesk.si2011.dbgenerator.dbstructure.storage;

import java.util.List;

import com.google.common.collect.Lists;

class Structure {
	private List<Table> tables = Lists.newArrayList();
	
	public void addTable(Table table) {
		tables.add(table);
	}

	public List<Table> getTables() {
		return tables;
	}
}
