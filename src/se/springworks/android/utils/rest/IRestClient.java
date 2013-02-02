package se.springworks.android.utils.rest;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public interface IRestClient {
	
	void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler);
	
	void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler);
}
