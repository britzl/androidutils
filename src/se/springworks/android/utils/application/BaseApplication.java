package se.springworks.android.utils.application;

import se.springworks.android.utils.activity.BaseActivity;
import se.springworks.android.utils.logging.AndroidLogTarget;
import se.springworks.android.utils.logging.LoggerFactory;
import android.app.Application;

public class BaseApplication extends Application {

	private BaseActivity current;
	
	private Class<?> mostRecentActivityClass;
	
	private static BaseApplication instance;
	
	public BaseApplication() {
		super();
		instance = this;
		AndroidLogTarget target = new AndroidLogTarget();
		target.setConvertDebugToInfo(true);
		LoggerFactory.addTarget(target);
	}
	
	public boolean hasCreatedAnyActivities() {
		return mostRecentActivityClass != null;
	}
	
	public Class<?> getMostRecentActivityClass() {
		return mostRecentActivityClass;
	}

	public static BaseApplication getInstance() {
		return instance;
	}
	
	public BaseActivity getCurrentActivity() {
		return current;
	}
	
	public void onActivityCreated(BaseActivity a) {
		this.current = a;
		mostRecentActivityClass = a.getClass();
	}
	
	public void onActivityDestroyed(BaseActivity a) {
		if(this.current == a) {
			this.current = null;
		}
	}
	
	public void onActivityPaused(BaseActivity a) {
		if(this.current == a) {
			this.current = null;
		}
	}
	
	public void onActivityResumed(BaseActivity a) {
		this.current = a;
		mostRecentActivityClass = a.getClass();
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		GrapeGuice.addModule(new LiveModule(this));
	}
}
