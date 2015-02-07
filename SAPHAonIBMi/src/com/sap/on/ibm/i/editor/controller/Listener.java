package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.logger.Logging;

public class Listener implements ActionListener, PropertyChangeListener,
		ItemListener {
	private OutputTestEditor _outputTestEditor;
	private User _user;
	private Logging logging;

	public Listener(OutputTestEditor outputTestEditor, User user) {
		this._outputTestEditor = outputTestEditor;
		this._user = user;
		logging = Logging.getInstance();
		logging.setLogger(Listener.class.getName());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean b = this._outputTestEditor.getStop_SAP_Checkbox().isSelected();
		logging.log(Level.INFO, "Stop_SAP_Checkbox: " + b);
		logging.log(Level.INFO, "user is: " + this._user.getUser());
		logging.log(Level.INFO, "passeord is: " + this._user.getPassword());

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

}
