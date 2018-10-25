package com.topdesk.si2011.dbgenerator.generator;

import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunication;
import com.topdesk.si2011.dbgenerator.configurator.ConfigReader;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;

public interface DatabaseGenerator {
	public void generate(ConfigReader configuration, IDbStructure structure, DatabaseCommunication dbComm);
}
