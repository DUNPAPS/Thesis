package com.sap.on.ibm.i.tasks;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sap.on.ibm.i.controller.IController;
import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.view.ProgressbarTimedUpdate;

public class SAPControl {

	private String param0;
	private String param1;
	private String instance;
	private String function;
	private String host;
	private String version;
	private String format;
	protected String SAP_CONTROL = "sapcontrol.exe";
	private Logging logger;
	private IController myController;
	protected Map<String, String> myMap = new ConcurrentHashMap<String, String>();
	private int MAX_WAIT_TIME_SEC = 30;
	private Process process;
	private ProgressbarTimedUpdate progressbar;
	private BufferedReader stdInput;
	private BufferedReader stdError;
	private int exitCode;

	public SAPControl(IController myController) {
		this.myController = myController;
		progressbar = new ProgressbarTimedUpdate(this.myController);

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

	public String getCommand() {

		String sap_ctrl = SAP_CONTROL;

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

		if (version != null) {
			sap_ctrl += " " + version;
		}
		return sap_ctrl;

	}

	public void setLogger(Logging logger) {
		this.logger = logger;
	}

	public Logging getLogger() {
		return logger;
	}

	public void execute() {

		Thread t2 = new Thread(new Runnable() {

			public void run() {
				String line;

				try {

					progressbar.start(MAX_WAIT_TIME_SEC);

					System.out.println("COMMAND: " + getCommand());
					System.out.println(" ");
					System.out.println("Execute COMMAND...");
					System.out.println(" ");

					process = Runtime.getRuntime().exec(getCommand());
					stdInput = new BufferedReader(new InputStreamReader(
							process.getInputStream()));

					// error
					stdError = new BufferedReader(new InputStreamReader(
							process.getErrorStream()));

					while ((line = stdInput.readLine()) != null) {

						if (!line.equals("") || line.contains("INFO")) {
							getLogger().logMessages(Levels.INFO, line, null);
						} else if (!line.equals("") && line.contains("FAIL")) {
							getLogger().logMessages(Levels.ERROR, null,
									new Exception(line));
						} else {
							getLogger().logMessages(Levels.INFO, line, null);
						}
					}
					while ((line = stdError.readLine()) != null) {

						if (!line.equals("")) {
							getLogger().logMessages(Levels.ERROR, null,
									new Exception(line));
						}
					}
					process.waitFor();
					exitCode = process.exitValue();

					if (exitCode == 0) {
						getLogger().logMessages(
								Levels.INFO,
								" " + " EXITCODE: " + exitCode
										+ " Last webmethod call successful",
								null);
						getLogger().logMessages(Levels.INFO, " " + " Finished",
								null);
						ActionEvent e = new TaskEvent(this, 0,
								"SAP CONTROL DONE");
						myController.sendDoneEvent(e);
					}
					if (exitCode == 1) {
						throw new RuntimeException(
								" EXITCODE: "
										+ exitCode
										+ "Last webmethod call failed, invalid parameter.");
					}
					if (exitCode == 2) {
						throw new RuntimeException(
								" EXITCODE: "
										+ exitCode
										+ "StartWait, StopWait, WaitforStarted, WaitforStopped, RestartServiceWait timed out.");
					}

					if (exitCode == 3) {
						getLogger()
								.logMessages(
										Levels.INFO,
										" "
												+ " EXITCODE: "
												+ exitCode
												+ " GetProcessList succeeded, all processes running correctly",
										null);
						ActionEvent e = new TaskEvent(this, 0,
								"SAP CONTROL DONE");
						myController.sendDoneEvent(e);
					}

					if (exitCode == 4) {
						getLogger()
								.logMessages(
										Levels.INFO,
										" "
												+ " EXITCODE: "
												+ exitCode
												+ " GetProcessList succeeded, all processes stopped",
										null);
						ActionEvent e = new TaskEvent(this, 0,
								"SAP CONTROL DONE");
						myController.sendDoneEvent(e);
					}

				} catch (Exception e) {
					try {
						getLogger().logMessages(Levels.ERROR, null,
								new Exception(e));
						process.destroy();
						stdError.close();
						stdInput.close();
						System.out.println("Exit Value: " + exitCode);
						System.exit(1);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			}

		}, "Execute SAP Control");
		t2.start();
		myController.setThreadName(t2.getName());
	}

	public void stopProcess() {
		if (process != null) {
			process.destroy();
			progressbar.Stop();
		}
	}
}
