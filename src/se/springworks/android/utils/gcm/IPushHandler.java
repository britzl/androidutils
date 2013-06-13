package se.springworks.android.utils.gcm;

import android.os.Bundle;

public interface IPushHandler {

	/**
	 * Registers for Google Cloud Messaging
	 * Once the registration is successful a callback will be made to {@link #onRegistered(String)}
	 */
	void register();
	
	/**
	 * Unregister from Google Cloud Messaging
	 */
	void unregister();
	
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
	 * Callback when registration with GCM has completed.
	 * The registration id should be sent to your server.
	 * @param regId
	 */
	void onRegistered(String regId);
	
	void onUnregistered(String regId);
	
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
