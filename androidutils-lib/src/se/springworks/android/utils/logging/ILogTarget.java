package se.springworks.android.utils.logging;

public interface ILogTarget {

	public enum LogLevel {
		DEBUG,
		INFO,
		WARN,
		ERROR
	}
	
	public void log(LogLevel level, String tag, String message);
}
