package com.topdesk.si2011.dbgenerator.dbentry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DBTableEntryBuilderTest {
	@Test
	public void createTableEntry() {
		final String tableName = "Personeel";
		final String columnName1 = "Column1";
		final String columnName2 = "Column2";

		DbTableEntryBuilder builder = DbTableEntryBuilder.create(tableName);

		builder.addColumn(columnName1, "Hello World");
		builder.addColumn(columnName2, "15");

		IDbTableEntry newEntry = builder.build();

		assertEquals(tableName, newEntry.getTableName());
		for (String columnName : newEntry.getColumnNames()) {
			System.out.println(columnName);
			assertEquals(columnName, newEntry.getColumnEntry(columnName).getColumnLocation().getColumn());
		}
	}

	@Test(expected = IllegalStateException.class)
	public void createTableEntryAddSameColumnTwice() {
		final String tableName = "Personeel";
		final String columnName = "Name";

		DbTableEntryBuilder builder = DbTableEntryBuilder.create(tableName);

		builder.addColumn(columnName, "Hello World");

		builder.addColumn(columnName, "Hello World");

		builder.build();
	}

	@Test(expected=IllegalStateException.class)
	public void buildTableWithoutColumns() {
		final String tableName = "Personeel";

		DbTableEntryBuilder builder = DbTableEntryBuilder.create(tableName);

		builder.build();
	}

}
