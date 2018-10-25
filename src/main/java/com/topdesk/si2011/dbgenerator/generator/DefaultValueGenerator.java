package com.topdesk.si2011.dbgenerator.generator;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

public class DefaultValueGenerator extends AbstractColumnGenerator {

	private final String defaultValue;

	public DefaultValueGenerator(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		return ImmutableMap.<String, Integer> builder().put(defaultValue, 1).build();
	}
}
