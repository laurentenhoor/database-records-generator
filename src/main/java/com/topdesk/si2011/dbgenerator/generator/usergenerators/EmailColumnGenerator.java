package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;

public class EmailColumnGenerator extends AbstractColumnGenerator {
	private static final DbLocation FIRST_NAME = new DbLocation("person", "voornaam");
	private static final DbLocation LAST_NAME = new DbLocation("person", "achternaam");
	private static final String DOMAIN = "@topdesk.com";;

	@Override
	public List<DbLocation> getDependentColumns() {
		return Lists.newArrayList(FIRST_NAME, LAST_NAME);
	}

	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedDependencies) {
		String firstName = (String) generatedDependencies.get(FIRST_NAME);
		String lastName = (String) generatedDependencies.get(LAST_NAME);
				
		String formalEmail = firstName.substring(0,1) + "." + lastName + DOMAIN;
		String informalEmail = firstName + lastName.substring(0,1) + DOMAIN;
		
		
		Map<String, Integer> histogram = new HashMap<String, Integer>();
		histogram.put(formalEmail, 2);
		histogram.put(informalEmail, 1);
		
		return ImmutableMap.copyOf(histogram);
	}
}
