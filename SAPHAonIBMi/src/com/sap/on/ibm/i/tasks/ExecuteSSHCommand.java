package com.sap.on.ibm.i.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Level;

import com.sap.on.ibm.i.editor.controller.Controller;
import com.sap.on.ibm.i.logger.Logging;

public class ExecuteSSHCommand {
	private Logging logging = Logging.getInstance();
	private String user;
	private String password;
	private String command;
	private SimpleDateFormat datumFormat = null;
	private Controller controller;
	private static final String DATUM_FORMAT_JETZT = "dd/MM/yyyy HH:mm:ss";

	public ExecuteSSHCommand(Controller controller) {
		this.controller = controller;
		logging.setLogger(this.getClass().getName());
		logging.getLogger().setLevel(Level.INFO);
	}

	public void setUser(String user) {
		this.user = user;// qsecofr

	}

	public void setPassword(String password) {
		this.password = password; // bigboss
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void execute() {

		// start another thread to connect to the server to avoid blocking the
		// ui
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				try {
					controller.get_outputTestEditor().getjProgressBar()
							.setIndeterminate(true);
					controller.get_outputTestEditor().getjProgressBar()
							.setBorderPainted(true);
					controller.get_outputTestEditor().getjProgressBar()
							.repaint();
					Thread.sleep(800);
					executeSSHCommand();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}, "Running command");
		t2.start();
	}

	public void executeSSHCommand() {

		String plink_exe = "plink.exe";

		if (password != null) {
			plink_exe += " -pw " + password;
		}
		
		if (user != null) {
			plink_exe += " " + user;
		}

		if (command != null) {
			plink_exe += " "+ command;
		}

		try {
			logging.getLogger().setLevel(Level.INFO);
			logging.getLogger().info(
					logging.getLogger().getLevel() + " " + "Command:    "
							+ plink_exe + "\n ");
			logging.getLogger().info(
					logging.getLogger().getLevel() + " "
							+ "Executig  plink command..." + "\n ");

			//Important:  + "qsecofr@as0013 ls /usr/sap/DCN/// SYS/exe/run"
			Process process = Runtime.getRuntime().exec(plink_exe);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("FAIL")) {
					Exception exception = new Exception(line);
					printException(exception);
				} else {
					logging.getLogger().info(line);
					controller.get_outputTestEditor().getjProgressBar()
							.setIndeterminate(true);
				}
			}
			// get the error stream of the process
			InputStream errorStream = process.getErrorStream();
			BufferedReader errorbufferedReader = new BufferedReader(
					new InputStreamReader(errorStream));

			String error = null;
			while ((error = errorbufferedReader.readLine()) != null) {
				logging.getLogger().setLevel(Level.ERROR);
				logging.getLogger().error(
						logging.getLogger().getLevel() + error + "/n");
			}
			logging.getLogger().setLevel(Level.INFO);
			logging.getLogger().info(
					"\n" + logging.getLogger().getLevel() + " "
							+ " End plink_exe" + "\n");
			controller.get_outputTestEditor().getjProgressBar()
					.setIndeterminate(false);

		} catch (IOException e) {
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

	public void printException(Exception fehler) {

		String zeit = actuallTime();
		zeit = zeit + ":: ";
		String exce = zeit + " ExceptionTrace";
		logging.getLogger().warn(exce + "   " + fehler.getMessage() + "\n");

	}

}
