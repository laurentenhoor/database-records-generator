package com.topdesk.si2011.dbgenerator.communication;

import java.net.Authenticator;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.topdesk.si2011.dbgenerator.dbentry.DbTableEntryBuilder;
import com.topdesk.si2011.dbgenerator.dbentry.IDbTableEntry;

public class IncidentGenerationTest {
	private static final String REST_SERVER_URL = "http://pc1595/tas/rest/";
	private static DatabaseCommunication dbComm;

	@BeforeClass
	public static void setupOnce() {
		Authenticator.setDefault(new MyAuthenticator());
		dbComm = DatabaseCommunicationFactory
				.createRestCommunication(REST_SERVER_URL);
		dbComm.createStructure();
	}

	@Ignore
	@Test
	public void deleteSomeTables(){
		dbComm.deleteDatabaseTable("classification");
	}

	

	@Test
	public void insertNiceIncidentWithDependencies() {
		
		String branchId = insertVestiging();
		String afdelingId = insertAfdeling();
		String budgetholderId = insertKostenplaats();
		
		String categorieId = insertCategorie();
		String subCategorieId = insertSubCategorie(categorieId);
		
		String personId = insertPerson(afdelingId, branchId, budgetholderId);
		
		String incidentId = insertIncident(personId,categorieId, subCategorieId);
		

		insertActieMemo(incidentId);
		insertVerzoekMemo(incidentId);
		
	}

	private String insertVestiging() {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("branch");
		builder.addColumn("naam", "GOEDbureau");
		builder.addColumn("plaats1", "Delft");
		builder.addColumn("straat1", "Voorstraat 42");
		builder.addColumn("postcode1", "2611 JR");
		builder.addColumn("telefoon", "015-231549469");
		builder.addColumn("email", "info@goedbureau.nl");

		IDbTableEntry newEntry = builder.build();
		return dbComm.insertTableEntry(newEntry).getColumnEntry("unid").getValue();
	}
	
	private String insertAfdeling() {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("department");
		builder.addColumn("naam", "WC-schoonmaak Afdeling");

		IDbTableEntry newEntry = builder.build();
		return dbComm.insertTableEntry(newEntry).getColumnEntry("unid").getValue();
	}
	
	private String insertKostenplaats() {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("budgetholder");
		builder.addColumn("naam", "Test");

		IDbTableEntry newEntry = builder.build();
		return dbComm.insertTableEntry(newEntry).getColumnEntry("unid").getValue();
	}

	private String insertCategorie() {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("classification");
		builder.addColumn("naam", "Vogels");			
		builder.addColumn("state", "1");
		builder.addColumn("incidentbeheer", "true");
		
		IDbTableEntry newEntry = builder.build();
		return dbComm.insertTableEntry(newEntry).getColumnEntry("unid").getValue();
	}

	private String insertSubCategorie(String categorie) {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("classification");
		builder.addColumn("naam", "Meeuwen");
		builder.addColumn("state", "1");
		builder.addColumn("incidentbeheer", "true");
		builder.addColumn("parentid", categorie);
		
		IDbTableEntry newEntry = builder.build();
		return dbComm.insertTableEntry(newEntry).getColumnEntry("unid").getValue();
	}

	private String insertPerson(String afdelingid, String branchId, String budgetholderId) {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("person");
		builder.addColumn("achternaam", "Hoor");
		builder.addColumn("tussenvoegsel", "ten");
		builder.addColumn("voornaam", "Lauren");
		builder.addColumn("voorletters", "G.L.J.");
		builder.addColumn("vestigingid", branchId);
		builder.addColumn("email", "laurentenhoor@domein.nl");
		builder.addColumn("geslacht", "1");
		
		builder.addColumn("budgethouderid", budgetholderId);
		
		builder.addColumn("afdelingid", afdelingid);

		IDbTableEntry newEntry = builder.build();
		return dbComm.insertTableEntry(newEntry).getColumnEntry("unid").getValue();
	}
	
	private String insertIncident(String personid, String categorieId, String subCategorieId){
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("incident");

		builder.addColumn("persoonid", personid);
		// Categorie wordt automatisch afgeleid uit de hoofdcategorie
		builder.addColumn("incident_specid", subCategorieId);

		IDbTableEntry newEntry = builder.build();
		return dbComm.insertTableEntry(newEntry).getColumnEntry("unid").getValue();
	}
	
	
	private void insertVerzoekMemo(String incidentId) {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("incident_memo_history");
		builder.addColumn("parentid", incidentId);
		builder.addColumn("veldnaam", "VERZOEK");
		builder.addColumn("memotekst",
				"Ik heb een knoop in m'n zakdoek!!");
		IDbTableEntry newEntry = builder.build();
		dbComm.insertTableEntry(newEntry);
	}
	

	private void insertActieMemo(String incidentId) {
		DbTableEntryBuilder builder = DbTableEntryBuilder.create("incident_memo_history");
		builder.addColumn("parentid", incidentId);
		builder.addColumn("veldnaam", "ACTIE");
		builder.addColumn("memotekst", "Ik heb de knoop doorgehakt");
		IDbTableEntry newEntry = builder.build();
		dbComm.insertTableEntry(newEntry);
	}
	
	
}
