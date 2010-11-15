package cn.org.rapid_framework.web;

public enum MessageLevel {
	INFO(30),WARN(40),ERROR(50);
	// TODO 1. add SUCCESS level = 35 ? 2. Message default level is ERROR
	
	private final int level;
	
	MessageLevel(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
}
