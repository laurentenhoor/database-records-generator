package com.topdesk.si2011.dbgenerator.generator.usergenerators;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbLocation;
import com.topdesk.si2011.dbgenerator.generator.AbstractColumnGenerator;

public class InitialsGenerator extends AbstractColumnGenerator {
	
	private static final DbLocation FIRST_NAME = DbLocation.create("person", "voornaam");

	public InitialsGenerator() {
	addDependency(FIRST_NAME);
	
	}
	
	@Override
	public Map<String, Integer> createDistribution(Map<DbLocation, Object> generatedValues) {
		
		Builder<String, Integer> mapBuilder = ImmutableMap.<String, Integer> builder();
		
		Random random = new Random();
		
		String voornaam = (String) generatedValues.get(FIRST_NAME);
		String oneInitial = voornaam.substring(0, 1) + ".";
		String twoInitials = voornaam.substring(0, 1) + "." + randomCharAndDot(random);
		String threeInitials = voornaam.substring(0, 1) + "." + randomCharAndDot(random) + randomCharAndDot(random);
		
		
		mapBuilder.put(oneInitial, 5);
		mapBuilder.put(twoInitials, 1);
		mapBuilder.put(threeInitials, 2);
		
		return mapBuilder.build();
	}


	
	private String randomCharAndDot(Random random) {
		String character;
		do {
			character = String.valueOf(((char)(random.nextInt(26) + 'a')));
		} while (character.equals("x") || character.equals("q") || character.equals("y"));
		
		return character.toUpperCase() + ".";
	}
}
