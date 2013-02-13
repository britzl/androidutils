package se.springworks.android.utils.guice;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class GrapeGuice {

	private static List<AbstractModule> modules = new ArrayList<AbstractModule>();
	
	private static Injector injector;
	
	public static void addModule(AbstractModule module) {
		if(!modules.contains(module)) {
			modules.add(module);
			injector = Guice.createInjector(modules);
		}
	}
	
	public static void injectMembers(Object o) {
		injector.injectMembers(o);
	}
	
	public static <T> T getInstance(Class<T> t) {
		return injector.getInstance(t);
	}
	
	
}
