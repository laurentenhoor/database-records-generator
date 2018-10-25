package com.topdesk.si2011.dbgenerator.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextInputPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9205232824181176643L;
	
	private JLabel lblName = new JLabel();
	private JTextField tfdValue = new JTextField();

	public TextInputPanel(String label) {
		super(new BorderLayout());
		
		add(lblName, BorderLayout.WEST);
		add(tfdValue, BorderLayout.CENTER);
		
		this.setLabel(label);
	}

	public void setLabel(String label) {
		lblName.setText(label);
	}
	
	public String getLabel() {
		return lblName.getText();
	}

	public void setValue(String value) {
		tfdValue.setText(value);
	}

	public String getValue() {
		return tfdValue.getText();
	}
	
	public void setEditable(boolean editable) {
		tfdValue.setEditable(editable);
	}
}
