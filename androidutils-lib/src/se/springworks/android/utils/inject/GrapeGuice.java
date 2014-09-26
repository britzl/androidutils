package se.springworks.android.utils.inject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import se.springworks.android.utils.inject.annotation.InjectView;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.logging.LoggerFactory;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.test.AndroidTestCase;
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
	
	private Logger logger = LoggerFactory.getLogger(GrapeGuice.class);
	
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
	
	public static GrapeGuice getInjector(AndroidTestCase testCase) {
		return getInjector(testCase.getContext());
	}
	
	public static GrapeGuice getInjector(Context context) {
		if(context == null) {
			return null;
		}
		final Application application = (Application)context.getApplicationContext();
		GrapeGuice injector = injectors.get(application);
		if (injector == null) {
			final Resources resources = application.getResources();
			final int id = resources.getIdentifier("guice_modules", "array", application.getPackageName());
			final String[] moduleNames = id > 0 ? resources.getStringArray(id) : new String[] {};
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
	 * Rebinds all bindings for this injector based on the provided module(s)
	 * @param modules
	 * @return This instance rebound with bindings defined in the provided module(s)
	 */
	public GrapeGuice rebind(AbstractModule ...modules) {
		this.injector = Guice.createInjector(modules);
		return this;
	}
	
	/**
	 * Get an injector for the specified context with the bindings defined in the provided modules
	 * If an injector already exists for the specified context it will be rebound with bindings from
	 * the specified module(s)
	 * @param context
	 * @param modules
	 * @return
	 */
	public static GrapeGuice getInjector(Context context, AbstractModule ...modules) {
		if(context == null) {
			return null;
		}
		
		GrapeGuice injector = GrapeGuice.getInjector(context);
		injector.rebind(modules);
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
				final int id = injectView.id();
				field.setAccessible(true);
				try {
					View v = null;
					if(from instanceof Activity) {
						v = ((Activity)from).findViewById(id);
					}
					else if(from instanceof View) {
						v = ((View)from).findViewById(id);
					}
					else if(from instanceof Dialog) {
						v = ((Dialog)from).findViewById(id);
					}
					if(v != null) {
						field.set(into, v);
					}
				}
				catch (IllegalArgumentException e) {
					e.printStackTrace();
					logger.error("injectViewsFromObject() %d %s", id, e);
				}
				catch (IllegalAccessException e) {
					e.printStackTrace();
					logger.error("injectViewsFromObject() %d %s", id, e);
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
