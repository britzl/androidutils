package se.springworks.android.utils.auth;

import java.io.IOException;

import se.springworks.android.utils.activity.BaseActivity;
import se.springworks.android.utils.activity.BaseActivity.OnActivityResultListener;
import se.springworks.android.utils.application.BaseApplication;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.logging.LoggerFactory;
import se.springworks.android.utils.resource.ParameterLoader;
import se.springworks.android.utils.threading.SimpleAsyncTask;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Authentication class for Google identities using oAuth2.
 * 
 * Specify the API key in a resource XML using the key specified in
 * {@link GoogleAuthentication#KEY_APIKEY}
 * 
 * If there's more than one account to chose from the AccountPicker will be used. You
 * can override the standard message by adding a string with the key specified in
 * {@link GoogleAuthentication#KEY_ACCOUNTPICKERMESSAGE} 
 * 
 * http://android-developers.blogspot.se/2013/01/verifying-back-end-calls-from-android.html
 * @author bjornritzl
 *
 */
public class GoogleAuthentication implements IAuthentication, OnActivityResultListener {
	
	private static final Logger logger = LoggerFactory.getLogger(GoogleAuthentication.class);

	public static final String KEY_APIKEY = "googleauth_clientid";
	public static final String KEY_ACCOUNTPICKERMESSAGE = "googleauth_accountpickermessage";
	
	private enum State {
		IDLE,
		WAITINGFORACCOUNTPICKER,
		WAITINGFORRECOVERY
	}
	
	
	private State state = State.IDLE;
	
	private OnTokenCallback callback;
	
	private Context context;
	
	private ParameterLoader paramLoader;

	
	public GoogleAuthentication(Context appContext) {
		this.context = appContext;
		paramLoader = new ParameterLoader(appContext);
	}
	
	
	
	@Override
	public void getToken(OnTokenCallback callback) {
		logger.debug("getToken()");

		final int serviceState = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if(serviceState != ConnectionResult.SUCCESS) {
			callback.onError(AuthenticationError.SERVICEUNAVAILABLE);
			return;
		}

		if(state != State.IDLE) {
			logger.debug("getToken() I'm not idle. Don't call me now.");
			callback.onError(AuthenticationError.BUSY);
			return;
		}
		
		AccountManager manager = AccountManager.get(context);
		Account[] accounts = manager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		if(accounts == null || accounts.length == 0) {
			logger.debug("getToken() No accounts");
			callback.onError(AuthenticationError.NOVALIDACCOUNTS);
		}
		else if(accounts.length == 1) {
			logger.debug("getToken() Exactly one account");
			getToken(accounts[0].name, callback);
		}
		else {
			logger.debug("getToken() More than one account");
			state = State.WAITINGFORACCOUNTPICKER;
			this.callback = callback;
			String customMessage = paramLoader.getString(KEY_ACCOUNTPICKERMESSAGE);
			Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, customMessage, null, null, null);
			BaseApplication.getInstance().getCurrentActivity().startActivityForResult(intent, 0, this);
		}
	}

	@Override
	public void getToken(final String accountName, final OnTokenCallback callback) {
		logger.debug("getToken() account name = %s", accountName);
		final String apiKey = paramLoader.getString(KEY_APIKEY);
		if(apiKey == null) {
			logger.warn("getToken() no api key");
			callback.onError(AuthenticationError.MISSINGAPIKEY);
			return;
		}
		
		SimpleAsyncTask<String, String> task = new SimpleAsyncTask<String, String>() {

			@Override
			public String performTask(String accountName) {
				try {
					final String scope = "audience:server:client_id:" + apiKey;
					logger.debug("getToken() scope = %s", scope);
					return GoogleAuthUtil.getToken(context, accountName, scope);
				}
				catch (GooglePlayServicesAvailabilityException playEx) {
					logger.debug("getToken()", playEx);
				}
				catch (UserRecoverableAuthException e) {
					state = State.WAITINGFORRECOVERY;
					BaseActivity current = BaseApplication.getInstance().getCurrentActivity();
					if(current != null) {
						current.startActivityForResult(e.getIntent(), 0, GoogleAuthentication.this);
					}
					else {
						callback.onError(AuthenticationError.UNRECOVERABLE);
					}
				}
				catch (IOException e) {
					logger.error("getToken()", e);
				}
				catch (GoogleAuthException e) {
					logger.error("getToken() %s", e.getMessage(), e);
				}
				// java.lang.IllegalArgumentException: Non existing account
				catch(IllegalArgumentException e) {
					logger.error("getToken() %s", e.getMessage(), e);				
				}
				return null;
			}

			@Override
			public void handleResult(String token) {
				logger.debug("handleResult() token = " + token);
				if(token == null) {
					callback.onError(AuthenticationError.UNRECOVERABLE);
					return;
				}
				callback.onToken(accountName, token);
			}
		};
		task.execute(accountName);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		logger.debug("handleActivityResult()");
		if(state == State.IDLE || callback == null) {
			return;
		}

		if(resultCode == Activity.RESULT_CANCELED) {
			callback.onError(AuthenticationError.USERCANCELED);
		}
		else if(resultCode != Activity.RESULT_OK) {
			callback.onError(AuthenticationError.UNSPECIFIED);
		}
		else {	
			if(state == State.WAITINGFORACCOUNTPICKER) {
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				getToken(accountName, callback);
			}
			else if(state == State.WAITINGFORRECOVERY) {
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				getToken(accountName, callback);
			}
		}
		state = State.IDLE;
	}



	@Override
	public boolean isValidAccountName(String accountName) {
		AccountManager manager = AccountManager.get(context);
		Account[] accounts = manager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		for(Account account : accounts) {
			if(accountName.equals(account.name)) {
				return true;
			}
		}
		return false;
	}
}
