package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;
import com.topdesk.si2011.dbgenerator.util.ReadCsv;

public class FirstNameGenerator extends AbstractColumnGenerator {

	public FirstNameGenerator() {
		addDependency("person", "geslacht");
	}
	
	@Override
	public List<DbLocation> getDependentColumns() {
		return dependencies;
	}

	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		
		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer>builder();
		
		DbLocation location = new DbLocation("person", "geslacht");
		
		if (generatedValues.get(location).equals("1")) {
			mapBuilder.putAll(new ReadCsv("men_firstnames.csv").readCsv());
			
		} else if (generatedValues.get(location).equals("2")) {
			mapBuilder.putAll(new ReadCsv("women_firstnames.csv").readCsv());
			
		} else {
			List<String> womenAndmen = Arrays.asList(
					new String[] {"men_firstnames.csv", "women_firstnames.csv"});
			
			mapBuilder.putAll(new ReadCsv(womenAndmen).readCsv());
		}

		return mapBuilder.build();
	}
	

}
