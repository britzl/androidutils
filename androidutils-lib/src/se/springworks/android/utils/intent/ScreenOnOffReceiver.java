package se.springworks.android.utils.intent;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * BroadcastReceiver used to detect screen on and off events and start a service
 * when this happens 
 * @author bjornritzl
 *
 */
public class ScreenOnOffReceiver extends BroadcastReceiver {
	
	public static final String SCREEN_OFF = "SCREEN_OFF";

	private boolean screenOff;
	
	private Class<? extends Service> serviceClass;

	public ScreenOnOffReceiver(Class<? extends Service> serviceClass) {
		super();
		this.serviceClass = serviceClass;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			screenOff = true;
		}
		else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			screenOff = false;
		}
		Intent i = new Intent(context, serviceClass);
		i.putExtra(SCREEN_OFF, screenOff);
		context.startService(i);
	}
	
	public static void registerReceiver(Service service) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		service.registerReceiver(new ScreenOnOffReceiver(service.getClass()), filter);
	}
}