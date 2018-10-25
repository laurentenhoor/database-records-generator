package com.topdesk.si2011.dbgenerator.communication.parser;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

public class ParsedTable extends ParsedXml {
	public ParsedTable(Document doc) {
		super(doc);
	}
	
	public List<String> getUnids() {
		List<String> unids = new ArrayList<String>();
		for (int i = 0; i < getDoc().getRootElement().getChildren().size(); i++) {
			Element child = (Element) getDoc().getRootElement().getChildren().get(i);
			unids.add(child.getAttributeValue("id"));
		}
		return unids;
	}

}
