package com.topdesk.si2011.dbgenerator.communication.parser;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.topdesk.si2011.dbgenerator.communication.ColumnAttributes;
import com.topdesk.si2011.dbgenerator.communication.ForeignKeyAttributes;
import com.topdesk.si2011.dbgenerator.communication.PrimaryKeyAttributes;
import com.topdesk.si2011.dbgenerator.dbstructure.DbDataConstraint;

public class ParsedDefinition extends ParsedXml {

	public ParsedDefinition(Document doc) {
		super(doc);
	}

	public List<ColumnAttributes> getAllChildrenAndAttributes() {
		List<ColumnAttributes> result = new ArrayList<ColumnAttributes>();
		if (isReadable()) {
			for (Object child : getDoc().getRootElement().getChildren()) {
				ColumnAttributes columnAttributes = createColumnAttributes((Element) child);
				if (columnAttributes != null) {
					result.add(columnAttributes);
				}
			}
		}
		return result;
	}

	private ColumnAttributes createColumnAttributes(Element columnDescription) {
		String name = columnDescription.getAttributeValue("name");
		String type = columnDescription.getAttributeValue("type");
		DbDataConstraint constraint = DbDataConstraint.valueOf(columnDescription.getAttributeValue("constraint"));
		String defaultValue = columnDescription.getAttributeValue("defaultvalue");
		String link = columnDescription.getAttributeValue("href", XLINK);
		String primary = columnDescription.getAttributeValue("primary");
		String readOnly = columnDescription.getAttributeValue("readonly");

		if (("true").equals(readOnly))
			return null;
		else if (("true").equals(primary))
			return new PrimaryKeyAttributes(name, type);
		else if (link != null)
			return new ForeignKeyAttributes(name, type, constraint,
					defaultValue, link);
		else
			return new ColumnAttributes(name, type, constraint, defaultValue);
	}

}
