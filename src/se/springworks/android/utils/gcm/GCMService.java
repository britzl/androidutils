package se.springworks.android.utils.gcm;

import se.springworks.android.utils.inject.GrapeGuice;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.threading.ICallback;
import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.google.inject.Inject;

/**
 * Base class for the service handling incoming push messages and push registrations
 * 
 * @author bjornritzl
 *
 */
public class GCMService extends GCMBaseIntentService {

	@InjectLogger
	private Logger logger;
	
	@Inject
	private IPushHandler pushHandler;
		
	@Override
	public void onCreate() {
		super.onCreate();
		GrapeGuice.getInjector(this).injectMembers(this);
	}

	// http://developer.android.com/google/gcm/gs.html
	@Override
	public void onRegistered(final Context context, String regId) {
		logger.debug("onRegistered() %s", regId);
		pushHandler.onRegistered(regId);
	}

	@Override
	protected void onError(Context context, String errorId) {
		logger.debug("onError() %s", errorId);
		pushHandler.onError(errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		logger.debug("onMessage()");
		pushHandler.onMessage(intent.getExtras());
	}

	@Override
	protected void onUnregistered(final Context context, String regId) {
		logger.debug("onUnregistered() %s", regId);
		pushHandler.onUnregistered(regId);
	}
	
	@Override
	protected String[] getSenderIds(Context context) {
		String senderId = pushHandler.getSenderId();
		return new String[] { senderId };
	}	
}
