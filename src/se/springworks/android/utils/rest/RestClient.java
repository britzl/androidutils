package se.springworks.android.utils.rest;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RestClient implements IRestClient {

	private AsyncHttpClient client = new AsyncHttpClient();

	private static String baseUrl = "";
	private static boolean cachingEnabled = true;

	private RestCache cache = new RestCache();

	public static void enableCaching() {
		cachingEnabled = true;
	}

	public static void disableCaching() {
		cachingEnabled = false;
	}

	@Override
	public void get(final String url, RequestParams params, final AsyncHttpResponseHandler responseHandler) {
		if(!cachingEnabled) {
			client.get(getAbsoluteUrl(url), params, responseHandler);
			return;
		}
		
		String cachedData = cache.get(url);
		if (cachedData != null) {
			responseHandler.onSuccess(cachedData);
		}
		else {
			client.get(getAbsoluteUrl(url), params, new AsyncHttpResponseHandler() {
				@Override
				public final void onSuccess(String response) {
					cache.cache(url, response);
					responseHandler.onSuccess(response);
				}

				@Override
				public void onFailure(Throwable e, String response) {
					responseHandler.onFailure(e, response);
				}
			});
		}
	}

	@Override
	public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private String getAbsoluteUrl(String relativeUrl) {
		return baseUrl + relativeUrl;
	}

	public static void setBaseUrl(String url) {
		baseUrl = url;
	}
}
