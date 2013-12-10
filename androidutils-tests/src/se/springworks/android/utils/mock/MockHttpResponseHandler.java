package se.springworks.android.utils.mock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import se.springworks.android.utils.rest.IRestClient;

public class MockHttpResponseHandler implements IRestClient.OnHttpResponseHandler {

	private CountDownLatch latch;

	public String response;
	public boolean success = false;
	
	public MockHttpResponseHandler() {
		latch = new CountDownLatch(1);
	}
	
	@Override
	public void onSuccess(String response) {
		this.response = response;
		success = true;
	}

	@Override
	public void onFailure(Throwable t, String response, int code) {
		this.response = response;
		success = false;
	}
	
	public MockHttpResponseHandler await(int timeoutMillis) throws InterruptedException {
		latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
		return this;
	}
}
