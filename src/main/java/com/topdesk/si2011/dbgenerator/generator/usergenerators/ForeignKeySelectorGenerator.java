package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;

public class ForeignKeySelectorGenerator extends AbstractColumnGenerator {

	private final DbLocation referenceColumnLocation;

	public ForeignKeySelectorGenerator(String tableName, String columnName) {
		DbLocation referenceColumnLocation = new DbLocation(tableName, columnName);
		this.referenceColumnLocation = referenceColumnLocation;
		dependencies.add(referenceColumnLocation);
	}
	
	public ForeignKeySelectorGenerator(DbLocation referenceColumnLocation) {
		this.referenceColumnLocation = referenceColumnLocation;
		dependencies.add(referenceColumnLocation);
	}

	@Override
	public List<DbLocation> getDependentColumns() {
		return dependencies;
	}

	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		@SuppressWarnings("unchecked")
		List<Object> generatedObjects = (List<Object>) generatedValues.get(referenceColumnLocation);

		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer> builder();

		for (Object generatedObject : generatedObjects) {
			mapBuilder.put((String) generatedObject, 1);
		}
		return mapBuilder.build();
	}

}
