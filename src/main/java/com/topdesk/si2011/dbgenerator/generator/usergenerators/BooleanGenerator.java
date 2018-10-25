package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;

public class BooleanGenerator extends AbstractColumnGenerator {

	private int ratioTrue;
	private int ratioFalse;

	public BooleanGenerator(int ratioTrue, int ratioFalse) {
		this.ratioTrue = ratioTrue;
		this.ratioFalse = ratioFalse;
	}

	@Override
	public Map<String, Integer> createDistribution(
			Map<DbLocation, Object> generatedValues) {

		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer>builder();

		mapBuilder.put("true", ratioTrue);
		mapBuilder.put("false", ratioFalse);
		
		return mapBuilder.build();
	}

	
}
