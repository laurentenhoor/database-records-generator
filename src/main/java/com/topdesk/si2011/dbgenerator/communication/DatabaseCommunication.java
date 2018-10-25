package com.topdesk.si2011.dbgenerator.communication;

import java.util.List;

import com.topdesk.si2011.dbgenerator.dbentry.IDbTableEntry;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.StructureInterpreter;


/**
 * Interface for the database communication. Children of this interface will provide functionalities to extract 
 * the structural information about their specific database storage and allow changes to be applied to the database.
 * 
 *
 */
public interface DatabaseCommunication extends StructureInterpreter {
	void setStructure(IDbStructure structure);
	
	/**
	 * Finds all primary keys from a given table. 
	 * 
	 * @throws NullPointerException when given tableName is {@code null}
	 * @param tableName Table name
	 * @return {@link IDBTableEntry} with the results of the search query. When the table does not exists it returns {@code null}.
	 */
	public List<IDbTableEntry> findAllPrimaryKeysFromTable(String tableName);
	
	/**
	 * Finds all table entries from a given table. 
	 * 
	 * @throws NullPointerException when given tableName is {@code null}
	 * @param tableName Table name
	 * @return {@link IDBTableEntry} with the results of the search query. When the table does not exists it returns {@code null}.
	 */
	public List<IDbTableEntry> findAllEntriesFromTable(String tableName);
	
	/**
	 * Inserts a new entry in the database. 
	 * 
	 * @throws NullPointerException when given table entry is {@code null}
	 * @param newEntry Information about the new entry
	 * @return {@link IDBTableEntry} with the results of the insert query. When the insert query failed, it returns {@code null}.
	 */
	public IDbTableEntry insertTableEntry(IDbTableEntry newEntry);
	
	/**
	 * Updates given column entries of an existing table entry in the database. 
	 * 
	 * @throws NullPointerException when given table entry is {@code null}
	 * @param newEntry Information about the to be updated entry
	 * @return {@link IDBTableEntry} with the results of the update query. When the update query failed, it returns {@code null}.
	 */
	public IDbTableEntry updateTableEntry(IDbTableEntry newEntry);
	
	/**
	 * Deletes all entries within a given Table. 
	 * It will only delete the entries without external references to them.
	 * 
	 * @throws NullPointerException when given table name is {@code null}
	 * @param tableName Information about the to be updated entry
	 * 
	 */
	public void deleteDatabaseTable(String tableName);
	

}
