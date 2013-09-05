package se.springworks.android.utils.inject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import se.springworks.android.utils.inject.annotation.InjectView;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Wrapper for Guice {@link com.google.inject.Guice} with added custom view dependency injection
 * The modules to load for Guice needs to be defined as a string-array resource with the name
 * "guice-modules".
 * 
 * If you obfuscate your code remember to keep the module names unchanged:
 * 
 * -keep public class * extends com.google.inject.AbstractModule
 * 
 * @author bjornritzl
 * 
 */
public class GrapeGuice {

	private static WeakHashMap<Application, GrapeGuice> injectors = new WeakHashMap<Application, GrapeGuice>();
	
	private Injector injector;

	private GrapeGuice(Injector injector) {
		this.injector = injector;
	}
	
	public static GrapeGuice getInjector(Fragment fragment) {
		return getInjector(fragment.getActivity());
	}
	
	public static GrapeGuice getInjector(View view) {
		return getInjector(view.getContext());
	}
	
	public static GrapeGuice getInjector(Context context) {
		if(context == null) {
			return null;
		}
		final Application application = (Application)context.getApplicationContext();
		GrapeGuice injector = injectors.get(application);
		if (injector == null) {
			final int id = application.getResources().getIdentifier("guice_modules", "array", application.getPackageName());
			final String[] moduleNames = id > 0 ? application.getResources().getStringArray(id) : new String[] {};
			final List<AbstractModule> modules = new ArrayList<AbstractModule>();

			try {
				for (String name : moduleNames) {
					final Class<? extends AbstractModule> clazz = Class.forName(name).asSubclass(AbstractModule.class);
					try {
						modules.add(clazz.getDeclaredConstructor(Context.class).newInstance(application));
					}
					catch (NoSuchMethodException ignored) {
						modules.add(clazz.newInstance());
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			injector = new GrapeGuice(Guice.createInjector(modules));
			injectors.put(application, injector);
		}
		return injector;
	}

	/**
	 * Injects dependencies into fields and methods of an object This uses the
	 * Guice framework to do annotations, {@link Injector#injectMembers(Object)}
	 * 
	 * @param o
	 *            The object to inject dependencies into
	 */
	public GrapeGuice injectMembers(Object o) {
		injector.injectMembers(o);
		return this;
	}

	/**
	 * Injects views from the specified activity into fields annotated with
	 * @InjectView(id = R.id.name) in the specified target object
	 * 
	 * @param a
	 *            The activity to get views from
	 * @param o
	 *            The object to inject views into
	 */
	public GrapeGuice injectViews(Activity a, Object o) {
		return injectViewsFromObject(a, o);
	}
	public GrapeGuice injectViews(Dialog d, Object o) {
		return injectViewsFromObject(d, o);
	}
	public GrapeGuice injectViews(View v, Object o) {
		return injectViewsFromObject(v, o);
	}
	
	private GrapeGuice injectViewsFromObject(Object from, Object into) {
		Class<?> targetClass = into.getClass();
		Field[] fields = targetClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(InjectView.class)) {
				InjectView injectView = (InjectView) field.getAnnotation(InjectView.class);
				field.setAccessible(true);
				try {
					View v = null;
					if(from instanceof Activity) {
						v = ((Activity)from).findViewById(injectView.id());
					}
					else if(from instanceof View) {
						v = ((View)from).findViewById(injectView.id());
					}
					else if(from instanceof Dialog) {
						v = ((Dialog)from).findViewById(injectView.id());
					}
					if(v != null) {
						field.set(into, v);
					}
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
		return this;
	}

	/**
	 * Injects views into the members of an activity Refer to
	 * {@link #injectViews(Activity, Object)} for details
	 * 
	 * @param a
	 *            The activity to get views from and fields to inject to
	 */
	public GrapeGuice injectViews(Activity a) {
		return injectViews(a, a);
	}

	/**
	 * Injects views from the current activity into a fragment Refer to
	 * {@link #injectViews(Activity, Object)} for details
	 * 
	 * @param f
	 */
	public GrapeGuice injectViews(Fragment f) {
		return injectViews(f.getView(), f);
	}

	public <T> T getInstance(Class<T> t) {
		return injector.getInstance(t);
	}

}
