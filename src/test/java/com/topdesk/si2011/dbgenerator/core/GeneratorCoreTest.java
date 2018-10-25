package com.topdesk.si2011.dbgenerator.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunicationFactory;
import com.topdesk.si2011.dbgenerator.dbstructure.storage.JSonStructureInterpreter;

public class GeneratorCoreTest {
	@Test
	public void testExtractStructure() {
		WorkBench workBench = new GeneratorCore(DatabaseCommunicationFactory.createRestCommunication("http://pc1595/tas/rest/"));

		assertNull(workBench.getCurrentStructure());
		workBench.extractStructureFromDatabase();		
		assertNotNull(workBench.getCurrentStructure());
		
	}
	
	@Test
	public void testLoadStructure() {
		WorkBench workBench = new GeneratorCore(DatabaseCommunicationFactory.createRestCommunication("http://pc1595/tas/rest/"));

		assertNull(workBench.getCurrentStructure());
		workBench.loadStructureFromFile(JSonStructureInterpreter.REST_STRUCTURE_JSON_FILE);
		assertNotNull(workBench.getCurrentStructure());
	}
	
	@Test
	public void testClearWorkbench() {
		WorkBench workBench = new GeneratorCore(DatabaseCommunicationFactory.createRestCommunication("http://pc1595/tas/rest/"));

		assertNull(workBench.getCurrentStructure());
		workBench.loadStructureFromFile(JSonStructureInterpreter.REST_STRUCTURE_JSON_FILE);		
		assertNotNull(workBench.getCurrentStructure());
		workBench.clear();
		assertNull(workBench.getCurrentStructure());
	}
}
