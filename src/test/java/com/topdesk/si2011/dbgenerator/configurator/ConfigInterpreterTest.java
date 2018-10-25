package com.topdesk.si2011.dbgenerator.configurator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunicationFactory;

public class ConfigInterpreterTest {

	@Test
	public void testGenerateConfigByStructure() {
		ConfigWriter writer = ConfigInterpreter.createConfigWriter();
		
		writer.generateConfiguration(DatabaseCommunicationFactory.createRestCommunication("http://pc1595/tas/rest/").createStructure());
	}
	
	@Test
	public void testCreateConfigReader() {
		ConfigReader reader = ConfigInterpreter.createConfigReader();
		
		assertEquals(488, reader.getTableConfigs().size());
	}

	@Test
	public void testCreateConfigWriter() {
		ConfigWriter writer = ConfigInterpreter.createConfigWriter();
		
		assertEquals(488, writer.getTableConfigs().size());
	}
}
