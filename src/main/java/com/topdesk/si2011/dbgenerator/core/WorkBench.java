package com.topdesk.si2011.dbgenerator.core;

import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;

/**
 * Database Generator core functionalities for performing database analysis tasks and maintaining the database structure. 
 * 
 * @author G.D.Eigenraam
 *
 */
public interface WorkBench {
	/**
	 * Clears the workbench of any information of the current project (Structure, configurations profile...)
	 */
	void clear();
	
	/**
	 * Extracts the structure from the database and stores it in-memory.
	 */
	void extractStructureFromDatabase();
	
	/**
	 * Gives the current in-memory database structure.
	 * 
	 * @return in-memory database structure
	 */
	IDbStructure getCurrentStructure();

	/**
	 * Gives the current generation configuration.
	 * 
	 * @return Generation configuration
	 */
	GenerationConfiguration getCurrentConfiguration();
	
	/**
	 * Creates a JSon format representation of the in-memory database structure and stores it
	 */
	void saveStructureToFile(String filename);
	
	/**
	 * Reads a database structure from a JSon file and loads it into the in-memory database structure
	 * @param fileLocation
	 */
	void loadStructureFromFile(String filename);
		
	/**
	 * Generates the database.
	 */
	void generateDatabase();


}
