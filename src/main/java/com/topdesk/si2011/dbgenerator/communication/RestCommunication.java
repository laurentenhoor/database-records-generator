package com.topdesk.si2011.dbgenerator.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.topdesk.si2011.dbgenerator.communication.parser.ParseException;
import com.topdesk.si2011.dbgenerator.communication.parser.Parser;
import com.topdesk.si2011.dbgenerator.dbentry.DbTableEntryBuilder;
import com.topdesk.si2011.dbgenerator.dbentry.IDbTableEntry;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.StructureInterpreter;
import com.topdesk.si2011.dbgenerator.dbstructure.storage.JSonStructureInterpreter;


/**
 * Implementation of the communication for REST
 * Note: hardcoded difference between primary key fields with name 'unid' and 'id'.
 * This is a difference between primary keys of type Text(6) [unid] and Integer [id]
 */
public class RestCommunication implements DatabaseCommunication {

	private IDbStructure dbStructureCopy;

	@Override
	public void setStructure(IDbStructure structure) {
		this.dbStructureCopy = structure;
	}

	private IDbStructure getStructure() {
		if (dbStructureCopy == null) {
			
			
			StructureInterpreter restStructureInterpreter = new RestStructureInterpreter();
			dbStructureCopy = restStructureInterpreter.createStructure();
		
//			dbStructureCopy = new JSonStructureInterpreter("structures/rest_structure/structure.json").createStructure();
		}

		return dbStructureCopy;
	}

	@Override
	public IDbStructure createStructure() {
		return getStructure();
	}
	
	public IDbStructure createStructureFromJson(String fileLocation) {
		return new JSonStructureInterpreter(fileLocation).createStructure();
	}

	@Override
	public List<IDbTableEntry> findAllPrimaryKeysFromTable(String tableName) {

		List<IDbTableEntry> entryList = new ArrayList<IDbTableEntry>();
		String url = String.format(ServerUrl.instance().getTableUrl(tableName));

		try {
			for (String unid : Parser.parseTable(url).getUnids()) {
				DbTableEntryBuilder builder = DbTableEntryBuilder
						.create(tableName);

				builder.addColumn(getStructure().getTableByName(tableName)
						.getPrimaryKeyColumn().getName(), unid);
				entryList.add(builder.build());
			}
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		}
		return entryList;
	}

	@Override
	public List<IDbTableEntry> findAllEntriesFromTable(String tableName) {

		List<IDbTableEntry> entryList = new ArrayList<IDbTableEntry>();
		String url = String.format(ServerUrl.instance().getTableUrl(tableName));
		try {
			for (String unid : Parser.parseTable(url).getUnids()) {

				DbTableEntryBuilder builder = DbTableEntryBuilder
						.create(tableName);
				builder.addColumn(getStructure().getTableByName(tableName)
						.getPrimaryKeyColumn().getName(), unid);

				String unidUrl;
				if (getStructure().getTableByName(tableName)
						.getPrimaryKeyColumn().getName().equals("unid")) {
					unidUrl = String.format(ServerUrl.instance().getEntryUrl(
							tableName, unid));
				} else {
					unidUrl = String.format(ServerUrl.instance()
							.getEntryUrlIntegerId(tableName, unid));
				}
				try {
					for (ColumnNameAndValue column : Parser.parseEntry(unidUrl)
							.getAllChildrenAndValues()) {

						if (tableIsNotReadOnly(column.getName())) {
							if (!(column.getName().equals("unid") || column
									.getName().equals("id"))) {
								builder.addColumn(column.getName(),
										column.getValue());
							}
						}
					}
				} catch (ParseException e) {
					System.err.println(e.getMessage());
				}
				entryList.add(builder.build());
			}
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		}
		return entryList;
	}

	private boolean tableIsNotReadOnly(String tableName) {
		if (getStructure().getTableNames().contains(tableName)) {
			return true;
		}
		return false;
	}

	@Override
	public IDbTableEntry insertTableEntry(IDbTableEntry newEntry) {

		try {
			String tableName = newEntry.getTableName();

			String docAsString = new XmlBuilder(newEntry, dbStructureCopy.getTableByName(tableName)).getDocAsString();
			String newUnid = null;

			try {
				newUnid = RestRequests.doPost(tableName, docAsString);
			} catch (IOException e) {
				e.printStackTrace();
			}

			DbTableEntryBuilder builder = DbTableEntryBuilder.create(tableName);
			builder.addColumn(getStructure().getTableByName(tableName)
					.getPrimaryKeyColumn().getName(), newUnid);
			for (String column : newEntry.getColumnNames()) {
				builder.addColumn(column, newEntry.getColumnEntry(column)
						.getValue());
			}
			return builder.build();
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	@Override
	public IDbTableEntry updateTableEntry(IDbTableEntry newEntry) {
		try {
			String tableName = newEntry.getTableName();
			String document = new XmlBuilder(newEntry, dbStructureCopy.getTableByName(tableName)).getDocAsString();
			String unidValue;
			if (newEntry.getColumnEntry("id") != null) {
				unidValue = newEntry.getColumnEntry("id").getValue()
						+ "/integer";
			} else {
				unidValue = newEntry.getColumnEntry("unid").getValue();
			}
			RestRequests.doPut(tableName, document, unidValue);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newEntry;
	}

	@Override
	public void deleteDatabaseTable(String tableName) {
		try {
			String url = String.format(ServerUrl.instance().getTableUrl(
					tableName));
			for (String unid : Parser.parseTable(url).getUnids()) {
				try {
					if (getStructure().getTableByName(tableName)
							.getPrimaryKeyColumn().getName().equals("unid")) {
						RestRequests.doDelete(tableName, unid);
					} else {
						RestRequests.doDelete(tableName, unid + "/integer");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
