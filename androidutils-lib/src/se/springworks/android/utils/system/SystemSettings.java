package se.springworks.android.utils.system;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

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
		if(connectivityManager == null) {
			return false;
		}
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	// http://stackoverflow.com/questions/12466878/how-to-check-programmatically-if-data-roaming-is-enabled-disabled
	@Override
	@SuppressWarnings("deprecation")
	public boolean isDataRoamingEnabled() {
		try {
			return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.DATA_ROAMING) == 1;
		} catch (SettingNotFoundException e) {
			return false;
		}
	}
	
	@Override
	public boolean isRoaming() {
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager == null) {
			return false;
		}
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isRoaming();
	}
	
	
	@Override
	public boolean isWifiConnected() {
		WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		if(wifi == null) {
			return false;
		}
		SupplicantState state = wifi.getConnectionInfo().getSupplicantState();
		if(state == null) {
			return false;
		}
		return WifiInfo.getDetailedStateOf(state) == NetworkInfo.DetailedState.CONNECTED;
	}
	
}
