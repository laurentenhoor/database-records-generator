package com.topdesk.si2011.dbgenerator.example.scripts;

import com.topdesk.si2011.dbgenerator.core.GenerationConfiguration;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.CompanyEmailGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.CsvBasedGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.EmailColumnGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.FirstNameGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.ForeignKeySelectorGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.IncidentTextGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.InitialsGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.PhoneNumberGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.StreetAddressGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.UniformIntegerGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.UniqueForeignKeySelectorGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.UniqueItemGenerator;
import com.topdesk.si2011.dbgenerator.generator.usergenerators.ZipCodeGenerator;
import com.topdesk.si2011.dbgenerator.util.ReadCsv;

public class MyFirstScript extends ConfigurationScript {

	@Override
	public void run(IDbStructure structure, GenerationConfiguration config) {
		
		// Person table configuration
		config.setEntryAmountByTableName("person", 20);
		config.setColumnGenerator("person", "geslacht", new UniformIntegerGenerator(1, 2));
		config.setColumnGenerator("person", "voornaam", new FirstNameGenerator());
		config.setColumnGenerator("person", "achternaam", new CsvBasedGenerator("lastnames.csv"));
		config.setColumnGenerator("person", "voorletters", new InitialsGenerator());
		config.setColumnGenerator("person", "telefoon", new PhoneNumberGenerator());
		config.setColumnGenerator("person", "email", new EmailColumnGenerator());

		config.includeColumn("person", "budgethouderid");
		config.includeColumn("person", "afdelingid");
		config.includeColumn("person", "vestigingid");
		
		// Branch table configuration
		int branchEntryAmount = new ReadCsv("company_names.csv").readCsv().size();
		config.setEntryAmountByTableName("branch", branchEntryAmount);
		config.setColumnGenerator("branch", "naam" , new UniqueItemGenerator("company_names.csv"));
		config.setColumnGenerator("branch", "straat1", new StreetAddressGenerator());
		config.setColumnGenerator("branch", "plaats1", new CsvBasedGenerator("city_names.csv"));
		config.setColumnGenerator("branch", "postcode1", new ZipCodeGenerator());
		config.setColumnGenerator("branch", "telefoon", new PhoneNumberGenerator());
		config.setColumnGenerator("branch", "email", new CompanyEmailGenerator());
		config.setColumnGenerator("branch", "huisnummer1", new UniformIntegerGenerator(1, 200));
		
		
		// Department table configuration
		config.setEntryAmountByTableName("department", new ReadCsv("departments.csv").readCsv().size());
		config.setColumnGenerator("department", "naam" , new UniqueItemGenerator("departments.csv"));
		
		// Budgetholder table configuration
		config.setEntryAmountByTableName("budgetholder", new ReadCsv("departments.csv").readCsv().size());
		config.setColumnGenerator("budgetholder", "naam" , new UniqueItemGenerator("departments.csv"));

		// Incident table configuration
		config.setEntryAmountByTableName("incident", 50);
		config.setColumnGenerator("incident", "state", new UniformIntegerGenerator(1, 2));
		config.includeColumn("incident", "persoonid");
		config.setColumnGenerator("incident", "aanmeldervestigingid", new ForeignKeySelectorGenerator("branch", "unid"));
		
		// Incident Memo History table configuration
		config.setEntryAmountByTableName("incident_memo_history", 50);
		config.setColumnGenerator("incident_memo_history", "parentid",  new UniqueForeignKeySelectorGenerator("incident", "unid"));
		config.setColumnGenerator("incident_memo_history", "veldnaam", new CsvBasedGenerator("memo_fieldnames.csv"));
		config.setColumnGenerator("incident_memo_history", "memotekst", new IncidentTextGenerator());
	}
}
