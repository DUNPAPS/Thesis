package com.sap.on.ibm.i.tasks;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class TaskDoneEvent extends ActionEvent {

	public TaskDoneEvent(Object source, int id, String command) {
		super(source, id, command);
	}

}
