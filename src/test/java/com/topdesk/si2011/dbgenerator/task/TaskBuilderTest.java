package com.topdesk.si2011.dbgenerator.task;

import org.junit.Test;

import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunicationFactory;
import com.topdesk.si2011.dbgenerator.core.GenerationConfiguration;
import com.topdesk.si2011.dbgenerator.core.GeneratorCore;
import com.topdesk.si2011.dbgenerator.core.WorkBench;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.storage.JSonStructureInterpreter;

public class TaskBuilderTest {

	@Test
	public void testBuild() {
		WorkBench workBench = new GeneratorCore(DatabaseCommunicationFactory.createRestCommunication("http://pc1595/tas/rest/"));
		workBench.loadStructureFromFile(JSonStructureInterpreter.REST_STRUCTURE_JSON_FILE);
		
		config(workBench.getCurrentStructure(), workBench.getCurrentConfiguration());
		
		workBench.generateDatabase();
	}

	private void config(IDbStructure structure, GenerationConfiguration config) {
		config.resetAllEntryAmounts();
		config.setEntryAmountByTableName("articleorderlink", 1);
		config.setEntryAmountByTableName("vat", 1);
		config.setEntryAmountByTableName("bestellingen", 3);
		config.setEntryAmountByTableName("object", 1);
		
		config.includeColumns(structure.getDependency("articleorderlink"));
		config.includeColumns(structure.getDependency("vat"));
		config.includeColumn(DbLocation.create("articleorderlink", "btwsoortid"));
		config.includeColumn(DbLocation.create("bestellingen", "attvrijedatum2"));
	}
}
