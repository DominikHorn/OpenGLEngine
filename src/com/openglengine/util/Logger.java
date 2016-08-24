package com.openglengine.util;

/**
 * Logger class, use this for printing err/warn and info logs
 * 
 * @author Dominik
 *
 */
public class Logger {
	/**
	 * Different log levels supported by this logger
	 * 
	 * @author Dominik
	 *
	 */
	public enum LogLevel {
		LOG_RELEASE, LOG_STANDARD, LOG_DEBUG;
	};

	private LogLevel logLevel;

	public Logger() {
		this.logLevel = LogLevel.LOG_STANDARD;
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * Info will only be printed in debug mode
	 * 
	 * @param msg
	 */
	public void info(String msg) {
		if (this.logLevel == LogLevel.LOG_DEBUG)
			System.out.println("INFO: " + msg);
	}

	/**
	 * Warnings will only be printed in debug and standard mode
	 * 
	 * @param msg
	 */
	public void warn(String msg) {
		if (this.logLevel != LogLevel.LOG_RELEASE)
			System.out.println("WARN: " + msg);
	}

	/**
	 * Use this method for logging errors. If loglevel = debug this will terminate the program
	 * 
	 * @param msg
	 */
	public void err(String msg) {
		// Always print error
		System.err.println("ERROR: " + msg);

		// Exit in debug mode
		if (this.logLevel == LogLevel.LOG_DEBUG)
			System.exit(-1);
	}

}
