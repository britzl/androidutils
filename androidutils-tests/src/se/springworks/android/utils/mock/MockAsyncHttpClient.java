package se.springworks.android.utils.mock;

import java.util.HashMap;
import java.util.Map;

import se.springworks.android.utils.http.IAsyncHttpClient;

public class MockAsyncHttpClient implements IAsyncHttpClient {
	
	private Map<String, String> responseMap = new HashMap<String, String>();
	
	
	public void setRespone(String url, String response) {
		responseMap.put(url, response);
	}
	

	@Override
	public void get(String url, IAsyncHttpResponseHandler responseHandler) {
		String response = responseMap.get(url);
		if(response != null) {
			responseHandler.onSuccess(response);
		}
		else {
			responseHandler.onFailure(null, null, 404);
		}
	}

	@Override
	public void delete(String url, IAsyncHttpResponseHandler responseHandler) {
		String response = responseMap.get(url);
		if(response != null) {
			responseHandler.onSuccess(response);
		}
		else {
			responseHandler.onFailure(null, null, 404);
		}
	}

	@Override
	public void post(String url, Map<String, String> params,
			IAsyncHttpResponseHandler responseHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void post(String url, String data, String contentType,
			IAsyncHttpResponseHandler responseHandler) {
		String response = responseMap.get(url);
		if(response != null) {
			responseHandler.onSuccess(response);
		}
		else {
			responseHandler.onFailure(null, null, 404);
		}
	}

	@Override
	public void cancelRequests(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPreemptiveBasicAuth(String user, String pass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearCookies() {
		// TODO Auto-generated method stub
		
	}

}
