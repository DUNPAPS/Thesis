package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;

public class CommandLineScriptController implements ActionListener,
		PropertyChangeListener, ItemListener, IScriptController {
	private OutputTestEditor _outputTestEditor;
	private User _user;
	private Logging logging;
	private SimpleDateFormat datumFormat = null;
	private static final String DATUM_FORMAT_JETZT = "dd/MM/yyyy HH:mm:ss";
	private Levels levels;
	private boolean stopSAPCheckBox;
	private boolean applyKernelCheckBox;
	private boolean allChecked;

	public CommandLineScriptController() {
		this._outputTestEditor = new OutputTestEditor();
		this._user = new User();
		this._user.setUser("qsecofr@as0013");
		this._user.setPassword("bigboss");
		this.addListener();
		logging = Logging.getInstance();
//		logging.setLogger(GUIScriptController.class.getName());

	}

	@Override
	public void progress(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			_outputTestEditor.getjProgressBar().setStringPainted(true);
			_outputTestEditor.getjProgressBar().setValue(progress);
			_outputTestEditor.getStatusBarJLabel().setText(
					"In " + evt.getPropertyName().toString() + " ....");
			_outputTestEditor.getPlayButton().setEnabled(false);
		} else {
			_outputTestEditor.getStatusBarJLabel().setText(
					"Task(s) " + evt.getNewValue().toString());
			_outputTestEditor.getPlayButton().setEnabled(true);

		}

	}

	public void addListener() {
		_outputTestEditor.setclearLogViewButtonActionListener(this);
		_outputTestEditor.setcopyActionListener(this);
		_outputTestEditor.setstop_SAP_CheckboxActionListener(this);
		_outputTestEditor.setplayButtonActionListener(this);
		_outputTestEditor.setstopButtonActionListener(this);
		_outputTestEditor.setexitJMenuItemActionListener(this);
		_outputTestEditor.setinforJMenuItemActionListener(this);
		_outputTestEditor.setImportScriptJMenuItemActionListener(this);
	}
	@Override
	public void sendDoneEvent(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		progress(evt);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
