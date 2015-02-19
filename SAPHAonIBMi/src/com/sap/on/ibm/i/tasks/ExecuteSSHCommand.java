package com.sap.on.ibm.i.tasks;

import org.apache.log4j.Level;

import com.sap.on.ibm.i.editor.controller.Controller;
import com.sap.on.ibm.i.logger.Logging;

public class ExecuteSSHCommand {
	private Logging logging = Logging.getInstance();
	private String user;
	private String password;
	private String command;
	private Controller controller;

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
					controller.runCommand(getCommand());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}, "Running command");
		t2.start();
	}

	public String getCommand() {

		String plink_exe = "plink.exe";

		if (password != null) {
			plink_exe += " -pw " + password;
		}

		if (user != null) {
			plink_exe += " " + user;
		}

		if (command != null) {
			plink_exe += " " + command;
		}
		return plink_exe;

	}

}
