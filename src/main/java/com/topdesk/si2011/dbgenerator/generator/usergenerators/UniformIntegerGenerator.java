package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;

public class UniformIntegerGenerator extends AbstractColumnGenerator {
	private int lowerBound;
	private int upperBound;

	public UniformIntegerGenerator(Integer lowerBound, Integer upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}

	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer>builder();
		for (int i = lowerBound; i <= upperBound; i++) {
			mapBuilder.put(""+i, 1);
		}
		return mapBuilder.build();
	}
}
