package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;
import com.topdesk.si2011.dbgenerator.util.ReadCsv;

public class CsvBasedGenerator extends AbstractColumnGenerator {
	private String fileName;

	public CsvBasedGenerator(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		return ImmutableMap.<String, Integer> builder().putAll(new ReadCsv(fileName).readCsv()).build();
		
	}

}
