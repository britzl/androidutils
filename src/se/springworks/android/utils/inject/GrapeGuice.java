package se.springworks.android.utils.inject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.springworks.android.utils.inject.annotation.InjectView;
import android.app.Activity;
import android.support.v4.app.Fragment;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;


/**
 * Wrapper for Guice {@link com.google.inject.Guice} and GreenRobot {@link de.greenrobot.inject.Injector}
 * dependency injection
 * @author bjornritzl
 *
 */
public class GrapeGuice {

	private static List<AbstractModule> modules = new ArrayList<AbstractModule>();
	
	private static Injector injector;
	
	public static void addModule(AbstractModule module) {
		if(!modules.contains(module)) {
			modules.add(module);
			injector = Guice.createInjector(modules);
		}
	}
	
	/**
	 * Injects dependencies into fields and methods of an object
	 * This uses the Guice framework to do annotations, {@link Injector#injectMembers(Object)}
	 * @param o The object to inject dependencies into
	 */
	public static void injectMembers(Object o) {
		injector.injectMembers(o);
	}
	
	/**
	 * Injects views from the specified activity into fields annotated
	 * with @InjectView(id = R.id.name) in the specified target object
	 * 
	 * @param a The activity to get views from
	 * @param o The object to inject views into
	 */
	public static void injectViews(Activity a, Object o) {
    	Class<?> targetClass = o.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
        	if(field.isAnnotationPresent(InjectView.class)) {
        		InjectView injectView = (InjectView)field.getAnnotation(InjectView.class);
        		field.setAccessible(true);
        		try {
					field.set(o, a.findViewById(injectView.id()));
				}
				catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
	}

	/**
	 * Injects views into the members of an activity
	 * Refer to {@link #injectViews(Activity, Object)} for details
	 * 
	 * @param a The activity to get views from and fields to inject to
	 */
	public static void injectViews(Activity a) {
		injectViews(a, a);
	}

	/**
	 * Injects views from the current activity into a fragment
	 * Refer to {@link #injectViews(Activity, Object)} for details
	 * @param f
	 */
	public static void injectViews(Fragment f) {
		injectViews(f.getActivity(), f);
	}

	public static <T> T getInstance(Class<T> t) {
		return injector.getInstance(t);
	}
	
}
