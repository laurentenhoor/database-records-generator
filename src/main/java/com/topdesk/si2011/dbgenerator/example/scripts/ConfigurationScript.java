package com.topdesk.si2011.dbgenerator.example.scripts;

import com.topdesk.si2011.dbgenerator.core.GenerationConfiguration;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;

public abstract class ConfigurationScript {
	public abstract void run(IDbStructure structure, GenerationConfiguration config);
}
