package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;

public class PhoneNumberGenerator extends AbstractColumnGenerator {

	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		Random random = new Random();
		
		String mobile = "06" + "-" + String.valueOf(random.nextInt(1000000000));
		String home = "0" + String.valueOf(random.nextInt(10) + 10)+ "-" + String.valueOf(random.nextInt(10000000));
		
		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer>builder();
		mapBuilder.put(mobile, 2);
		mapBuilder.put(home, 1);
		
		return mapBuilder.build();
	}

}
