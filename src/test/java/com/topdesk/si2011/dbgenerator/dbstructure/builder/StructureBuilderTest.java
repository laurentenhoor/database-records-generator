package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import static com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint.NOT_EMPTY;
import static com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint.NOT_NULL;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnType;
import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnTypeName;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;

public class StructureBuilderTest {

	@Test
	public void testBuildTable() {
		StructureBuilder builder = new StructureBuilderImpl();
		
		TableBuilder tableBuilder = builder.addTable("Table1");
		
		tableBuilder.addPrimaryKeyColumn(new PrimaryKeyColumn("unid", DbColumnType.getByName("text(6)"), NOT_EMPTY));
		tableBuilder.buildTable();
	}

	@Test
	public void testBuild() {
		StructureBuilder builder = new StructureBuilderImpl();
		
		TableBuilder tableBuilder = builder.addTable("Table1").addPrimaryKeyColumn(new PrimaryKeyColumn("unid", DbColumnType.getByName("text(6)"), NOT_NULL));
		tableBuilder.addColumn(new TextColumn("Name", 25, "Jan", null)).buildTable();
		
		IDbStructure structure = builder.build();
		
		assertEquals(1, structure.getTables().size());
		assertEquals("unid", structure.getTableByName("Table1").getPrimaryKeyColumn().getName());
	}
	
	@Test(expected=RuntimeException.class)
	public void testBuildWithoutPrimaryKey() {
		StructureBuilder builder = new StructureBuilderImpl();
		builder.addTable("Table1").addColumn(new TextColumn("unid", 6, "", NOT_NULL)).buildTable();
		IDbStructure structure = builder.build();
		
		assertEquals(1, structure.getTables().size());
		assertEquals("unid", structure.getTableByName("Table1").getPrimaryKeyColumn().getName());
	}
	
	@Test
	public void testKeepReferenceWhileBuilding() {
		StructureBuilder builder = new StructureBuilderImpl();
		TableBuilder tableBuilder = builder.addTable("Table1");
		tableBuilder.addColumn(new TextColumn("Column1", 6, "", NOT_NULL));
		tableBuilder.addPrimaryKeyColumn(new PrimaryKeyColumn("unid", DbColumnType.getByName("text(6)"), NOT_NULL));
		tableBuilder.addColumn(new TextColumn("Column2", 6, "", NOT_NULL));
		
		tableBuilder.buildTable();
		
		tableBuilder = builder.addTable("Table2");
		tableBuilder.addPrimaryKeyColumn(new PrimaryKeyColumn("unid", DbColumnType.getByName("text(6)"), NOT_NULL));
		tableBuilder.buildTable();
		
		IDbStructure structure = builder.build();
		
		assertEquals(2, structure.getTables().size());
		assertEquals("unid", structure.getTableByName("Table1").getPrimaryKeyColumn().getName());
		assertEquals("unid", structure.getTableByName("Table2").getPrimaryKeyColumn().getName());
	}
	
	@Test
	public void testReferenceTables() {
		StructureBuilder builder = new StructureBuilderImpl();
		TableBuilder tableBuilder = builder.addTable("Table1");
		tableBuilder.addColumn(new TextColumn("Column1", 6, "", NOT_NULL));
		tableBuilder.addPrimaryKeyColumn(new PrimaryKeyColumn("unid", DbColumnType.getByName("text(6)"), NOT_NULL));
		tableBuilder.addColumn(new TextColumn("Column2", 6, "", NOT_NULL));
		
		tableBuilder.buildTable();
		
		tableBuilder = builder.addTable("Table2");
		tableBuilder.addPrimaryKeyColumn(new PrimaryKeyColumn("unid", DbColumnType.getByName("text(6)"), NOT_NULL));
		tableBuilder.addForeignKeyColumn(new ForeignKeyColumn("table1Id", "", NOT_NULL), "Table1");
		tableBuilder.buildTable();
		
		IDbStructure structure = builder.build();
		
		IDbColumn referencedColumn = structure.getTableByName("Table2").getColumnByName("table1Id").getReferencedColumn();
		
		assertEquals(2, structure.getTables().size());
		assertEquals(true, structure.getTableByName("Table2").hasForeignKeys());
		assertEquals("unid", referencedColumn.getName());
		assertEquals(DbColumnTypeName.TEXT, referencedColumn.getType().getType());
	}
}
