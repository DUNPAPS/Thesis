package com.sap.on.ibm.i.tasks;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class TaskEvent extends ActionEvent {

	public TaskEvent(Object source, int id, String command) {
		super(source, id, command);
	}

}
