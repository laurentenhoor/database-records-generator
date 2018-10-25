package com.topdesk.si2011.dbgenerator.communication;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.topdesk.si2011.dbgenerator.dbentry.IDbTableEntry;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbTable;

public class XmlBuilder {

	private String docAsString;

	public XmlBuilder(IDbTableEntry entry, IDbTable table) {
		Document doc = new Document();
		Element root = new Element(entry.getTableName());

		for (String columnName : entry.getColumnNames()) {
			if (!columnName.equals(table.getPrimaryKeyColumnName())) {
				String value = entry.getColumnEntry(columnName).getValue();
				
				if(value != null) {
					if(!value.equals(table.getColumnByName(columnName).getDefaultValue())) {
						root.addContent(new Element(columnName).setText(value));	
					}
				} else {
					root.addContent(new Element(columnName).setText(value));
				}
			}
		}
		doc.setContent(root);

		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		docAsString = outputter.outputString(doc);

		System.out.println(docAsString);
	}

	public String getDocAsString() {
		return docAsString;
	}
}
