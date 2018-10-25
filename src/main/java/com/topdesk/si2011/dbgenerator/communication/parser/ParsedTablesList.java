package com.topdesk.si2011.dbgenerator.communication.parser;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

public class ParsedTablesList extends ParsedXml {
	public ParsedTablesList(Document doc) {
		super(doc);
	}
	
	public List<String> getChildrensAttribute(String attribute) {
		List<String> childrensAttributes = new ArrayList<String>();
		for (int i = 0; i < getDoc().getRootElement().getChildren().size(); i++) {
			Element child = (Element) getDoc().getRootElement().getChildren().get(i);
			childrensAttributes.add(child.getAttributeValue(attribute, XLINK));
		}
		return childrensAttributes;
	}
}
