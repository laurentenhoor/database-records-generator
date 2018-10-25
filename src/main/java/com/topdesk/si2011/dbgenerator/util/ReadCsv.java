package com.topdesk.si2011.dbgenerator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ReadCsv {

	private final List<String> filePaths;
	private final static String CSV_PATH_PREFIX = "config/examples/";

	public ReadCsv(String fileName) {
		List<String> filePaths = new ArrayList<String>();
		filePaths.add(CSV_PATH_PREFIX + fileName);
		this.filePaths = filePaths;
	}

	public ReadCsv(List<String> fileNames) {
		List<String> filePaths = new ArrayList<String>();
		for (String fileName : fileNames){
			filePaths.add(CSV_PATH_PREFIX + fileName);
		}
		this.filePaths = filePaths;
	}

	public Map<String, Integer> readCsv() {

		Map<String, Integer> valueAndDistribution = new HashMap<String, Integer>();

		for (String filePath : filePaths) {
			interpretCsvFile(valueAndDistribution, filePath);
		}

		return valueAndDistribution;
	}


	private void interpretCsvFile(Map<String, Integer> valueAndDistribution,
			String filePath) throws RuntimeException, NumberFormatException,
			NullPointerException {

		File csvFile = new File(filePath);

		if (!csvFile.exists()) {
			throw new RuntimeException("File " + csvFile.getAbsolutePath() + " does not exist");
		}

		BufferedReader bufRdr = null;

		try {
			bufRdr = new BufferedReader(new FileReader(csvFile));
			String line = null;

			String name = "";
			Integer value = -1;

			while ((line = bufRdr.readLine()) != null) {
				StringTokenizer activeLine = new StringTokenizer(line, ";");

				if (activeLine.hasMoreTokens()) {
					name = activeLine.nextToken();
				}
				if (activeLine.hasMoreTokens()) {

					try {
						value = Integer.valueOf(activeLine.nextToken());
					} catch (NumberFormatException e) {
						throw new RuntimeException("The second column of the CSV must contain integers. File: " + filePath);
					}

				} else {
					value = Integer.valueOf(1);
				}
				if (activeLine.hasMoreTokens()) {
					throw new RuntimeException(
							"Expected a maximum of 2 columns per row in CSV file: " + filePath);
				}
				if (name.equals("") || value.equals(-1)) {
					throw new RuntimeException("CSV contains empty value(s)! File: " + filePath);
				}
				if (valueAndDistribution.containsKey(name)) {
					value += valueAndDistribution.get(name);
				}

				valueAndDistribution.put(name, value);
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufRdr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
