package com.topdesk.si2011.dbgenerator.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.topdesk.si2011.dbgenerator.communication.parser.ParseException;
import com.topdesk.si2011.dbgenerator.communication.parser.ParsedDefinition;
import com.topdesk.si2011.dbgenerator.communication.parser.Parser;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.StructureInterpreter;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.StructureBuilder;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.StructureBuilderImpl;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.TableBuilder;
import com.topdesk.si2011.dbgenerator.util.StringConversions;

public class RestStructureInterpreter implements StructureInterpreter {
	private List<String> log = new ArrayList<String>();

	@Override
	public IDbStructure createStructure() {
		StructureBuilder structureBuilder = new StructureBuilderImpl();
		buildStructure(structureBuilder,
				getReadableTables(getTablesFromServer()));
		printLog();
		return structureBuilder.build();
	}

	private void printLog() {
		for (String entry : log) {
			System.out.println(entry);
		}
	}

	private void buildStructure(StructureBuilder structureBuilder,
			Set<String> readableTables) {
		for (String tableName : readableTables) {
			System.out.println(String
					.format("Processing Table:\t%s", tableName));
			try {
				ParsedDefinition tableParser = Parser.parseDefinition(ServerUrl.instance()
						.getDefinitionUrl(tableName));
				createColumnsForTable(tableName, tableParser,
						structureBuilder.addTable(tableName), readableTables);
			} catch (ParseException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	private List<String> getTablesFromServer() {
		try {
			return Parser.parseTablesList(ServerUrl.instance().getServerUrl())
					.getChildrensAttribute("href");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private Set<String> getReadableTables(List<String> tableUrls) {
		Set<String> result = Sets.newHashSet();

		for (String tableUrl : tableUrls) {
			String tableName = StringConversions.urlToName(tableUrl);
			if (isValid(tableName)) {
				result.add(tableName);
				System.out.println(String.format("Readable   Table:\t%s",
						tableName));
			} else {
				System.err.println(String.format("Unreadable Table:\t%s",
						tableName));
			}
		}

		return result;
	}

	private boolean isValid(String tableName) {
		try {
			Parser.parseTable(ServerUrl.instance().getTableUrl(tableName));
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	private StructureBuilder createColumnsForTable(String tableName,
			ParsedDefinition tableParser, TableBuilder tableBuilder,
			Set<String> readableTables) {
		for (ColumnAttributes column : tableParser
				.getAllChildrenAndAttributes()) {
			try {
				column.addToBuilder(tableBuilder, readableTables);
			} catch (UnknownReferenceTableException e) {
				log.add("In table " + tableName + ", field " + e.getMessage());
			} catch (RuntimeException e1) {
				throw e1;
			}
		}
		return tableBuilder.buildTable();
	}
}
