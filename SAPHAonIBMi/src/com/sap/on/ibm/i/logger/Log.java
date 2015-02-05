package com.sap.on.ibm.i.logger;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	private static Logger LOGGER;
	public Log() {
	}

	public void setLogger(String string) {
		LOGGER = Logger.getLogger(string);
		Properties log4jProperties = new Properties();
		log4jProperties.setProperty("log4j.rootLogger", "INFO, myConsoleAppender");
		log4jProperties.setProperty("timeformat", "yyyy-MM-dd HH:mm:ss");
		log4jProperties.setProperty("log4j.appender.myConsoleAppender", "org.apache.log4j.ConsoleAppender");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		log4jProperties.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		log4jProperties.setProperty("log4j.appender.myConsoleAppender.layout", "org.apache.log4j.PatternLayout");
		log4jProperties.setProperty("log4j.appender.myConsoleAppender.layout.ConversionPattern", "%-5p %c %x - %m%n");
		log4jProperties.setProperty("log4j.appender.A1","org.pollerosoftware.log4j.additions.appenders.TimestampFileAppender");
	}

	public void log(Level level, String msg) {
		LOGGER.log(level, msg);
	}
}
