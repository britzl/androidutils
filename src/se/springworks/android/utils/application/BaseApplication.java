package se.springworks.android.utils.application;

import se.springworks.android.utils.activity.BaseActivity;
import se.springworks.android.utils.guice.LiveModule;
import se.springworks.android.utils.inject.GrapeGuice;
import android.app.Application;

public class BaseApplication extends Application {

	private BaseActivity current;
	
	private static BaseApplication instance;
	
	public BaseApplication() {
		super();
		instance = this;
	}
	
	public static BaseApplication getInstance() {
		return instance;
	}
	
	public void setCurrentActivity(BaseActivity a) {
		this.current = a;
	}
	
	public BaseActivity getCurrentActivity() {
		return current;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		GrapeGuice.addModule(new LiveModule(this));
	}
}
