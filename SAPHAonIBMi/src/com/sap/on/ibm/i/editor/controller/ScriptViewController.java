package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.editor.view.ScriptEditor;

public class ScriptViewController implements IScriptController, ActionListener,
		PropertyChangeListener, ItemListener {
	private GUIScriptController guiScriptController;
	private OutputTestEditor outputTestEditor;

	public ScriptViewController(GUIScriptController guiScriptController) {
		this.guiScriptController = guiScriptController;
		this.outputTestEditor = this.guiScriptController.getOutputTestEditor();
	}

	@Override
	public void progress(PropertyChangeEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendDoneEvent(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void showScriptView() {
		ScriptEditor scriptEditor = new ScriptEditor(this);
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

	public OutputTestEditor getOutputTestEditor() {
		return outputTestEditor;
	}

}
