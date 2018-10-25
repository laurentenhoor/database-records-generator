package com.topdesk.si2011.dbgenerator.examples;

import com.topdesk.si2011.dbgenerator.Controller;
import com.topdesk.si2011.dbgenerator.example.scripts.MyFirstScript;

/**
 * Main class of the database generator
 * 
 * @author G.D.Eigenraam
 * 
 */
public class Main {
	private static final String JSON_STRUCTURE_URL = "structures/rest_structure/structure.json";
	private static final String DEFAULT_REST_URL = "http://pc1595/tas/rest/";
	
	public static void main(String[] args) {
		// Default method
		Controller.runWithScript(new MyFirstScript(), DEFAULT_REST_URL, "admin", "admin");
		
		// Alternative method
//		Controller.runWithScript(new MyFirstScript(), DEFAULT_REST_URL, "admin", "admin", JSON_STRUCTURE_URL);
		
		// Run structure browser
		Controller.runStructureBrowser(DEFAULT_REST_URL, "admin", "admin", JSON_STRUCTURE_URL);
	}
}
