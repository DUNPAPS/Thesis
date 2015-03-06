package com.sap.on.ibm.i.tasks;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.SwingWorker;

import com.sap.on.ibm.i.controller.IController;
import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;

public class ExecuteSAPControl {

	private String param0;
	private String param1;
	private String instance;
	private String function;
	private String host;
	private String version;
	private String format;
	protected String SAP_CONTROL = "C:\\Users\\IBM_ADMIN\\git\\Thesis\\SAPHAonIBMi\\sapcontrol.exe";
//	protected String SAP_CONTROL = "sapcontrol.exe";
	private Logging logger;
	private IController myController;
	protected Map<String, String> myMap = new ConcurrentHashMap<String, String>();

	public ExecuteSAPControl(IController myController) {
		this.myController = myController;
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

		// if (debug) {
		// sap_ctrl += " -debug";
		// }

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
				ProgressbarTimedUpdate PGU = new ProgressbarTimedUpdate(myController);

				try {

					int maxWaitTimeSEC = 30;
					
					SAP_CONTROL = " " + getCommand();
					getLogger().logMessages(Levels.INFO,
							"Command:    " + SAP_CONTROL, null);
					getLogger().logMessages(Levels.INFO, "Executig command...",
							null);
					
					
					PGU.Start(maxWaitTimeSEC);
					
					Process p = Runtime.getRuntime().exec(SAP_CONTROL);
					BufferedReader stdInput = new BufferedReader(
							new InputStreamReader(p.getInputStream()));
					
			
					
					// error
					BufferedReader stdError = new BufferedReader(
							new InputStreamReader(p.getErrorStream()));
					
					
					
					
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
					p.waitFor();
				} catch (Exception e) {
					try {
						getLogger().logMessages(Levels.ERROR, null,
								new Exception(e));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				try {
					getLogger().logMessages(Levels.INFO, " " + " Finished ...",
							null);
					ActionEvent e = new TaskDoneEvent(this, 1234,
							"SAP CONTROL DONE.....");
					myController.sendDoneEvent(e);
					PGU.Stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, "Execute SAP Control  .....");
		t2.start();
	}

}
