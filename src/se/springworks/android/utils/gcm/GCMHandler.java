package se.springworks.android.utils.gcm;

import java.io.IOException;

import se.springworks.android.utils.application.ApplicationUtils;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.persistence.IKeyValueStorage;
import se.springworks.android.utils.resource.ParameterLoader;
import se.springworks.android.utils.threading.AsyncVoidTask;
import se.springworks.android.utils.threading.ICallback;
import se.springworks.android.utils.threading.SimpleAsyncTask;
import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.inject.Inject;

/**
 * http://developer.android.com/google/gcm/gs.html
 * @author bjornritzl
 *
 */
public abstract class GCMHandler implements IPushHandler {

	public static final String KEY_SENDERID = "gcm_senderid";
	
	public static final String PROPERTY_REG_ID = "gcm_registration_id";
	private static final String PROPERTY_APP_VERSION = "gcm_appVersion";
	private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";
	
	/**
	 * Default lifespan (7 days) of a reservation until it is considered expired.
	 */
	public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;


	@InjectLogger
	private Logger logger;

	@Inject
	private Context context;

	@Inject
	private ParameterLoader parameterLoader;
	
	@Inject
	private IKeyValueStorage storage;
	
	@Inject
	private GoogleCloudMessaging gcm;

	@Override
	public void unregister(final ICallback callback) {
		logger.debug("unregister()");
		final String regId = getRegistrationId();
		if(regId == null) {
			logger.debug("register() GCM not registered");
			if (callback != null) {
				callback.onDone();
			}
			return;			
		}
	
		AsyncVoidTask task = new AsyncVoidTask() {	
			private Exception exception = null;
			
			@Override
			protected void performTask() {
				try {
					gcm.unregister();
				}
				catch (IOException e) {
					exception = e;
					e.printStackTrace();
				}
			}
			
			@Override
			protected void onPostExecute() {
				if(exception != null) {
					if(callback != null) {
						callback.onError(exception);
					}
					return;
				}
				
				removeRegistrationId();				
				unregisterOnServer(regId, callback);
			}
		};
		task.execute();
	}

	@Override
	public void register(final ICallback callback) {
		logger.debug("register()");
		String regId = getRegistrationId();
		if(regId != null) {
			logger.debug("register() GCM already registered using id = %s", regId);
			if (callback != null) {
				callback.onDone();
			}
			return;
		}
		
		SimpleAsyncTask<String, String> task = new SimpleAsyncTask<String, String>() {
			private Exception exception;
			
			@Override
			public String performTask(String senderId) {
				logger.debug("register() registering gcm using sender id = %s", senderId);
				String regId = null;
				try {
					regId = gcm.register(senderId);
				}
				catch(Exception e) {
					logger.error("register() unable to register for gcm, %s", e);
					exception = e;
				}
				return regId;
			}

			@Override
			public void handleResult(String regId) {
				logger.debug("register() async gcm registration done, reg id = %s", regId);
				if(regId == null || regId.length() == 0) {
					callback.onError((exception != null) ? exception : new RuntimeException("Unable to register for gcm"));
					return;
				}
				setRegistrationId(regId);		
				registerOnServer(regId, callback);
			}
		};
		task.execute(getSenderId());
	}

	/**
	 * Checks if the registration has expired.
	 * 
	 * <p>
	 * To avoid the scenario where the device sends the registration to the
	 * server but the server loses it, the app developer may choose to
	 * re-register after REGISTRATION_EXPIRY_TIME_MS.
	 * 
	 * @return true if the registration has expired.
	 */
	private boolean isRegistrationExpired() {
		// checks if the information is not stale
		long expirationTime = storage.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
		return System.currentTimeMillis() > expirationTime;
	}

	@Override
	public void onError(String message) {
		logger.error("onError() %s", message);
	}

	@Override
	public String getSenderId() {
		return parameterLoader.getString(KEY_SENDERID);
	}

	@Override
	public String getRegistrationId() {
		if(!storage.contains(PROPERTY_REG_ID)) {
			logger.debug("Registration not found.");
			return null;
		}
		String registrationId = storage.getString(PROPERTY_REG_ID);
		// check if app was updated; if so, it must clear registration id to
		// avoid a race condition if GCM sends a message
		int registeredVersion = storage.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = ApplicationUtils.getVersionCode(context);
		if (registeredVersion != currentVersion || isRegistrationExpired()) {
			logger.debug("App version changed or registration expired.");
			return null;
		}
		return registrationId;
//		return GCMRegistrar.getRegistrationId(context);
	}
	
	
	/**
	 * Stores the registration id, app versionCode, and expiration time in the
	 * application's {@code SharedPreferences}.
	 *
	 * @param regId registration id
	 */
	private void setRegistrationId(String regId) {
	    final long expirationTime = System.currentTimeMillis() + REGISTRATION_EXPIRY_TIME_MS;
	    final int appVersion = ApplicationUtils.getVersionCode(context);
	    logger.debug("Saving regId on app version %d with expiry time %d", appVersion, expirationTime);
	    storage.put(PROPERTY_REG_ID, regId);
	    storage.put(PROPERTY_APP_VERSION, appVersion);
	    storage.put(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
	}
	
	private void removeRegistrationId() {
		storage.remove(PROPERTY_REG_ID);
		storage.remove(PROPERTY_APP_VERSION);
		storage.remove(PROPERTY_ON_SERVER_EXPIRATION_TIME);
	}
}
