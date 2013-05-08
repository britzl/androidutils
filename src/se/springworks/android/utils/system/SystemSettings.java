package se.springworks.android.utils.system;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.google.inject.Inject;

public class SystemSettings implements ISystemSettings {

	@Inject
	private Context context;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isAirplaneModeOn() {
		return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}

	@Override
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	
}
