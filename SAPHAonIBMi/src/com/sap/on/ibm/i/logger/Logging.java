package com.sap.on.ibm.i.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Logger class Singleton.
 *
 * @author Duncan
 */

public class Logging  {

	private static final String DATUM_FORMAT_JETZT = "dd/MM/yyyy HH:mm:ss";
	static final String LOG_PROPERTIES_FILE = "log/log.properties";
	private SimpleDateFormat datumFormat = null;
	private Logger logger;
	private static Logging instance = null;


	private Logging() {
		
		initializeLogger();

	}

	private void initializeLogger() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		PropertyConfigurator.configure(classLoader
				.getResource("log/log.properties"));

	}
	/**
	 * @return instance
	 */
	public static Logging getInstance() {
		if (instance == null) {
			instance = new Logging();
		}
		return instance;
	}

	private String actuallTime() {
		if (datumFormat == null) {
			datumFormat = new SimpleDateFormat(DATUM_FORMAT_JETZT);
		}

		Calendar cal = Calendar.getInstance(); // Calendar Singleton Objekt
		return datumFormat.format(cal.getTime());

	}
	public void setLogger(String className){
		this.logger=Logger.getLogger(className);
	}
	public Logger getLogger() {
		return this.logger;
	}

	public void logMessages(Levels enumLevels, String loggingMsg,
			Exception exception) throws Exception {

		switch (enumLevels) {
		case TRACE:
			 getLogger().setLevel(Level.TRACE);
			String trace =  getLogger().getLevel() + " " + actuallTime()
					+ "::" + " full stack trace";
			 getLogger().warn(trace + "   " + exception + "\n");
			break;
		case DEBUG:
			 getLogger().setLevel(Level.DEBUG);
			String debug =  getLogger().getLevel() + " " + actuallTime()
					+ "::" + " full stack trace";
			 getLogger().warn(debug + "   " + exception + "\n");
			break;
		case INFO:
			if (loggingMsg == null) {
				throw new Exception(
						"Parameter 'loggingMsg' of this Method is null");
			} else {
				 getLogger().setLevel(Level.INFO);
				String info =  getLogger().getLevel() + " "
						+ actuallTime();
				 getLogger().info(info + "   " + loggingMsg + "\n");

			}
			break;
		case WARN:
			 getLogger().setLevel(Level.WARN);
			String warn =  getLogger().getLevel() + " " + actuallTime();
			 getLogger().warn(
					warn + "  " + exception.getLocalizedMessage() + "\n");
			break;
		case ERROR:
			 getLogger().setLevel(Level.ERROR);
			String error =  getLogger().getLevel().toString() + " "
					+ actuallTime();
			 getLogger().error(
					error + "   " + exception.getLocalizedMessage() + "\n");
			break;
		}
	}
}
