package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;
import com.topdesk.si2011.dbgenerator.util.ReadCsv;

public class StreetAddressGenerator extends AbstractColumnGenerator {
	
	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
//		Map<String, Integer> readCsv = new ReadCsv("street_names.csv").readCsv();
		
		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer>builder();
		
		
		mapBuilder.putAll(new ReadCsv("street_names.csv").readCsv());
//		for(String key : readCsv.keySet()) {
//			key += " " + new Random().nextInt(300);
//			mapBuilder.put(key, 1);
//		}
		return mapBuilder.build();
	}


}
