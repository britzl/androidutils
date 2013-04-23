package se.springworks.android.utils.logging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoggerFactory {

	private static List<ILogTarget> targets = new ArrayList<ILogTarget>();
	
	public static Logger getLogger() {
		return new Logger();
	}
	
	public static Logger getLogger(String tag) {
		return new Logger(tag);
	}
	
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class clazz) {
		return new Logger(clazz);
	}
	
	public static void addTarget(ILogTarget target) {
		if(!targets.contains(target)) {
			targets.add(target);
		}
	}
	
	public static Collection<ILogTarget> getTargets() {
		return targets;
	}
}
