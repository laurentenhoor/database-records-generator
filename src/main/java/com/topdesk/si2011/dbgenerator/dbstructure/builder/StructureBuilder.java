package com.topdesk.si2011.dbgenerator.dbstructure.builder;

import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;


public interface StructureBuilder {
	TableBuilder addTable(String name);
	IDbStructure build();
}
