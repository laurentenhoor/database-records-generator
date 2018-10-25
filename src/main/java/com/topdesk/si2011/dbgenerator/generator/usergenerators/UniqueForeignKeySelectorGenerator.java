package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;

public class UniqueForeignKeySelectorGenerator extends AbstractColumnGenerator {

	private final DbLocation referenceColumnLocation;
	private final List<String> unselectedActionUnids = new ArrayList<String>();

	public UniqueForeignKeySelectorGenerator(String tableName, String columnName) {
		DbLocation referenceColumnLocation = new DbLocation(tableName, columnName);
		this.referenceColumnLocation = referenceColumnLocation;
		dependencies.add(referenceColumnLocation);
	}

	@Override
	public List<DbLocation> getDependentColumns() {
		return dependencies;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		if (unselectedActionUnids.size() < 1) {
			this.unselectedActionUnids.addAll((List<String>) generatedValues.get(referenceColumnLocation));
		}
		
		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer> builder();
		
		String	selectedUnid = (String) unselectedActionUnids.get(new Random().nextInt(unselectedActionUnids.size()));
			unselectedActionUnids.remove(selectedUnid);
	
		mapBuilder.put(selectedUnid, 1);
		
		return mapBuilder.build();
	}

}
