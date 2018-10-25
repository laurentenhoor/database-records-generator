package com.topdesk.si2011.dbgenerator.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.topdesk.si2011.dbgenerator.communication.parser.ParseException;
import com.topdesk.si2011.dbgenerator.communication.parser.ParsedEntry;
import com.topdesk.si2011.dbgenerator.communication.parser.Parser;
import com.topdesk.si2011.dbgenerator.dbentry.DbTableEntryBuilder;
import com.topdesk.si2011.dbgenerator.dbentry.IDbTableEntry;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;

public class RestCommunicationTest {

	private static final String REST_SERVER_URL = "http://pc1595/tas/rest/";
	private static DatabaseCommunication dbComm;

	@BeforeClass
	public static void setupOnce() {
		// Authenticator.setDefault(new MyAuthenticator());
		dbComm = DatabaseCommunicationFactory
				.createRestCommunication(REST_SERVER_URL);
	}

	@Test(timeout = 60000)
	public void testAnalyseDatabaseStructure() {
		IDbStructure structure = dbComm.createStructure();

		assertNotNull(structure);
		assertNotNull(structure.getTableByName("stock"));
	}

	@Test
	public void testFindAllPrimaryKeysFromTable() {
		final String tableName = "lmpactivity_memo_history";

		List<IDbTableEntry> pkEntries = dbComm
				.findAllPrimaryKeysFromTable(tableName);

		assertNotNull(pkEntries);
		for (IDbTableEntry tableEntry : pkEntries) {
			assertNotNull(tableEntry.getColumnEntry("unid"));
		}
	}

	@Test
	public void testFindAllEntriesFromTableText6Unid() {
		String tableName = "branch";
		List<IDbTableEntry> allEntries = dbComm
				.findAllEntriesFromTable(tableName);

		assertNotNull(allEntries);
		for (IDbTableEntry tableEntry : allEntries) {
			assertNotNull(tableEntry.getColumnEntry("unid"));
		}
	}

	@Test
	public void testFindAllEntriesFromTableIntegerUnid() {
		String tableName = "importcouple";
		List<IDbTableEntry> allEntries = dbComm
				.findAllEntriesFromTable(tableName);

		assertNotNull(allEntries);
		for (IDbTableEntry tableEntry : allEntries) {
			assertNotNull(tableEntry.getColumnEntry("id"));
		}
	}

	@Test
	public void testText6UnidTableEntryInsert() {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("addresstype");

		builder.addColumn("guicode", "DIT IS EEN TEST");
		builder.addColumn("archief", "9");
		builder.addColumn("rang", "2");
		builder.addColumn("naam", "123456");

		IDbTableEntry newEntry = builder.build();

		IDbTableEntry editedEntry = dbComm.insertTableEntry(newEntry);
		String unid = editedEntry.getColumnEntry("unid").getValue();

		try {
			ParsedEntry parser = Parser.parseEntry(ServerUrl.instance()
					.getEntryUrl("addresstype", unid));
			for (ColumnNameAndValue child : parser.getAllChildrenAndValues()) {
				if (child.getName().equals("guicode")) {
					assertEquals("DIT IS EEN TEST", child.getValue());
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testIntegerUnidTableEntryInsert() {
		DbTableEntryBuilder builder = DbTableEntryBuilder
				.create("importcouple");
		builder.addColumn("runid", "EA0001");
		builder.addColumn("source", "henk");
		builder.addColumn("target", "TEST");
		IDbTableEntry newEntry = builder.build();

		IDbTableEntry editedEntry = dbComm.insertTableEntry(newEntry);
		String unid = editedEntry.getColumnEntry("id").getValue();

		try {
			ParsedEntry parser = Parser.parseEntry(ServerUrl.instance()
					.getEntryUrl("importcouple", unid + "/integer"));
			for (ColumnNameAndValue child : parser.getAllChildrenAndValues()) {
				if (child.getName().equals("source")) {
					assertEquals("henk", child.getValue());
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	
	@Test (expected = RuntimeException.class)
	public void insertEmptyTableInLinkTable() {
		
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("articleorderlink");
		builder.addColumn("aantal", "1");
		IDbTableEntry newEntry = builder.build();
		
		
		System.out.println(dbComm.insertTableEntry(newEntry).getColumnEntry("unid").getValue());
		
	}
	

	
	@Test(expected = RuntimeException.class)
	public void testInsertTooLongTableEntry() {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("addresstype");

		builder.addColumn("guicode", "DIT IS EEN TEST");
		builder.addColumn("archief", "9");
		builder.addColumn("rang", "2");
		builder.addColumn("naam", "123456789123456789123456789000hsssssllo");

		IDbTableEntry newEntry = builder.build();
		dbComm.insertTableEntry(newEntry);
	}

	@Test(expected = IllegalStateException.class)
	public void testInsertEmpyTableEntry() {

		DbTableEntryBuilder builder = DbTableEntryBuilder.create("addresstype");
		IDbTableEntry newEntry = builder.build();
		dbComm.insertTableEntry(newEntry);

	}

	@Test
	public void testUpdateText6Unid() {
		DbTableEntryBuilder builder = DbTableEntryBuilder
				.create("abstractoperator");
		builder.addColumn("unid", "360003");
		builder.addColumn("accountmanager", "true");
		IDbTableEntry newEntry = builder.build();
		dbComm.updateTableEntry(newEntry);

		try {
			ParsedEntry parser = Parser.parseEntry(ServerUrl.instance()
					.getEntryUrl("abstractoperator", "360003"));
			for (ColumnNameAndValue child : parser.getAllChildrenAndValues()) {
				if (child.getName().equals("accountmanager")) {
					assertEquals("true", child.getValue());
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		DbTableEntryBuilder builder2 = DbTableEntryBuilder
				.create("abstractoperator");
		builder2.addColumn("unid", "360003");
		builder2.addColumn("accountmanager", "false");
		IDbTableEntry newEntry2 = builder2.build();
		dbComm.updateTableEntry(newEntry2);

		try {
			ParsedEntry parser = Parser.parseEntry(ServerUrl.instance()
					.getEntryUrl("abstractoperator", "360003"));
			for (ColumnNameAndValue child : parser.getAllChildrenAndValues()) {
				if (child.getName().equals("accountmanager")) {
					assertEquals("false", child.getValue());
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateIntegerId() {
		DbTableEntryBuilder builder = DbTableEntryBuilder
				.create("importcouple");
		builder.addColumn("id", "124");
		builder.addColumn("source", "test");
		builder.addColumn("target", "TEST");
		IDbTableEntry newEntry = builder.build();
		dbComm.updateTableEntry(newEntry);

		try {
			ParsedEntry parser = Parser.parseEntry(ServerUrl.instance()
					.getEntryUrl("importcouple", "124/integer"));
			for (ColumnNameAndValue child : parser.getAllChildrenAndValues()) {
				if (child.getName().equals("source")) {
					assertEquals("test", child.getValue());
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		DbTableEntryBuilder builder2 = DbTableEntryBuilder
				.create("importcouple");
		builder2.addColumn("id", "124");
		builder2.addColumn("source", "henk");
		builder2.addColumn("target", "TEST");
		IDbTableEntry newEntry2 = builder2.build();
		dbComm.updateTableEntry(newEntry2);

		try {
			ParsedEntry parser = Parser.parseEntry(ServerUrl.instance()
					.getEntryUrl("importcouple", "124/integer"));
			for (ColumnNameAndValue child : parser.getAllChildrenAndValues()) {
				if (child.getName().equals("source")) {
					assertEquals("henk", child.getValue());
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
