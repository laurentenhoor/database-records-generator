package com.topdesk.si2011.dbgenerator.core;

public class MissingGeneratorException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5909942659656068461L;
	
	private final String message;

	public MissingGeneratorException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
