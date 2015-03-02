package com.sap.on.ibm.i.editor.controller;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.sap.on.ibm.i.editor.model.User;
import com.sap.on.ibm.i.editor.view.OutputTestEditor;
import com.sap.on.ibm.i.editor.view.ScriptEditor;
import com.sap.on.ibm.i.logger.Appender;
import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.tasks.ApplyKernel;
import com.sap.on.ibm.i.tasks.ExecuteSAPControl;

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

	public void sendDoneEvent(ActionEvent e) {
		_listener.actionPerformed(e);
	}

	public Controller() {
		this._outputTestEditor = new OutputTestEditor();
		this._user = new User();
		this._user.setUser("qsecofr@as0013");
		this._user.setPassword("bigboss");
		this._listener = new Listener(this);
		this.addListener();

	}

	public void showMainView() {
		Appender appender = new Appender(this);
		Logger root = Logger.getRootLogger();
		PatternLayout layout = new PatternLayout("%m%n");
		logging.setLayout(layout);
		appender.setLayout(logging.getLayout());
		root.addAppender(appender);

		this._outputTestEditor.setVisible(true);

	}

	public void showScriptView() {
		ScriptEditor scriptEditor = new ScriptEditor(this);
		scriptEditor.setVisible();

	}

	public void stopSAP() {

		ExecuteSAPControl sapControl = new ExecuteSAPControl(this);
		sapControl.setFunction("GetProcessList");
		sapControl.setInstance("00");
		sapControl.setHost("as0013");
		sapControl.addPropertyChangeListener(_listener);
		sapControl.execute();
	}

	public void applyKernel() {

		ApplyKernel applylKernel = new ApplyKernel(this);
		applylKernel.setCommand("STEP0", "cd /FSIASP/sapmnt/DCN/exe/uc");
		// applylKernel.setCommand("STEP0",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; rm -R as400_pase_64.backup");
		// applylKernel.setCommand("STEP1",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; cp -R as400_pase_64 as400_pase_64.backup");
		applylKernel.addPropertyChangeListener(_listener);
		applylKernel.execute();

		// LastNightSAPKernel lastNightSAPKernel = new LastNightSAPKernel(this);
		// lastNightSAPKernel.setCommand("STEP0",
		// "cd /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64; cp /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXEDB_DB4.SAR .");
		// lastNightSAPKernel.setCommand("STEP1",
		// "cd /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64; cp /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXE.SAR  .");
		// lastNightSAPKernel.exe();
		// getKernelBuildl.setCommand("STEP2",
		// "cd /FSIASP/sapmnt/DCN/exe/uc; ");
		// getKernelBuildl.setCommand("STEP3", "cd /FSIASP/sapmnt/DCN/exe/uc");
		// getKernelBuildl.exe();

		// LastNightSAPKernel lastNightSAPKernel = new LastNightSAPKernel(this);
		// lastNightSAPKernel.setCommand("STEP0",
		// "cd /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64; cp /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXEDB_DB4.SAR .");
		// lastNightSAPKernel.setCommand("STEP1",
		// "cd /FSIASP/sapmnt/DCN/exe/uc/as400_pase_64; cp /bas/742_COR/gen/dbgU/as400_pase_64/_out/SAPEXE.SAR  .");
		// lastNightSAPKernel.exe();
	}

	public void addListener() {
		_outputTestEditor.setclearLogViewButtonActionListener(_listener);
		_outputTestEditor.setcopyActionListener(_listener);
		 _outputTestEditor.setstop_SAP_CheckboxActionListener(_listener);
		_outputTestEditor.setplayButtonActionListener(_listener);
		_outputTestEditor.setstopButtonActionListener(_listener);
		_outputTestEditor.setexitJMenuItemActionListener(_listener);
		_outputTestEditor.setinforJMenuItemActionListener(_listener);
		_outputTestEditor.setImportScriptJMenuItemActionListener(_listener);
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
			logging.getLogger().warn(
					warn + "  " + exception.getLocalizedMessage() + "\n");
			break;
		case ERROR:
			logging.getLogger().setLevel(Level.ERROR);
			String error = logging.getLogger().getLevel().toString() + " "
					+ actuallTime();
			logging.getLogger().error(
					error + "   " + exception.getLocalizedMessage() + "\n");
			break;
		}
	}

	private String actuallTime() {
		if (datumFormat == null) {
			datumFormat = new SimpleDateFormat(DATUM_FORMAT_JETZT);
		}

		Calendar cal = Calendar.getInstance(); // Calendar Singleton Objekt
		return datumFormat.format(cal.getTime());

	}

	public OutputTestEditor get_outputTestEditor() {
		return _outputTestEditor;
	}

	public User get_user() {
		return _user;
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

	public void setTime(long totaltime) {
		// TODO Auto-generated method stub
		
	}

}