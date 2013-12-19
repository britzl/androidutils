package se.springworks.android.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

import com.google.inject.Inject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class LoopjAsyncHttpClient implements IAsyncHttpClient {

	private Context context;
	
	private AsyncHttpClient loopj = new AsyncHttpClient();
	
	@Inject
	public LoopjAsyncHttpClient(Context context) {
		this.context = context;
	}
	
	@Override
	public void get(String url, final IAsyncHttpResponseHandler responseHandler) {		
		loopj.get(url, new AsyncHttpResponseHandler() {
			@Override
			public final void onSuccess(String response) {
				responseHandler.onSuccess(response);
			}

			@Override
			public void onFailure(int code, Throwable e, String response) {
				responseHandler.onFailure(e, response, code);
			}
		});
	}

	@Override
	public void post(String url, Map<String, String> params, final IAsyncHttpResponseHandler responseHandler) {
		RequestParams rp = (params != null) ? new RequestParams(params) : null;
		loopj.post(url, rp, new AsyncHttpResponseHandler() {
			@Override
			public final void onSuccess(String response) {
				responseHandler.onSuccess(response);
			}

			@Override
			public void onFailure(int code, Throwable e, String response) {
				responseHandler.onFailure(e, response, code);
			}				
		});
	}

	@Override
	public void post(String url, String data, String contentType, final IAsyncHttpResponseHandler responseHandler) {
		try {
			StringEntity se = new StringEntity(data);
			loopj.post(context, url, se, contentType, new AsyncHttpResponseHandler() {
				@Override
				public final void onSuccess(String response) {
					responseHandler.onSuccess(response);
				}

				@Override
				public void onFailure(int code, Throwable e, String response) {
					responseHandler.onFailure(e, response, code);
				}				
			});
		}
		catch (UnsupportedEncodingException e) {
			responseHandler.onFailure(e, null, -1);
		}
	}

	@Override
	public void setPreemptiveBasicAuth(String user, String pass) {
		
    	loopj.setBasicAuth(user, pass);
    	
		HttpContext httpContext = loopj.getHttpContext();
    	httpContext.setAttribute("preemptive-auth", new BasicScheme());
    	
    	DefaultHttpClient httpClient = (DefaultHttpClient)loopj.getHttpClient();
    	httpClient.addRequestInterceptor(new HttpRequestInterceptor() {			
			@Override
			public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
				AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

				// If no auth scheme available yet, try to initialize it
				// preemptively
				if (authState.getAuthScheme() == null) {
					AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
					CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
					HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
					if (authScheme != null) {
						AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
						Credentials creds = credsProvider.getCredentials(authScope);
						if (creds == null) {
							throw new HttpException("No credentials for preemptive authentication");
						}
						authState.setAuthScheme(authScheme);
						authState.setCredentials(creds);
					}
				}
			}
		}, 0);
	}

	@Override
	public void cancelRequests(boolean mayInterruptIfRunning) {
		loopj.cancelRequests(context, mayInterruptIfRunning);
	}

	@Override
	public void delete(String url, final IAsyncHttpResponseHandler responseHandler) {
		loopj.delete(context, url, new AsyncHttpResponseHandler() {
			@Override
			public final void onSuccess(String response) {
				responseHandler.onSuccess(response);
			}

			@Override
			public void onFailure(int code, Throwable e, String response) {
				responseHandler.onFailure(e, response, code);
			}				
		});
	}

	@Override
	public void clearCookies() {
		PersistentCookieStore cookieStore = new PersistentCookieStore(context);
		cookieStore.clear();
		loopj.setCookieStore(cookieStore);
	}

	@Override
	public void setHeader(String header, String value) {
		loopj.addHeader(header, value);
	}
	
	@Override
	public void removeHeader(String header) {
		loopj.removeHeader(header);
	}


}
