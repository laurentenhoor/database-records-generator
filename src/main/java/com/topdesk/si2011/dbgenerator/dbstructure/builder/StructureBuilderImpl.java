package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;

public class StructureBuilderImpl implements StructureBuilder, TableBuilder {

	private List<DbTable> tables = Lists.newArrayList();
	private Map<ForeignKeyColumn, String> referenceTables = Maps.newHashMap();
	private DbTable currentTable;

	@Override
	public TableBuilder addTable(String name) {
		checkForUnbuiltTable();
		
		currentTable = new DbTable(name);
		return this;
	}

	@Override
	public TableBuilder addColumn(DefaultColumn column) {
		currentTable.addColumn(Preconditions.checkNotNull(column));
		return this;
	}

	@Override
	public TableBuilder addPrimaryKeyColumn(PrimaryKeyColumn column) {
		currentTable.addPrimaryKeyColumn(Preconditions.checkNotNull(column));
		return this;
	}

	@Override
	public TableBuilder addForeignKeyColumn(ForeignKeyColumn column, String referenceTable) {
		referenceTables.put(column, referenceTable);
		currentTable.addColumn(Preconditions.checkNotNull(column));
		return this;
	}

	@Override
	public StructureBuilder buildTable() {
		if (!Preconditions.checkNotNull(currentTable).hasPrimaryKey()) {
			throw new RuntimeException("Table " + currentTable
					+ " does not have a primary key");
		}

		tables.add(currentTable);

		currentTable = null;
		return this;
	}

	@Override
	public IDbStructure build() {
		checkForUnbuiltTable();
		
		resolveTableReferences();

		return new DbStructure(ImmutableList.<IDbTable>copyOf(tables));
	}
	
	private void checkForUnbuiltTable() {
		if(currentTable != null) {
			throw new RuntimeException("Table " + currentTable.getName()
					+ " has not been built");
		}
	}	
	
	private void resolveTableReferences() {
		for(ForeignKeyColumn column : referenceTables.keySet()) {
			column.setReferenceColumn(findTableByName(referenceTables.get(column)));
		}
	}
	
	private IDbColumn findTableByName(String referenceTableName) {
		for(IDbTable table : tables) {
			if(table.getName().equals(referenceTableName)) {
				return table.getPrimaryKeyColumn();
			}
		}
		
		throw new UnResolvedTableReferenceException(referenceTableName);
	}
}
