package se.springworks.android.utils.rest;

import java.util.Map;

import se.springworks.android.utils.cache.ICache;
import se.springworks.android.utils.cache.ICache.CacheException;
import se.springworks.android.utils.http.HttpUtils;
import se.springworks.android.utils.http.IAsyncHttpClient;
import se.springworks.android.utils.http.IAsyncHttpClient.IAsyncHttpResponseHandler;
import se.springworks.android.utils.http.ISimpleHttpClient;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class RestClient implements IRestClient {

	@Inject
	private IAsyncHttpClient asyncClient;
	
	@Inject
	private ISimpleHttpClient syncClient;

	private String baseUrl = "";
	
	private boolean cachingEnabled = false;

	@Inject(optional=true)
	@Named("restclient")
	private ICache<String> cache;
	
	@InjectLogger Logger logger;
	
	@Inject
	private Context context;

	@Override
	public void enableCaching() {
		cachingEnabled = true && cache != null;
	}

	@Override
	public void disableCaching() {
		cachingEnabled = false;
	}
	
	@Override
	public boolean isCachingEnabled() {
		return cachingEnabled && cache != null;
	}
	
	@Override
	public String get(final String url, Map<String, String> params) {
		logger.debug("get() %s", url);
		final String result;
		final String absoluteUrl = getAbsoluteUrl(url, params);
		if(isCachingEnabled() && cache.contains(absoluteUrl)) {
			result = cache.get(absoluteUrl);
		}
		else {
			result = syncClient.getAsString(absoluteUrl);
			if(result != null && isCachingEnabled()) {
				try {
					cache.cache(absoluteUrl, result);
				}
				catch (CacheException e) {
					logger.warn("get() %s unable to cache result %s", absoluteUrl, e.getMessage());
				}
			}
		}
		return result;
	}
	
	@Override
	public void get(final String url, Map<String, String> params, final OnHttpResponseHandler responseHandler) {
		final String absoluteUrl = getAbsoluteUrl(url, params);
		logger.debug("get() %s", absoluteUrl);

		if(isCachingEnabled() && cache.contains(absoluteUrl)) {
			responseHandler.onSuccess(cache.get(absoluteUrl));
		}
		else {
			asyncClient.get(absoluteUrl, new IAsyncHttpResponseHandler() {				
				@Override
				public final void onSuccess(String response) {
					if(isCachingEnabled()) {
						try {
							cache.cache(absoluteUrl, response);
						}
						catch (CacheException e) {
							logger.warn("get() %s unable to cache result %s", absoluteUrl, e.getMessage());
						}
					}
					responseHandler.onSuccess(response);
				}

				@Override
				public void onFailure(Throwable e, String response, int code) {
					responseHandler.onFailure(e, response, code);
				}
			});
		}
	}

	@Override
	public void post(String url, Map<String, String> params, final OnHttpResponseHandler responseHandler) {
		logger.debug("post() %s", url);
		asyncClient.post(getAbsoluteUrl(url), params, new IAsyncHttpResponseHandler() {
			@Override
			public final void onSuccess(String response) {
				responseHandler.onSuccess(response);
			}

			@Override
			public void onFailure(Throwable e, String response, int code) {
				responseHandler.onFailure(e, response, code);
			}				
		});
	}

	@Override
	public void post(String url, String json, final OnHttpResponseHandler responseHandler) {
		logger.debug("post() %s", url);
		asyncClient.post(getAbsoluteUrl(url), json, "application/json", new IAsyncHttpResponseHandler() {
			@Override
			public final void onSuccess(String response) {
				responseHandler.onSuccess(response);
			}

			@Override
			public void onFailure(Throwable e, String response, int code) {
				responseHandler.onFailure(e, response, code);
			}				
		});
	}

	private String getAbsoluteUrl(String relativeUrl, Map<String, String> params) {
		return HttpUtils.createUrlWithQueryString(baseUrl + relativeUrl, params);
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
		asyncClient.setPreemptiveBasicAuth(username, password);
		syncClient.setBasicAuth(username, password);
	}

	@Override
	public void cancelRequests() {
		asyncClient.cancelRequests(true);
	}

	@Override
	public void delete(String url, final OnHttpResponseHandler responseHandler) {
		logger.debug("delete() %s", url);
		asyncClient.delete(getAbsoluteUrl(url), new IAsyncHttpResponseHandler() {
			@Override
			public final void onSuccess(String response) {
				responseHandler.onSuccess(response);
			}

			@Override
			public void onFailure(Throwable e, String response, int code) {
				responseHandler.onFailure(e, response, code);
			}				
		});
	}

	@Override
	public void clearCookies() {
		asyncClient.clearCookies();
	}

	@Override
	public void clearCache() {
		if(cache != null) {
			cache.clear();
		}
	}

	@Override
	public void setHeader(String header, String value) {
		asyncClient.setHeader(header, value);
	}

	@Override
	public void removeHeader(String header) {
		asyncClient.removeHeader(header);
	}
}
