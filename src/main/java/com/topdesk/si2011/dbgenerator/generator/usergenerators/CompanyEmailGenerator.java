package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;

public class CompanyEmailGenerator extends AbstractColumnGenerator {

	@Override
	public List<DbLocation> getDependentColumns() {
		addDependency("branch", "naam");
		return super.getDependentColumns();
	}
	
	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		
		return ImmutableMap.<String, Integer> builder().put("info@" + generatedValues.get(new DbLocation("branch", "naam"))+ ".com", 1).build();

	}

}
