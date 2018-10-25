package com.topdesk.si2011.dbgenerator.dbstructure.builder;

class UnResolvedTableReferenceException extends RuntimeException {
	private static final long serialVersionUID = -5613326169721915613L;
	
	private final String tableName;
	
	public UnResolvedTableReferenceException(String tableName) {
		this.tableName = tableName;
	}
	
	@Override
	public String getMessage() {
		return "Could not find table " + tableName;
	}
}
