package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;
import com.topdesk.si2011.dbgenerator.util.ReadCsv;

public class UniqueItemGenerator extends AbstractColumnGenerator{

	private String fileName;
	Map<String, Integer> values;
	
	public UniqueItemGenerator(String fileName) {
		this.fileName = fileName;
		values = new ReadCsv(fileName).readCsv();
	}

	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		if (values.size() < 1){
			this.values = new ReadCsv(fileName).readCsv();
		}
		
		int random = new Random().nextInt(values.keySet().size());
		List<String> list = new ArrayList<String>(values.keySet());
		String selectedValue = list.get(random);
		values.remove(selectedValue);
		
		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer>builder();
		mapBuilder.put(selectedValue, 1);
		return mapBuilder.build();
	}

}
