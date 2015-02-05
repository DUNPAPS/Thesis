package com.sap.on.ibm.i.tasks;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import com.sap.on.ibm.i.logger.Log;

public class ExecuteSAPControl {
	private Log logger = new Log();
	private String param0;
	private String param1;
	private String instance;
	private String function;
	private String host;
	private String version;
	private String format;
	public ExecuteSAPControl() {
		logger.setLogger(ExecuteSAPControl.class.getName());
		logger.log(Level.CONFIG,
				"---------------------------------------------------------------------------");
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

		logger.log(Level.INFO, "start sap_ctrl");

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

//		if (debug) {
//			sap_ctrl += " -debug";
//		}

		if (version != null) {
			sap_ctrl += " " + version;
		}

		try {

			logger.log(Level.INFO, "Start sap_ctrl command");
			logger.log(Level.INFO, "Command: " + "  " + sap_ctrl);

			Process process = Runtime.getRuntime().exec(sap_ctrl);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}

			// get the error stream of the process
			InputStream errorStream = process.getErrorStream();
			BufferedReader errorbufferedReader = new BufferedReader(
					new InputStreamReader(errorStream));

			String error = null;
			while ((error = errorbufferedReader.readLine()) != null) {
				logger.log(Level.WARNING, error);
			}

			logger.log(Level.INFO, "End sap_ctrl");

		} catch (IOException e) {
			logger.log(Level.SEVERE, "\n" + e.getMessage());
		}
	}
}