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

public class ExecuteSAPControl {
	private Logging logging = Logging.getInstance();
	private String param0;
	private String param1;
	private String instance;
	private String function;
	private String host;
	private String version;
	private String format;
	private SimpleDateFormat datumFormat = null;
	private Controller controller;
	private static final String DATUM_FORMAT_JETZT = "dd/MM/yyyy HH:mm:ss";

	public ExecuteSAPControl(Controller controller) {
		this.controller = controller;
		logging.setLogger(this.getClass().getName());
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
					Thread.sleep(800);
					executeSAPControl();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}, "Running command");
		t2.start();
	}

	public void executeSAPControl() {

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

		try {
			logging.getLogger().setLevel(Level.INFO);
			logging.getLogger().info(
					logging.getLogger().getLevel() + " " + "Command:    "
							+ sap_ctrl + "\n ");
			logging.getLogger().info(
					logging.getLogger().getLevel() + " "
							+ "Executig SAP Control command...");

			Process process = Runtime.getRuntime().exec(sap_ctrl);
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
				logging.getLogger().error(logging.getLogger().getLevel()+error+"/n");
			}
			logging.getLogger().setLevel(Level.INFO);
			logging.getLogger().info("\n"+logging.getLogger().getLevel()+" "+" End sap_ctrl" + "\n");
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
