package com.sap.on.ibm.i.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.sap.on.ibm.i.view.HATestEditor;
import com.sap.on.ibm.i.view.HATestScriptEditor;

public class ScriptController implements IController, ActionListener,
		PropertyChangeListener, ItemListener {
	private GUIController guiScriptController;
	private HATestEditor outputTestEditor;

	public ScriptController(GUIController guiScriptController) {
		this.guiScriptController = guiScriptController;
		this.outputTestEditor = this.guiScriptController.getOutputTestEditor();
	}

	@Override
	public void progress(PropertyChangeEvent e, String Taskname) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendDoneEvent(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void showScriptView() {
		HATestScriptEditor scriptEditor = new HATestScriptEditor(this);
		scriptEditor.setVisible();

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getOutputTestEditor().getImportScriptJMenuItem()) {
			showScriptView();
		}

	}

	public HATestEditor getOutputTestEditor() {
		return outputTestEditor;
	}





}
