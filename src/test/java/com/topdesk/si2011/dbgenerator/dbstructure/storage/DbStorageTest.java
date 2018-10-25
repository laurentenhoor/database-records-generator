package com.topdesk.si2011.dbgenerator.dbstructure.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunication;
import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunicationFactory;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;
import com.topdesk.si2011.dbgenerator.dbstructure.StructureInterpreter;

public class DbStorageTest {

	private static final String STRUCTURE_JSON = "structures/rest_structure/structure.json";
	private static IDbStructure realDatabaseStructure;

	@BeforeClass
	public static void setup() {
		DatabaseCommunication dbComm = DatabaseCommunicationFactory
				.createRestCommunication("http://pc1595/tas/rest/");
		realDatabaseStructure = dbComm.createStructure();
	}

	@Test
	public void testSaveStructure() {
		JSonStructureInterpreter storage = new JSonStructureInterpreter(
				STRUCTURE_JSON);
		storage.backupStructure(realDatabaseStructure);
		
		assertTrue(new File(STRUCTURE_JSON).exists());
	}

	@Test
	public void testLoadStructure() {
		StructureInterpreter storage = new JSonStructureInterpreter(
				STRUCTURE_JSON);
		
		IDbStructure loadedDatabaseStructure = storage.createStructure();

		assertNotNull(loadedDatabaseStructure);
		assertTrue(loadedDatabaseStructure.getTables().size() > 0);
		
		for (IDbTable table : realDatabaseStructure.getTables()) {
			assertEquals(table.toString(), loadedDatabaseStructure
					.getTableByName(table.getName()).toString());
		}
	}
}
