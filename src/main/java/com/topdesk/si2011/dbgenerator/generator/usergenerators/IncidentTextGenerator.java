package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;
import com.topdesk.si2011.dbgenerator.util.ReadCsv;

public class IncidentTextGenerator extends AbstractColumnGenerator {

	public IncidentTextGenerator() {
		addDependency("incident_memo_history", "veldnaam");
	}

	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer> builder();

		if (generatedValues.get(new DbLocation("incident_memo_history", "veldnaam")).equals("ACTIE")) {
			mapBuilder.putAll(new ReadCsv("incident_actions.csv").readCsv());
		} else {
			mapBuilder.putAll(new ReadCsv("incident_requests.csv").readCsv());
		}
		
		return mapBuilder.build();
	}

}
