package com.openglengine.util;

public class DebugLogger extends Logger {
	@Override
	public void info(String infoMsg) {
		System.out.println("INFO: " + infoMsg);
	}

	@Override
	public void warn(String warnMsg) {
		System.out.println("WARN: " + warnMsg);
	}

	@Override
	public void err(String errMsg) {
		System.err.println("ERROR: " + errMsg);
		System.exit(-1);
	}
}
