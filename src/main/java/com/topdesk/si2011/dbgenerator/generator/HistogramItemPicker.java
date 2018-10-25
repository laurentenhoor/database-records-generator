package com.topdesk.si2011.dbgenerator.generator;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Ordering;

public class HistogramItemPicker<E> implements RandomItemPicker<E> {
	private static final Random random = new Random();
	
	private final ImmutableSortedMap<Integer, E> cumulativeOccurenceMap;
	
	private int total;
	
	public HistogramItemPicker(Map<E, Integer> occurenceMap) {
		Builder<Integer, E> mapBuilder = new ImmutableSortedMap.Builder<Integer, E>(Ordering.natural());
		total = 0;
		for (E key: occurenceMap.keySet()) {
			total += occurenceMap.get(key);
			mapBuilder.put(total, key);
		}
		
		cumulativeOccurenceMap = mapBuilder.build();
	}
	
	@Override
	public E pick() {
		int pick = random.nextInt(total) + 1;
	
		for (int occurence : cumulativeOccurenceMap.keySet()) {
			if (pick <= occurence) {
				return cumulativeOccurenceMap.get(occurence);
			}
		}
		
		throw new RuntimeException("Picking item failed, pick: " + pick + " maximum: " + total);
	}
}
