package se.springworks.android.utils.system;

public interface ISystemSettings {

	/**
	 * Check if device is connected to a network
	 * @return
	 */
	boolean isNetworkAvailable();
	
	/**
	 * Check if airplane mode is enabled
	 * @return
	 */
	boolean isAirplaneModeOn();
	
	/**
	 * Check if data roaming is enabled
	 * @return
	 */
	boolean isDataRoamingEnabled();
	
	/**
	 * Check if the device is currently roaming
	 * @return
	 */
	boolean isRoaming();
	
	/**
	 * Check if device is connected to wifi
	 * @return
	 */
	boolean isWifiConnected();
}
