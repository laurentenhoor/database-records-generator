package com.topdesk.si2011.dbgenerator.example.scripts;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.topdesk.si2011.dbgenerator.core.GenerationConfiguration;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.generator.ColumnGenerator;

public class CycleScript extends ConfigurationScript {

	@Override
	public void run(IDbStructure structure, GenerationConfiguration config) {
		config.setEntryAmountByTableName("person", 1);
		
		final DbLocation person_voornaam = DbLocation.create("person", "voornaam");
		final DbLocation person_achternaam = DbLocation.create("person", "achternaam");		
		
		config.setColumnGenerator(person_voornaam, new ColumnGenerator() {
			
			@Override
			public String pickItem(Map<String, Integer> distribution) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<DbLocation> getDependentColumns() {
				List<DbLocation> dependencies = Lists.newArrayList();
				
				dependencies.add(person_achternaam);
				
				return dependencies;
			}
			
			@Override
			public Map<String, Integer> createDistribution(
					Map<DbLocation, Object> generatedValues) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		config.setColumnGenerator(person_achternaam, new ColumnGenerator() {
			
			@Override
			public String pickItem(Map<String, Integer> distribution) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<DbLocation> getDependentColumns() {
				List<DbLocation> dependencies = Lists.newArrayList();
				
				dependencies.add(person_voornaam);
				
				return dependencies;
			}
			
			@Override
			public Map<String, Integer> createDistribution(
					Map<DbLocation, Object> generatedValues) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

}
