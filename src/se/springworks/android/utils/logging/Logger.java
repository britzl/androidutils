package se.springworks.android.utils.logging;

import se.springworks.android.utils.logging.ILogTarget.LogLevel;


public class Logger {
	
	private String tag;
	
	public Logger() {
		this.tag = "";
	}
	
	public Logger(String tag) {
		this.tag = tag;
	}

	@SuppressWarnings("rawtypes")
	public Logger(Class clazz) {
		this.tag = clazz.getName();
	}

	public void debug(String message, Throwable t, Object ...args) {
		if(t != null) {
			t.printStackTrace();
		}
		log(LogLevel.DEBUG, message, args);
	}

	public void debug(String message, Object ...args) {
		log(LogLevel.DEBUG, message, args);
	}

	public void warn(String message, Throwable t, Object ...args) {
		if(t != null) {
			t.printStackTrace();
		}
		log(LogLevel.WARN, message, args);
	}

	public void warn(String message, Object ...args) {
		log(LogLevel.WARN, message, args);
	}

	public void error(String message, Throwable t, Object ...args) {
		if(t != null) {
			t.printStackTrace();
		}
		log(LogLevel.ERROR, message, args);
	}

	public void error(String message, Object ...args) {
		log(LogLevel.ERROR, message, args);
	}

	public void info(String message, Throwable t, Object ...args) {
		if(t != null) {
			t.printStackTrace();
		}
		log(LogLevel.INFO, message, args);
	}

	public void info(String message, Object ...args) {
		log(LogLevel.INFO, message, args);
	}
	
	private void log(LogLevel level, String message, Object ...args) {
		if(args != null && args.length > 0) {
			try {
				message = String.format(message, args);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		for(ILogTarget target : LoggerFactory.getTargets()) {
			target.log(level, tag, message);
		}
	}

}
