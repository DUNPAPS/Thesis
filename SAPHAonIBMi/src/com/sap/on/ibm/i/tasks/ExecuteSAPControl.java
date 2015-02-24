package com.sap.on.ibm.i.tasks;

import java.awt.event.ActionEvent;

import com.sap.on.ibm.i.editor.controller.Controller;

public class ExecuteSAPControl {

	private String param0;
	private String param1;
	private String instance;
	private String function;
	private String host;
	private String version;
	private String format;
	private Controller controller;

	public ExecuteSAPControl(Controller controller) {
		this.controller = controller;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public void setParam0(String param) {
		this.param0 = param;
	}

	public void setParam1(String param) {
		this.param1 = param;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setVersionInfo(String version) {
		this.version = version;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setDebug(Boolean debug) {
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
					Thread.sleep(1000);
					controller.runCommand(getCommand());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				ActionEvent e = new TaskDoneEvent(this,1234,"ExecuteSAPControlDone");
				controller.sendDoneEvent(e);				
			}
		}, "Running SAPControl...");
		t2.start();
	}

	public String getCommand() {

		String sap_ctrl = "sapcontrol.exe";

		if (instance != null) {
			sap_ctrl += " -nr " + instance;
		}
		if (function != null) {
			sap_ctrl += " -host " + host;
		}
		if (function != null) {
			sap_ctrl += " -function " + function;
		}
		if (param0 != null) {
			sap_ctrl += " " + param0;
		}
		if (param1 != null) {
			sap_ctrl += " " + param1;
		}

		if (format != null) {
			sap_ctrl += " -format " + format;
		}

		// if (debug) {
		// sap_ctrl += " -debug";
		// }

		if (version != null) {
			sap_ctrl += " " + version;
		}
		return sap_ctrl;

	}

}
