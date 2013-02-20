package se.springworks.android.utils.inject;

import java.util.ArrayList;
import java.util.List;

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
	 * Injects views, extras and resources from an activity into fields of an object
	 * This uses the following GreenRobot annotations:
	 * 
	 *  - @InjectView(id = R.id.name) View v
	 *  - @InjectExtra(id = "key") Object o
	 *  - @InjectResource(id = R.drawable.name) Drawable d
	 *  - @InjectResource(id = R.strings.name) String s
	 *  - @InjectResource(id = R.drawable.name) Bitmap b
	 * 
	 * @param a The activity to get views from
	 * @param o The object to inject views into
	 */
	public static void injectViews(Activity a, Object o) {
		de.greenrobot.inject.Injector.inject(a, o);
	}

	/**
	 * Injects views, extras and resources for an activity
	 * Refer to {@link #injectViews(Activity, Object)} for details
	 * 
	 * @param a The activity to get views from and fields to inject to
	 */
	public static void injectViews(Activity a) {
		de.greenrobot.inject.Injector.injectInto(a);
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
