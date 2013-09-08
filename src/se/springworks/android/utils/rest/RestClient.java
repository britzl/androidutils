package se.springworks.android.utils.rest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import se.springworks.android.utils.cache.RestCache;
import se.springworks.android.utils.http.SimpleHttpClient;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import android.content.Context;

import com.google.inject.Inject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class RestClient implements IRestClient {

	private AsyncHttpClient asyncClient = new AsyncHttpClient();
	private SimpleHttpClient syncClient = new SimpleHttpClient();

	private String baseUrl = "";
	private boolean cachingEnabled = true;
//	private String username;
//	private String password;

	private RestCache<String> cache = new RestCache<String>();
	
	@InjectLogger Logger logger;
	
	@Inject
	private Context context;

	@Override
	public void enableCaching() {
		cachingEnabled = true;
	}

	@Override
	public void disableCaching() {
		cachingEnabled = false;
	}
	
	@Override
	public String get(final String url, Map<String, String> params) {
		logger.debug("get() %s", url);
		final String absoluteUrl = getAbsoluteUrl(url, params);
		if(!cachingEnabled) {
			return syncClient.getAsString(absoluteUrl);
		}
		
		// do we have it cached?
		final String cachedData = cache.get(absoluteUrl);
		if (cachedData != null) {
			return cachedData;
		}

		// get new value and cache it
		final String result = syncClient.getAsString(absoluteUrl);
		if(result != null) {
			cache.cache(absoluteUrl, result);
		}
		return result;
	}
	
	@Override
	public void get(final String url, Map<String, String> params, final OnHttpResponseHandler responseHandler) {
		final String absoluteUrl = getAbsoluteUrl(url, params);
		logger.debug("get() %s", absoluteUrl);
		if(!cachingEnabled) {
			asyncClient.get(absoluteUrl, new AsyncHttpResponseHandler() {
				@Override
				public final void onSuccess(String response) {
					responseHandler.onSuccess(response);
				}

				@Override
				public void onFailure(Throwable e, String response) {
					responseHandler.onFailure(e, response);
				}				
			});
			return;
		}
		
		String cachedData = cache.get(absoluteUrl);
		if (cachedData != null) {
			responseHandler.onSuccess(cachedData);
		}
		else {
//			asyncClient.setBasicAuth(username, password);
			asyncClient.get(getAbsoluteUrl(url, params), new AsyncHttpResponseHandler() {
				@Override
				public final void onSuccess(String response) {
					cache.cache(absoluteUrl, response);
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
	public void post(String url, Map<String, String> params, final OnHttpResponseHandler responseHandler) {
		logger.debug("post() %s", url);
		RequestParams rp = (params != null) ? new RequestParams(params) : null;
		asyncClient.post(getAbsoluteUrl(url), rp, new AsyncHttpResponseHandler() {
			@Override
			public final void onSuccess(String response) {
				responseHandler.onSuccess(response);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				responseHandler.onFailure(e, response);
			}				
		});
	}

	@Override
	public void post(String url, String json, final OnHttpResponseHandler responseHandler) {
		logger.debug("post() %s", url);
		try {
			StringEntity se = new StringEntity(json);
			asyncClient.post(context, getAbsoluteUrl(url), se, "application/json", new AsyncHttpResponseHandler() {
				@Override
				public final void onSuccess(String response) {
					responseHandler.onSuccess(response);
				}

				@Override
				public void onFailure(Throwable e, String response) {
					responseHandler.onFailure(e, response);
				}				
			});
		}
		catch (UnsupportedEncodingException e) {
			responseHandler.onFailure(e, null);
		}
	}

	private String getAbsoluteUrl(String relativeUrl, Map<String, String> params) {
		String absoluteUrl = baseUrl + relativeUrl;
		if(params != null) {
			absoluteUrl = AsyncHttpClient.getUrlWithQueryString(absoluteUrl, new RequestParams(params));
		}
		return absoluteUrl;
	}

	private String getAbsoluteUrl(String relativeUrl) {
		return baseUrl + relativeUrl;
	}

	@Override
	public void setBaseUrl(String url) {
		baseUrl = url;
	}
	
	@Override
	public void setBasicAuth(final String username, final String password) {
//		this.username = username;
//		this.password = password;
		asyncClient.setPreemptiveBasicAuth(username, password);
		syncClient.setBasicAuth(username, password);
	}

	@Override
	public void cancelRequests() {
		asyncClient.cancelRequests(context, true);
	}

	@Override
	public void delete(String url, final OnHttpResponseHandler responseHandler) {
		logger.debug("delete() %s", url);
		asyncClient.delete(context, url, new AsyncHttpResponseHandler() {
			@Override
			public final void onSuccess(String response) {
				responseHandler.onSuccess(response);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				responseHandler.onFailure(e, response);
			}				
		});
	}

	@Override
	public void clearCookies() {
		PersistentCookieStore cookieStore = new PersistentCookieStore(context);
		cookieStore.clear();
		asyncClient.setCookieStore(cookieStore);
	}
	
}
