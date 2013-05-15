package se.springworks.android.utils.gcm;

import se.springworks.android.utils.inject.GrapeGuice;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.google.inject.Inject;

/**
 * Base class for the service handling incoming push messages and push registrations
 * 
 * @author bjornritzl
 *
 */
public abstract class GCMService extends GCMBaseIntentService {

	@InjectLogger
	private Logger logger;
	
	@Inject
	private IPushHandler pushHandler;
		
	@Override
	public void onCreate() {
		super.onCreate();
		GrapeGuice.getInjector(this).injectMembers(this);
		create();
	}

	// http://developer.android.com/google/gcm/gs.html
	@Override
	public void onRegistered(Context context, String regId) {
		logger.debug("onRegistered() %s", regId);
		registerPushId(regId);
		GCMRegistrar.setRegisteredOnServer(context, true);
	}

	@Override
	protected void onError(Context context, String errorId) {
		logger.debug("onError() %s", errorId);
		handleError(errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		logger.debug("onMessage()");
		handleMessage(intent.getExtras());
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		logger.debug("onUnregistered() %s", regId);
		unregisterPushId(regId);
		GCMRegistrar.setRegisteredOnServer(context, false);
	}
	
	@Override
	protected String[] getSenderIds(Context context) {
		String senderId = pushHandler.getSenderId();
		return new String[] { senderId };
	}
	
	abstract protected void create();
	
	abstract protected void registerPushId(String regId);
	
	abstract protected void unregisterPushId(String regId);
	
	abstract protected void handleMessage(Bundle extras);
	
	abstract protected void handleError(String errorId);

}
