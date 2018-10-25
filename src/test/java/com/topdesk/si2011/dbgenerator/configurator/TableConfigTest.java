package com.topdesk.si2011.dbgenerator.configurator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TableConfigTest {

	@Test
	public void testAddColumnConfig() {
		GSonTableConfig tableConfig = new GSonTableConfig();
		tableConfig.setName("Table1");
		tableConfig
				.addColumnConfig(new GSonColumnConfig(tableConfig.getName()));

		assertEquals(1, tableConfig.getColumnConfigs().size());
	}

	@Test
	public void testRemoveColumnConfig() {
		GSonTableConfig tableConfig = new GSonTableConfig();
		tableConfig.setName("Table1");
		
		GSonColumnConfig columnConfig = new GSonColumnConfig(tableConfig.getName());
		tableConfig.addColumnConfig(columnConfig);
		tableConfig.removeColumnConfig(columnConfig);

		assertEquals(0, tableConfig.getColumnConfigs().size());
	}

	@Test
	public void testRemoveColumnConfigByName() {
		GSonTableConfig tableConfig = new GSonTableConfig();
		tableConfig.setName("Table1");
		
		GSonColumnConfig columnConfig = new GSonColumnConfig(tableConfig.getName());
		columnConfig.setName("Daan");
		tableConfig.addColumnConfig(columnConfig);
		tableConfig.removeColumnConfigByName("Daan");

		assertEquals(0, tableConfig.getColumnConfigs().size());
	}

}
