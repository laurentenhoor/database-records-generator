package com.topdesk.si2011.dbgenerator.configurator;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;


public class GSonTableConfig implements TableConfig {
	private String name;
	private int entryAmount;
	
	private List<GSonColumnConfig> columnsConfig = new ArrayList<GSonColumnConfig>();

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public List<GSonColumnConfig> getWritableColumnConfigs() {
		return columnsConfig;
	}
	
	@Override
	public List<ColumnConfig> getColumnConfigs() {
		return ImmutableList.<ColumnConfig>copyOf(columnsConfig);
	}
	
	@Override
	public GSonColumnConfig getColumnConfigByName(String name) {
		for(GSonColumnConfig config : columnsConfig) {
			if(config.getName().equals(name)) {
				return config;
			}
		}
		
		return null;
	}
	
	public void addColumnConfig(GSonColumnConfig config) {
		columnsConfig.add(config);
	}
	
	public void removeColumnConfig(GSonColumnConfig config) {
		columnsConfig.remove(config);
	}
	
	public void removeColumnConfigByName(String name) {
		removeColumnConfig(getColumnConfigByName(name));
	}
	
	@Override
	public String toString() {
		return "TableConfig:" + columnsConfig;
	}

	public void setEntryAmount(int amount) {
		this.entryAmount = amount;
	}
	
	@Override
	public int getEntryAmount() {
		return entryAmount;
	}
}
