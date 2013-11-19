package se.springworks.android.utils.auth;



public interface IAuthentication {

	public enum AuthenticationError {
		NOVALIDACCOUNTS,
		BUSY,
		USERCANCELED,
		UNSPECIFIED,
		MISSINGAPIKEY,
		UNRECOVERABLE,
		SERVICEUNAVAILABLE
	}
	
	public interface OnTokenCallback {
		void onToken(String accountName, String token);
		void onError(AuthenticationError error);
	}
	
	boolean isValidAccountName(String accountName);
	
	/**
	 * Get a login token. If more than one account exists some kind of account
	 * picker should be shown, or a best guess should be made
	 * @param callback
	 */
	void getToken(OnTokenCallback callback);
	
	/**
	 * Get a login token for a specific account
	 * @param accountName
	 * @param callback
	 */
	void getToken(String accountName, OnTokenCallback callback);
}
