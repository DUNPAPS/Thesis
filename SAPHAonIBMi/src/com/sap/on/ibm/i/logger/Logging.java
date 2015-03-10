package com.sap.on.ibm.i.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Logger class Singleton.
 *
 * @author Duncan
 */

public class Logging {

	static final String LOG_PROPERTIES_FILE = "log/log.properties";
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

	public void setLogger(String className) {
		this.logger = Logger.getLogger(className);
	}

	public Logger getLogger() {
		return this.logger;
	}

	public void logMessages(Levels enumLevels, String loggingMsg,
			Exception exception) throws Exception {

		switch (enumLevels) {
		case TRACE:
			getLogger().setLevel(Level.TRACE);
			String trace = " full stack trace" + "::";
			getLogger().warn(trace + "   " + exception + "\n");
			break;
		case DEBUG:
			getLogger().setLevel(Level.DEBUG);
			getLogger().warn(exception + "\n");
			break;
		case INFO:
			if (loggingMsg == null) {
				throw new Exception(
						"Parameter 'loggingMsg' of this Method is null");
			} else {
				getLogger().setLevel(Level.INFO);
				getLogger().info(loggingMsg + "\n");

			}
			break;
		case WARN:
			getLogger().setLevel(Level.WARN);
			getLogger().warn(exception.getLocalizedMessage() + "\n");
			break;
		case ERROR:
			getLogger().setLevel(Level.ERROR);
			getLogger().error(exception.getLocalizedMessage() + "\n");
			break;
		}
	}

}
