package se.springworks.android.utils.gcm;

import se.springworks.android.utils.threading.ICallback;
import android.os.Bundle;

public interface IPushHandler {

	/**
	 * Registers for Google Cloud Messaging
	 * @param callback Called when completely registered with GCM, own server etc
	 */
	void register(ICallback callback);
	
	/**
	 * Unregister from Google Cloud Messaging
	 * @param callback Called when completely unregistered from GCM, own server etc
	 */
	void unregister(ICallback callback);
	
	/**
	 * Get the sender id to use when registering for GCM
	 * @return
	 */
	String getSenderId();
	
	/**
	 * Get the current registration id
	 * @return Registration id or null if not registered
	 */
	String getRegistrationId();
	
	/**
	 * Called when registered with GCM
	 * @param regId
	 */
	void onRegistered(String regId);

	/**
	 * Called when unregistered from GCM
	 * @param regId
	 */
	void onUnregistered(String regId);
	
	/**
	 * Sends GCM registration to your own server
	 * @param regId
	 * @param callback Callback when registration has completed 
	 */
	void registerOnServer(String regId, ICallback callback);
	
	/**
	 * Sends GCM unregistration to your own server
	 * @param regId
	 * @param callback Callback to use when unregistration has completed 
	 */
	void unregisterOnServer(String regId, ICallback callback);
	
	/**
	 * Handle an incoming GCM
	 * @param extras
	 */
	void onMessage(Bundle extras);
	
	/**
	 * Called when an error occurs
	 * @param errorId
	 */
	void onError(String errorId);
}
