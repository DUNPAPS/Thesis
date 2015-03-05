package com.sap.on.ibm.i.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.sap.on.ibm.i.logger.Levels;
import com.sap.on.ibm.i.logger.Logging;
import com.sap.on.ibm.i.view.HATestEditor;

public class ExecuteSAPControl extends SwingWorker<String, Integer> {

	private String param0;
	private String param1;
	private String instance;
	private String function;
	private String host;
	private String version;
	private String format;
	protected String SAP_CONTROL = "sapcontrol.exe";
	private Logging logger;
	private HATestEditor outputTestEditor;
	protected Map<String, String> myMap = new ConcurrentHashMap<String, String>();

	public ExecuteSAPControl() {
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

	public void setLogger(Logging logger) {
		this.logger = logger;
	}

	public Logging getLogger() {
		return logger;
	}

	@Override
	protected String doInBackground() throws Exception {
		String line;
		try {
			SAP_CONTROL = " " + getCommand();
			getLogger().logMessages(Levels.INFO, "Command:    " + SAP_CONTROL,
					null);
			getLogger().logMessages(Levels.INFO, getState().toString(), null);

			Process p = Runtime.getRuntime().exec(SAP_CONTROL);
			getOutputTestEditor().getStatusBarJLabel().setText(" ");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			// error
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			int progress = 0;

			while (!isCancelled() && progress < 30) {
				setProgress(++progress);
				Thread.sleep(200);

			}
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
			while (!isCancelled() && progress < 100) {
				setProgress(++progress);
				Thread.sleep(50);

			}

		} catch (Exception e) {
			try {
				getLogger().logMessages(Levels.ERROR, null, new Exception(e));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return "SAP Stopped";
	}

	@Override
	protected void process(List<Integer> chunks) {
		super.process(chunks);
	}

	@Override
	protected void done() {
		try {
			if (isCancelled()) {
				getOutputTestEditor().getStatusBarJLabel().setText(
						"Process canceled");
			} else {
				getLogger().logMessages(Levels.INFO, " " + get(), null);
				getOutputTestEditor().getStatusBarJLabel().setText(get());
				getOutputTestEditor().getPlayButton().setEnabled(true);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public HATestEditor getOutputTestEditor() {
		return outputTestEditor;
	}

	public void setOutputTestEditor(HATestEditor outputTestEditor) {
		this.outputTestEditor = outputTestEditor;
	}
}
