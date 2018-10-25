package com.topdesk.si2011.dbgenerator.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;

public abstract class AbstractColumnGenerator implements ColumnGenerator {

	protected final List<DbLocation> dependencies = new ArrayList<DbLocation>();
	
	public List<DbLocation> getDependentColumns() {
		return dependencies;
	}
	
	public abstract Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues);
	
	protected void addDependency(String tableName, String columnName) {
		addDependency(new DbLocation(tableName, columnName));
	}
	
	protected void addDependency(DbLocation location) {
		dependencies.add(location);
	}
	
	@Override
	final public String pickItem(Map<String, Integer> distribution) {
		
		return new HistogramItemPicker<String>(distribution).pick();
	}
}
