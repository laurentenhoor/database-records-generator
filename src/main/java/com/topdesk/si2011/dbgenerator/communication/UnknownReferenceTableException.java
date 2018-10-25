package com.topdesk.si2011.dbgenerator.communication;

public class UnknownReferenceTableException extends RuntimeException {
	private static final long serialVersionUID = 2868129603027126660L;
	
	private final String columnName;
	private final String referenceTable;

	public UnknownReferenceTableException(String columnName, String referenceTable) {
		this.columnName = columnName;
		this.referenceTable = referenceTable;	
	}
	
	@Override
	public String getMessage() {
		return columnName + " refers to unreadable table " + referenceTable;
	}
}
