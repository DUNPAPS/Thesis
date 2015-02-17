/**
 * 
 */
package com.sap.on.ibm.i.editor.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.logger.Appender;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;
import com.sap.on.ibm.i.tasks.ExecuteSSHCommand;

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
		this._listener = new Listener(this);
		this.addListener();
	}

	public void showView() {
		Appender a = new Appender(this, this._outputTestEditor.getJtextArea());
		Logger root = Logger.getRootLogger();
		root.addAppender(a);

		this._outputTestEditor.setVisible(true);

	}

	public void executeSAPcontrol() {

		ExecuteSAPControl sapControl = new ExecuteSAPControl(this);
		sapControl.setFunction("GetProcessList");
		sapControl.setInstance("00");
		sapControl.setHost("as0013");
		sapControl.execute();
	}
	
	public void executeSSHCommand() {

		ExecuteSSHCommand executeSSHCommand = new ExecuteSSHCommand(this);
		executeSSHCommand.setUser("qsecofr@as0013");
		executeSSHCommand.setPassword("bigboss"); 
		executeSSHCommand.setCommand("ls -R /");
		executeSSHCommand.execute();
	}

	public void addListener() {
		_outputTestEditor.setclearLogViewButtonActionListener(_listener);
		_outputTestEditor.setcopyActionListener(_listener);
		_outputTestEditor.setstop_SAP_CheckboxActionListener(_listener);
		_outputTestEditor.setplayButtonActionListener(_listener);
		_outputTestEditor.setstopButtonActionListener(_listener);
		_outputTestEditor.setexitJMenuItemActionListener(_listener);
		_outputTestEditor.setinforJMenuItemActionListener(_listener);

	}

	public void removeListener(String msg) {
	}

	public OutputTestEditor get_outputTestEditor() {
		return _outputTestEditor;
	}

	public User get_user() {
		return _user;
	}


}