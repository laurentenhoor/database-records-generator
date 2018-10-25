package com.topdesk.si2011.dbgenerator.newgenerator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.topdesk.si2011.dbgenerator.util.ReadCsv;

public class ReadCsvTest {

	@Test
	public void testReadingCsvFile() {

		Map<String, Integer> distribution = new HashMap<String, Integer>();
		distribution = (new ReadCsv("config/examples/company_names.csv").readCsv());

		assertEquals(true, distribution.containsKey("Slagerij 'Leo van Vliet'"));
		assertEquals(Integer.valueOf(1), distribution.get("TOPdesk"));

	}

	@Test
	public void testReadingMultipleCsvFiles() {

		Map<String, Integer> distribution = new HashMap<String, Integer>();

		List<String> csvFiles = new ArrayList<String>();
		
		csvFiles.add("config/examples/men_firstnames.csv");
		csvFiles.add("config/examples/women_firstnames.csv");

		distribution.putAll(new ReadCsv(csvFiles).readCsv());

		assertEquals(true, distribution.containsKey("Daan"));
		assertEquals(Integer.valueOf(3), distribution.get("Lauren"));

	}
}
