package com.topdesk.si2011.dbgenerator.dbstructure;

public class DbLocation {
	
	private final String column;
	private final String table;
	
	public DbLocation(String table, String column) {
		this.table = table;
		this.column = column;
	}

	public String getColumn() {
		return column;
	}
	
	public String getTable() {
		return table;
	}
	
	@Override
	public String toString() {
		return table + "." + column;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DbLocation other = (DbLocation) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}

	public static DbLocation create(String tableName, String columnName) {
		return new DbLocation(tableName, columnName);
	}
}
