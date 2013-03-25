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
		void onToken(String token);
		void onError(AuthenticationError error);
	}
	
	void getToken(OnTokenCallback callback);
}
