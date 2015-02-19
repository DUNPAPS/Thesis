/**
 * 
 */
package com.sap.on.ibm.i.editor.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.logger.Appender;
import com.sap.on.ibm.i.logger.Levels;
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
	private Logging logging = Logging.getInstance();
	private SimpleDateFormat datumFormat = null;
	private static final String DATUM_FORMAT_JETZT = "dd/MM/yyyy HH:mm:ss";
	private Levels levels;

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

	public void logMessages(Levels enumLevels, String loggingMsg,
			Exception exception) throws Exception {

		switch (enumLevels) {
		case TRACE:
			logging.getLogger().setLevel(Level.TRACE);
			String trace = logging.getLogger().getLevel() + " " + actuallTime()
					+ "::" + " full stack trace";
			logging.getLogger().warn(trace + "   " + exception + "\n");
			break;
		case DEBUG:
			logging.getLogger().setLevel(Level.DEBUG);
			String debug = logging.getLogger().getLevel() + " " + actuallTime()
					+ "::" + " full stack trace";
			logging.getLogger().warn(debug + "   " + exception + "\n");
			break;
		case INFO:
			if (loggingMsg == null) {
				throw new Exception(
						"Parameter 'loggingMsg' of this Method is null");
			} else {
				logging.getLogger().setLevel(Level.INFO);
				String info = logging.getLogger().getLevel() + " "
						+ actuallTime();
				logging.getLogger().info(info + "   " + loggingMsg + "\n");

			}
			break;
		case WARN:
			logging.getLogger().setLevel(Level.WARN);
			String warn = logging.getLogger().getLevel() + " " + actuallTime();
			logging.getLogger().warn(warn + "  " + exception.getLocalizedMessage() + "\n");
			break;
		case ERROR:
			logging.getLogger().setLevel(Level.ERROR);
			String error = logging.getLogger().getLevel().toString()+ " " + actuallTime();
			logging.getLogger().error(error + "   " +  exception.getLocalizedMessage() + "\n");
			break;
		}

	}

	public void runCommand(String command) {

		try {

			logMessages(Levels.INFO, "Command:    " + command, null);
			logMessages(Levels.INFO, "Executig command...", null);
			
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (!line.equals("")&& line.contains("INFO")) {
					logMessages(Levels.INFO, line, null);
				}
				else {
					if (!line.equals("")&& line.contains("FAIL")){
						logMessages(Levels.ERROR, null, new Exception(line));
					}
				}
			}

			// get the error stream of the process
			InputStream errorStream = process.getErrorStream();
			BufferedReader errorbufferedReader = new BufferedReader(
					new InputStreamReader(errorStream));

			String warning = null;
			while ((warning = errorbufferedReader.readLine()) != null) {
				if (!warning.equals("")) {
					logMessages(Levels.ERROR, null, new Exception(warning));
				}
			}
			logMessages(Levels.INFO, " " + " Completed...", null);
			get_outputTestEditor().getjProgressBar().setIndeterminate(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String actuallTime() {
		if (datumFormat == null) {
			datumFormat = new SimpleDateFormat(DATUM_FORMAT_JETZT);
		}

		Calendar cal = Calendar.getInstance(); // Calendar Singleton Objekt
		return datumFormat.format(cal.getTime());

	}

	public Logging getLogging() {
		return logging;
	}

	public Levels getLevels() {
		return levels;
	}

	public void setLevels(Levels levels) {
		this.levels = levels;
	}

}