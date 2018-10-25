package com.topdesk.si2011.dbgenerator.communication.parser;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.topdesk.si2011.dbgenerator.communication.ColumnNameAndValue;
import com.topdesk.si2011.dbgenerator.util.StringConversions;

public class ParsedEntry extends ParsedXml {

	public ParsedEntry(Document doc) {
		super(doc);
	}

	public List<ColumnNameAndValue> getAllChildrenAndValues() {
		List<ColumnNameAndValue> result = new ArrayList<ColumnNameAndValue>();
		if (isReadable()) {
			for (Object child : getDoc().getRootElement().getChildren()) {
				result.add(createColumnNameAndValue((Element) child));
			}
		}
		return result;
	}
	
	private ColumnNameAndValue createColumnNameAndValue(Element child) {
		String name = child.getAttributeValue("name");
		String value;
		if (child.getAttribute("href", XLINK) == null) {
			value = child.getText();
		} else {
			value = StringConversions.urlToName(child.getAttributeValue("href",
					XLINK));
		}
		return new ColumnNameAndValue(name, value);
	}

}
