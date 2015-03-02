package com.sap.on.ibm.i.tasks;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.SwingWorker;

import com.sap.on.ibm.i.editor.controller.Controller;
import com.sap.on.ibm.i.logger.Levels;

public class ExecuteSAPControl extends SwingWorker<String, Integer> {

	private String param0;
	private String param1;
	private String instance;
	private String function;
	private String host;
	private String version;
	private String format;
	protected String SAP_CONTROL = "sapcontrol.exe";
	protected Controller controller;
	protected Map<String, String> myMap = new ConcurrentHashMap<String, String>();

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

	@Override
	protected String doInBackground() throws Exception {
		String line;
		try {
			SAP_CONTROL = " " + getCommand();
			controller.logMessages(Levels.INFO, "Command:    " + SAP_CONTROL,
					null);
			controller.logMessages(Levels.INFO, "Executig command...", null);

			Process p = Runtime.getRuntime().exec(SAP_CONTROL);
			controller.get_outputTestEditor().getStatusBarJLabel().setText(" ");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			// error
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			int progress = 0;

			while (!isCancelled() && progress < 30) {
				setProgress(++progress);
				this.controller.get_outputTestEditor().getjProgressBar()
						.setString("Running SAP Control....." + progress + "%");
				Thread.sleep(200);

			}
			while ((line = stdInput.readLine()) != null) {

				if (!line.equals("") || line.contains("INFO")) {
					controller.logMessages(Levels.INFO, line, null);
				} else if (!line.equals("") && line.contains("FAIL")) {
					controller.logMessages(Levels.ERROR, null, new Exception(
							line));
				} else {
					controller.logMessages(Levels.INFO, line, null);
				}
			}
			while ((line = stdError.readLine()) != null) {

				if (!line.equals("")) {
					controller.logMessages(Levels.ERROR, null, new Exception(
							line));
				}
			}

			p.waitFor();
			while (!isCancelled() && progress < 100) {
				setProgress(++progress);
				this.controller.get_outputTestEditor().getjProgressBar()
						.setString("Running SAP Control....." + progress + "%");
				Thread.sleep(50);

			}
		} catch (Exception e) {
			try {
				controller.logMessages(Levels.ERROR, null, new Exception(e));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		try {
			ActionEvent e = new TaskDoneEvent(this, 0, "SAP CONTROL DONE.....");
			controller.logMessages(Levels.INFO, " " + " Finished ...", null);
			controller.sendDoneEvent(e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "SAP Stopped";
	}

	@Override
	protected void process(List<Integer> chunks) {
		super.process(chunks);
	}

	@Override
	protected void done() {
		if (isCancelled()) {
			controller.get_outputTestEditor().getStatusBarJLabel()
					.setText("Process canceled");
		}
	}
}
