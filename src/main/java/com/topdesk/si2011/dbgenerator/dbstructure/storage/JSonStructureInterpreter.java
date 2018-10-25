package com.topdesk.si2011.dbgenerator.dbstructure.storage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnType;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;
import com.topdesk.si2011.dbgenerator.dbstructure.StructureInterpreter;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.BigDecimalColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.BinaryColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.BooleanColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.DateColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.DefaultColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.DoubleColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.ForeignKeyColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.IntegerColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.LongColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.MemoColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.PrimaryKeyColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.StructureBuilder;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.StructureBuilderImpl;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.TableBuilder;
import com.topdesk.si2011.dbgenerator.dbstructure.builder.TextColumn;

public class JSonStructureInterpreter implements StructureInterpreter {
	public static final String REST_STRUCTURE_JSON_DIRECTORY = "structures/rest_structure/";
	public static final String REST_STRUCTURE_JSON_FILE = "structures/rest_structure/structure.json";
	private final String filename;

	public JSonStructureInterpreter(String filename) {
		this.filename = filename;
	}
	
	@Override
	public IDbStructure createStructure() {
		Structure structureData = readStructure();

		StructureBuilder structureBuilder = new StructureBuilderImpl();

		for (Table table : structureData.getTables()) {
			buildTable(structureBuilder.addTable(table.getName()), table);
		}

		return structureBuilder.build();
	}

	public void backupStructure(IDbStructure structure) {
		Structure structureData = new Structure();
		for (IDbTable table : structure.getTables()) {
			structureData.addTable(constructTableData(table));
		}

		writeStructure(structureData);
	}

	private Table constructTableData(IDbTable table) {
		Table tableData = new Table(table.getName(), table
				.getPrimaryKeyColumn().getName());

		for (IDbColumn column : table.getColumns()) {
			tableData.addColumn(constructColumnData(column));
		}

		return tableData;
	}

	private Column constructColumnData(IDbColumn column) {
		if (column.isForeignKey()) {
			return new Column(column.getName(), column.getType().toString(),
					column.getDefaultValue(), column.getDataConstraint(),
					column.getReferencedTable().getName() + "."
							+ column.getReferencedColumn().getName());
		}

		return new Column(column.getName(), column.getType().toString(),
				column.getDefaultValue(), column.getDataConstraint(), null);
	}

	private void writeStructure(Structure structureData) {
		Writer writer = null;

		try {
			writer = new OutputStreamWriter(new FileOutputStream(filename));

			new GsonBuilder().setPrettyPrinting().create()
					.toJson(structureData, Structure.class, writer);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	private Structure readStructure() {
		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(filename));

			return new Gson().fromJson(bufferedReader, Structure.class);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private StructureBuilder buildTable(TableBuilder tableBuilder, Table tableData) {
		for(Column columnData : tableData.getColumns()) {
			if(columnData.getName().equals(tableData.getPrimaryKeyColumn())) {
				tableBuilder.addPrimaryKeyColumn(new PrimaryKeyColumn(columnData.getName(), DbColumnType.getByName(columnData.getType()), columnData.getConstraint()));
			} else {
				String referenceTableColumn = columnData.getReferenceTableColumn();
				if(referenceTableColumn != null) {
					tableBuilder.addForeignKeyColumn(new ForeignKeyColumn(columnData.getName(), columnData.getDefaultValue(), columnData.getConstraint()), referenceTableColumn.split("\\.")[0]);
				} else {
					tableBuilder.addColumn(createColumnByData(columnData));
				}
			}
		}
		
		return tableBuilder.buildTable();
	}

	private DefaultColumn createColumnByData(Column columnData) {
		DbColumnType typeByName = DbColumnType.getByName(columnData.getType());
		switch (typeByName.getType()) {
		case TEXT:
			return new TextColumn(columnData.getName(),
					typeByName.getParameter(), columnData.getDefaultValue(),
					columnData.getConstraint());
		case MEMO:
			return new MemoColumn(columnData.getName(),
					columnData.getDefaultValue(), columnData.getConstraint());
		case BIGDECIMAL:
			return new BigDecimalColumn(columnData.getName(),
					columnData.getDefaultValue(), columnData.getConstraint());
		case BINARY:
			return new BinaryColumn(columnData.getName(),
					columnData.getDefaultValue(), columnData.getConstraint());
		case BOOLEAN:
			return new BooleanColumn(columnData.getName(),
					columnData.getDefaultValue(), columnData.getConstraint());
		case DATE:
			return new DateColumn(columnData.getName(),
					columnData.getDefaultValue(), columnData.getConstraint());
		case DOUBLE:
			return new DoubleColumn(columnData.getName(),
					columnData.getDefaultValue(), columnData.getConstraint());
		case INTEGER:
			return new IntegerColumn(columnData.getName(),
					columnData.getDefaultValue(), columnData.getConstraint());
		case LONG:
			return new LongColumn(columnData.getName(),
					columnData.getDefaultValue(), columnData.getConstraint());
		default:
			throw new UnsupportedOperationException("Unsupported type "
					+ columnData.getType());
		}
	}
}
