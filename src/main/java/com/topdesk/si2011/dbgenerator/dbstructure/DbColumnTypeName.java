package com.topdesk.si2011.dbgenerator.dbstructure;


public enum DbColumnTypeName {
	TEXT(true),
	MEMO(false),
	INTEGER(false),
	DOUBLE(false),
	BIGDECIMAL(false),
	BOOLEAN(false),
	DATE(false),
	LONG(false),
	BINARY(false),
	;
	
	private final boolean parameter;
	
	private DbColumnTypeName(boolean parameter) {
		this.parameter = parameter;
	}
	
	public boolean hasParameter() {
		return parameter;
	}
}
