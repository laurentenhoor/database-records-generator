package com.topdesk.si2011.dbgenerator.configurator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.storage.JSonStructureInterpreter;

public class GsonConfigTest {
	@Test
	public void testGetTableConfigByName() throws FileNotFoundException {
		ConfigReader conf = ConfigInterpreter.createConfigReader();

		TableConfig tableConfig = conf.getTableConfigByName("Table1");

		assertEquals(1, tableConfig.getColumnConfigs().size());
		assertEquals("aanmelderemail", tableConfig.getColumnConfigs().get(0)
				.getName());

		for (ColumnConfig columnConfig : tableConfig.getColumnConfigs()) {
			System.out.println(columnConfig);
		}
	}

	@Test
	public void testResetEntryAmount() {
		ConfigWriter writer = ConfigInterpreter.createConfigWriter();
		writer.resetAllEntryAmounts();

		assertTrue(writer.getTableConfigs().size() > 0);
		for (TableConfig tableConfig : writer.getTableConfigs()) {
			assertEquals(0, tableConfig.getEntryAmount());
		}
	}

	@Test
	public void testGenerateConfiguration() {
		IDbStructure structure = new JSonStructureInterpreter(
				JSonStructureInterpreter.REST_STRUCTURE_JSON_FILE)
				.createStructure();
		Configurator.generateConfiguration(structure);
	}

	@Test
	public void testSetEntryAmount() throws FileNotFoundException {
		ConfigWriter writer = ConfigInterpreter.createConfigWriter();
		writer.setEntryAmountByTableName("branch", 10);

		ConfigReader reader = ConfigInterpreter.createConfigReader();

		assertEquals(10, reader.getTableConfigByName("branch").getEntryAmount());
	}
}
