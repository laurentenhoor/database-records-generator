package com.topdesk.si2011.dbgenerator.communication.parser;

import org.jdom.Document;
import org.jdom.Element;

import com.topdesk.si2011.dbgenerator.util.StringConversions;

public class ParsedInsertResult extends ParsedXml {

	public ParsedInsertResult(Document doc) {
		super(doc);
	}

	public String getNewUnid() {
		
//		// Logging for Debugging
//		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
//		System.out.println(outputter.outputString(getDoc()));
		
		for (int i = 0; i < getDoc().getRootElement().getChildren().size(); i++) {
			Element child = (Element) getDoc().getRootElement().getChildren().get(i);
			String name = child.getAttributeValue("name");
			if (name.equals("id")) {
				return StringConversions.integerUnidConversion(child
						.getAttributeValue("href", XLINK));
			} else if (name.equals("unid")) {
				return StringConversions.stringUnidConversion(child.getAttributeValue(
						"href", XLINK));
			}
		}
		return null;

	}

}
