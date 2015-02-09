/**
 * 
 */
package com.sap.on.ibm.i.editor.controller;

import org.apache.log4j.Logger;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.logger.Appender;
import com.sap.on.ibm.i.logger.Logging;

/**
 * @author Duncan
 *
 */
public class Controller {
	private OutputTestEditor _outputTestEditor;
	private User _user;
	private Listener _listener;
	private Logging logger = Logging.getInstance();
	public Controller() {
		this._outputTestEditor = new OutputTestEditor();
		this._user = new User();
		this._listener = new Listener(_outputTestEditor, this._user);
		this.addListener(this._listener);
	}

	public void showView() {
		Appender a = new Appender(this._outputTestEditor.getJtextArea());
		Logger root = Logger.getRootLogger();
		root.addAppender(a);

		this._outputTestEditor.setVisible(true);

	}

	public void addListener(Listener listener) {
		_outputTestEditor.setclearLogViewButtonActionListener(listener);
		_outputTestEditor.setcopyActionListener(listener);
		_outputTestEditor.setstop_SAP_CheckboxActionListener(listener);
		_outputTestEditor.setplayButtonActionListener(listener);
		_outputTestEditor.setstopButtonActionListener(listener);
		_outputTestEditor.setexitJMenuItemActionListener(listener);
		_outputTestEditor.setinforJMenuItemActionListener(listener);

	}

	public void removeListener() {
		// TODO
	}
}