package com.topdesk.si2011.dbgenerator.dbentry;

import java.util.List;

/**
 * Container class for exchanging information about database entries.
 * 
 *
 */
public interface IDbTableEntry {
	/**
	 * Returns the name of the table for the entry
	 * 
	 * @return Table names
	 */
	String getTableName();
	
	/**
	 * Returns the names of the columns in the table entry
	 * 
	 * @return Column names
	 */
	List<String> getColumnNames();

	/**
	 * Returns the column entry information of a specific column
	 * 
	 * @see IDBColumnEntry
	 * 
	 * @param columnName Column name
	 * @return Entry information
	 */
	IDbColumnEntry getColumnEntry(String columnName);
}
