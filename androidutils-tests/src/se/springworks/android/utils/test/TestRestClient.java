package se.springworks.android.utils.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.cache.DiskCache;
import se.springworks.android.utils.cache.ICache;
import se.springworks.android.utils.file.IFileHandler;
import se.springworks.android.utils.file.StorageFileHandler;
import se.springworks.android.utils.http.HttpUtils;
import se.springworks.android.utils.http.IAsyncHttpClient;
import se.springworks.android.utils.http.ISimpleHttpClient;
import se.springworks.android.utils.inject.GrapeGuice;
import se.springworks.android.utils.inject.guice.InjectLoggerListener;
import se.springworks.android.utils.json.IJsonParser;
import se.springworks.android.utils.json.JacksonParser;
import se.springworks.android.utils.mock.MockAsyncHttpClient;
import se.springworks.android.utils.mock.MockSimpleHttpClient;
import se.springworks.android.utils.persistence.IKeyValueStorage;
import se.springworks.android.utils.persistence.NamedSharedPreferencesStorage;
import se.springworks.android.utils.rest.IRestClient;
import se.springworks.android.utils.rest.RestClient;
import android.content.Context;
import android.test.AndroidTestCase;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

public class TestRestClient extends AndroidTestCase {
	
	private class TestRestClientModule extends AbstractModule {
		private Context context;
		private MockAsyncHttpClient asyncHttpClient;
		private MockSimpleHttpClient syncHttpClient;

		public TestRestClientModule(Context context, MockAsyncHttpClient asyncHttpClient, MockSimpleHttpClient syncHttpClient) {
			this.context = context;
			this.asyncHttpClient = asyncHttpClient;
			this.syncHttpClient = syncHttpClient;
		}
		
		@Override
		protected void configure() {
			bind(Context.class).toInstance(context);

			install(new FactoryModuleBuilder().implement(IKeyValueStorage.class, NamedSharedPreferencesStorage.class).build(IKeyValueStorage.NamedKeyValueStorageFactory.class));

            bindListener(Matchers.any(), new InjectLoggerListener());
			bind(IJsonParser.class).to(JacksonParser.class);
			bind(IFileHandler.class).to(StorageFileHandler.class);
			bind(new TypeLiteral<ICache<String>>(){}).to(new TypeLiteral<DiskCache<String>>(){});
			
            bind(new TypeLiteral<ICache<String>>(){}).annotatedWith(Names.named("restclient")).to(new TypeLiteral<DiskCache<String>>(){});
			bind(IAsyncHttpClient.class).toInstance(asyncHttpClient);
			bind(ISimpleHttpClient.class).toInstance(syncHttpClient);
			bind(IRestClient.class).to(RestClient.class);
		}
	}
	
	private class MockHttpResponseHandler implements IRestClient.OnHttpResponseHandler {

		private CountDownLatch latch;

		public String respone;
		public boolean success = false;
		
		public MockHttpResponseHandler() {
			latch = new CountDownLatch(1);
		}
		
		@Override
		public void onSuccess(String response) {
			this.respone = response;
			success = true;
		}

		@Override
		public void onFailure(Throwable t, String response, int code) {
			// TODO Auto-generated method stub	
		}
		
		public void await(int timeoutMillis) throws InterruptedException {
			latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
		}
	}
	
	@Inject
	private IRestClient restClient;
	
	private MockAsyncHttpClient asyncHttp;
	private MockSimpleHttpClient syncHttp;
	private MockHttpResponseHandler responseHandler;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		asyncHttp = new MockAsyncHttpClient();
		syncHttp = new MockSimpleHttpClient();
		responseHandler = new MockHttpResponseHandler();
		GrapeGuice.getInjector(this).rebind(new TestRestClientModule(getContext(), asyncHttp, syncHttp)).injectMembers(this);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSyncGet() {
		final String expectedResponse = "RESPONSE1";
		final String url = "http://www.acme.com";
		Map<String, String> params = new HashMap<String, String>();
		params.put("key1", "value1");
		params.put("key2", "value2");
		params.put("key3", "value3");
		
		syncHttp.setRespone(HttpUtils.createUrlWithQueryString(url, params), expectedResponse);

		final String actualResponse = restClient.get(url, params);
		assertEquals(expectedResponse, actualResponse);
	}


	@Test
	public void testAsyncGet() throws InterruptedException {
		final String expectedResponse = "RESPONSE1";
		final String url = "http://www.acme.com";
		Map<String, String> params = new HashMap<String, String>();
		params.put("key1", "value1");
		params.put("key2", "value2");
		params.put("key3", "value3");

		asyncHttp.setRespone(HttpUtils.createUrlWithQueryString(url, params), expectedResponse);

		restClient.get(url, params, responseHandler);
		responseHandler.await(500);
		assertEquals(expectedResponse, responseHandler.respone);
	}
	
}
