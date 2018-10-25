package com.topdesk.si2011.dbgenerator.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.topdesk.si2011.dbgenerator.dbstructure.DbColumnTypeName;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbColumn;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;

public class DBStructureTreeModel implements TreeModel {
	private IDbStructure structure;

	public DBStructureTreeModel() {
		this.structure = null;
	}

	public DBStructureTreeModel(IDbStructure structure) {
		this.structure = structure;
	}

	private String columnRepresentation(String tableName, String columnName) {
		IDbTable table = structure.getTableByName(tableName);
		IDbColumn column = table.getColumnByName(columnName);

		String rep = column.getName();
		rep += "( " + column.getType().getType();

		if (column.getType().getType() == DbColumnTypeName.TEXT) {
			rep += "(" + column.getType().getParameter() + ")";
		}

		rep += " )";

		rep += column.isPrimaryKey() ? " ( PK ) " : "";
		rep += column.isForeignKey() ? (" ( FK TO TABLE: " + column
				.getReferencedColumn().getTable().getName())
				+ " ) " : "";

		return rep;
	}

	public void setStructure(IDbStructure structure) {
		this.structure = structure;
	}

	@Override
	public Object getRoot() {
		if (structure == null)
			return null;

		return "Database";
	}

	@Override
	public Object getChild(Object parent, int index) {
		String element = (String) parent;

		List<String> tableNames = structure.getTableNames();
		Collections.sort(tableNames, String.CASE_INSENSITIVE_ORDER);

		if (element.equals("Database")) {
			int counter = 0;

			for (String tableName : tableNames) {
				if (counter == index)
					return tableName;

				counter++;
			}

			return null;
		} else {
			for (String tableName : tableNames) {

				if (element.equals(tableName)) {
					int counter = 0;

					List<String> columnNames = new ArrayList<String>();
					for (IDbColumn column : structure.getTableByName(tableName)
							.getColumns()) {
						columnNames.add(column.getName());
					}

					Collections
							.sort(columnNames, String.CASE_INSENSITIVE_ORDER);

					for (String columnName : columnNames) {
						if (counter == index)
							return columnRepresentation(tableName, columnName);

						counter++;
					}
				}
			}
		}

		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		String element = (String) parent;

		List<String> tableNames = structure.getTableNames();

		if (element.equals("Database")) {
			return structure.getTables().size();
		} else {
			for (String tableName : tableNames) {
				if (element.equals(tableName))
					return structure.getTableByName(tableName).getColumns()
							.size();
			}
		}

		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		String element = (String) node;

		if (element.equals("Database")) {
			return structure.getTables().isEmpty();
		} else {
			for (IDbTable table : structure.getTables()) {
				if (element.equals(table.getName()))
					return table.getColumns().isEmpty();
			}
		}

		return true;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// Not implemented
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		String element = (String) parent;
		String elementChild = (String) child;

		if (element.equals("Database")) {
			int index = 0;

			List<String> tableNames = structure.getTableNames();

			for (String tableName : tableNames) {
				if (elementChild.equals(tableName))
					return index;

				index++;
			}
		} else {
			IDbTable table = structure.getTableByName(element);
			int index = 0;

			for (IDbColumn column : table.getColumns()) {
				if (elementChild.equals(column.getName()))
					return index;

				index++;
			}
		}

		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// Not implemented
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// Not implemented
	}

}
