package se.springworks.android.utils.auth;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.springworks.android.utils.activity.BaseActivity.OnActivityResultListener;
import se.springworks.android.utils.application.BaseApplication;
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

public class GoogleAuthentication implements IAuthentication, OnActivityResultListener {
	
	private static final Logger logger = LoggerFactory.getLogger(GoogleAuthentication.class);

	public static final String KEY_APIKEY = "googleauth_clientid";
	
	private enum State {
		IDLE,
		WAITINGFORACCOUNTPICKER,
		WAITINGFORRECOVERY
	}
	
	
	private State state = State.IDLE;
	
	private AccountManager manager;
	
	private OnTokenCallback callback;
	
	private Context context;
	
	public GoogleAuthentication(Context appContext) {
		this.context = appContext;
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
		
		manager = AccountManager.get(context);
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
			Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null);
			BaseApplication.getInstance().getCurrentActivity().startActivityForResult(intent, 0, this);
		}
	}

	private void getToken(String accountName, final OnTokenCallback callback) {
		logger.debug("getToken() account name = " + accountName);
		ParameterLoader paramLoader = new ParameterLoader(context);
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
					logger.debug("getToken() scope = " + scope);
					return GoogleAuthUtil.getToken(context, accountName, scope);
				}
				catch (GooglePlayServicesAvailabilityException playEx) {
					logger.debug("getToken()", playEx);
				}
				catch (UserRecoverableAuthException e) {
					state = State.WAITINGFORRECOVERY;
					BaseApplication.getInstance().getCurrentActivity().startActivityForResult(e.getIntent(), 0, GoogleAuthentication.this);
				}
				catch (IOException e) {
					logger.error("getToken()", e);
				}
				catch (GoogleAuthException e) {
					logger.error("getToken() " + e.getMessage(), e);
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
				callback.onToken(token);
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
}
