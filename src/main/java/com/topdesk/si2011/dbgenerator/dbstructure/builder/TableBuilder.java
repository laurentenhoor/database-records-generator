package com.topdesk.si2011.dbgenerator.dbstructure.builder;

public interface TableBuilder {
	TableBuilder addColumn(DefaultColumn column);
	TableBuilder addPrimaryKeyColumn(PrimaryKeyColumn column);
	TableBuilder addForeignKeyColumn(ForeignKeyColumn column, String referenceTable);
	
	StructureBuilder buildTable();
}
