package com.topdesk.si2011.dbgenerator.task;

public interface Task {
	boolean canPerform();
	void perform();
	boolean isPerformed();
	String getDescription();
}
