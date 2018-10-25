package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;

public class ZipCodeGenerator extends AbstractColumnGenerator {

	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		
		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer>builder();
		Random random = new Random();
		String zipCode = String.valueOf(random.nextInt(9000)+1000);
		zipCode += " " + randomChar(random) + randomChar(random);
		mapBuilder.put(zipCode , Integer.valueOf(1));
		return mapBuilder.build();
	}

	private String randomChar(Random random) {
		return String.valueOf(((char)(random.nextInt(26) + 'a'))).toUpperCase();
	}

}
