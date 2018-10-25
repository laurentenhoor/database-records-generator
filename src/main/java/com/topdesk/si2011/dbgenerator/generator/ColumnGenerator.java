package com.topdesk.si2011.dbgenerator.generator;

import java.util.List;
import java.util.Map;

import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

public interface ColumnGenerator {
	List<DbLocation> getDependentColumns();
	Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues);
	String pickItem(Map<String, Integer> distribution);
}
