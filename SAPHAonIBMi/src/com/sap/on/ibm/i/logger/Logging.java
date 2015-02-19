package com.sap.on.ibm.i.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Logger class Singleton.
 *
 * @author Duncan
 */

public class Logging extends AppenderSkeleton {

	private static final String DATUM_FORMAT_JETZT = "dd/MM/yyyy HH:mm:ss";
	private String LogFile = "log/LogFile.txt";
	static final String LOG_PROPERTIES_FILE = "log/log.properties";
	private SimpleDateFormat datumFormat = null;
	private static Logging instance = null;

	private Logger logger;

	private Logging() {
		initializeLogger();

	}

	private void initializeLogger() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		PropertyConfigurator.configure(classLoader
				.getResource("log/log.properties"));

	}

	public void setLogger(String name) {
		logger = Logger.getLogger("");
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
	public Logger getLogger() {
		return logger;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void append(LoggingEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
