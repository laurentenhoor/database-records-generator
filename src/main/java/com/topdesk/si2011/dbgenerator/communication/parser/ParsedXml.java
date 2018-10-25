package com.topdesk.si2011.dbgenerator.communication.parser;

import org.jdom.Document;
import org.jdom.Namespace;

public abstract class ParsedXml {
	
	protected static final Namespace XLINK = Namespace.getNamespace("http://www.w3.org/1999/xlink");
	private final Document getDoc;

	public ParsedXml(Document doc) {
		this.getDoc = doc;
	}
	
	protected boolean isReadable() {
		return getDoc != null;
	}
	
	protected Document getDoc() {
		return getDoc;
	}

}
