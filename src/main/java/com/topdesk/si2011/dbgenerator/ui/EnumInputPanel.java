package com.topdesk.si2011.dbgenerator.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EnumInputPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2815980205492105893L;

	private JLabel lblName = new JLabel();
	private JComboBox cbxValue = new JComboBox();

	public EnumInputPanel(String label) {
		super(new BorderLayout());

		add(lblName, BorderLayout.WEST);
		add(cbxValue, BorderLayout.CENTER);

		this.setLabel(label);
	}
	
	public void setOptions(List<String> options) {
		cbxValue.removeAllItems();
		
		for(String option : options) {
			cbxValue.addItem(option);
		}
	}

	public void setLabel(String label) {
		lblName.setText(label);
	}

	public String getLabel() {
		return lblName.getText();
	}

	public void setValue(String value) {
		cbxValue.setSelectedItem(value);
	}

	public String getValue() {
		return cbxValue.getSelectedItem().toString();
	}

	public void addOption(String option) {
		cbxValue.addItem(option);
	}
}
