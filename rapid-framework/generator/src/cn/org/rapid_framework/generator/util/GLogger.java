package cn.org.rapid_framework.generator.util;

public class GLogger {
	private static final int DEBUG = 1;
	private static final int INFO = 5;
	private static final int ERROR = 10;
	private static final int WARN = 15;

	public static int logLevel = INFO;

	public static void debug(String s) {
		if (logLevel >= DEBUG)
			System.out.println("[Generator DEBUG] " + s);
	}

	public static void info(String s) {
		if (logLevel >= INFO)
			System.out.println("[Generator INFO] " + s);
	}

	public static void warn(String s) {
		if (logLevel >= WARN)
			System.err.println("[Generator WARN] " + s);
	}

	public static void warn(String s, Throwable e) {
		if (logLevel >= WARN) {
			System.err.println("[Generator WARN] " + s);
			e.printStackTrace();
		}
	}

	public static void error(String s) {
		if (logLevel >= ERROR)
			System.err.println("[Generator ERROR] " + s);
	}

	public static void error(String s, Throwable e) {
		if (logLevel >= ERROR) {
			System.err.println("[Generator ERROR] " + s);
			e.printStackTrace();
		}
	}
}
