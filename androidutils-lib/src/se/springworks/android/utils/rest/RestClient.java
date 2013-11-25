package se.springworks.android.utils.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import se.springworks.android.utils.cache.MemCache;
import se.springworks.android.utils.http.IAsyncHttpClient;
import se.springworks.android.utils.http.IAsyncHttpClient.IAsyncHttpResponseHandler;
import se.springworks.android.utils.http.ISimpleHttpClient;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import android.content.Context;

import com.google.inject.Inject;

public class RestClient implements IRestClient {

	@Inject
	private IAsyncHttpClient asyncClient;
	
	@Inject
	private ISimpleHttpClient syncClient;

	private String baseUrl = "";
	private boolean cachingEnabled = true;

	private MemCache<String> cache = new MemCache<String>();
	
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
		final String result;
		final String absoluteUrl = getAbsoluteUrl(url, params);
		if(cachingEnabled && cache.contains(absoluteUrl)) {
			result = cache.get(absoluteUrl);
		}
		else {
			result = syncClient.getAsString(absoluteUrl);
			if(result != null && cachingEnabled) {
				cache.cache(absoluteUrl, result);
			}
		}
		return result;
	}
	
	@Override
	public void get(final String url, Map<String, String> params, final OnHttpResponseHandler responseHandler) {
		final String absoluteUrl = getAbsoluteUrl(url, params);
		logger.debug("get() %s", absoluteUrl);

		if(cachingEnabled && cache.contains(absoluteUrl)) {
			responseHandler.onSuccess(cache.get(absoluteUrl));
		}
		else {
			asyncClient.get(absoluteUrl, new IAsyncHttpResponseHandler() {				
				@Override
				public final void onSuccess(String response) {
					if(cachingEnabled) {
						cache.cache(absoluteUrl, response);
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
		String absoluteUrl = baseUrl + relativeUrl;
		if(params != null && !params.isEmpty()) {
			
            StringBuffer buffer = new StringBuffer();
            Iterator<String> keys = params.keySet().iterator();
            while(keys.hasNext()) {
            	String key = keys.next();
            	String value = params.get(key);
            	buffer.append(key);
            	buffer.append('=');
            	try {
					buffer.append(URLEncoder.encode(value, "UTF-8"));
				}
            	catch (UnsupportedEncodingException e) {
					buffer.append(value);
				}
            	if(keys.hasNext()) {
            		buffer.append('&');
            	}
            }
            if (absoluteUrl.indexOf("?") == -1) {
            	absoluteUrl += "?" + buffer.toString();
            }
            else {
            	absoluteUrl += "&" + buffer.toString();
            }
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
		asyncClient.delete(url, new IAsyncHttpResponseHandler() {
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
}
