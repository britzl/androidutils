package se.springworks.android.utils.test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

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
import se.springworks.android.utils.mock.MockAsyncHttpResponseHandler;
import se.springworks.android.utils.mock.MockHttpResponseHandler;
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
	
	
	@Inject
	private IRestClient restClient;
	
	private MockAsyncHttpClient asyncHttp;
	private MockSimpleHttpClient syncHttp;
	private MockHttpResponseHandler mockHttpResponseHandler;
	private MockAsyncHttpResponseHandler mockAsyncHttpResponseHandler;

	
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
		mockHttpResponseHandler = new MockHttpResponseHandler();
		mockAsyncHttpResponseHandler = new MockAsyncHttpResponseHandler();
		GrapeGuice.getInjector(this).rebind(new TestRestClientModule(getContext(), asyncHttp, syncHttp)).injectMembers(this);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testMock() throws InterruptedException {
		syncHttp.setResponse("url1", "response1");
		syncHttp.setResponse("url2", "response2");
		assertNull(syncHttp.get("someOtherUrl"));
		assertEquals("response1", syncHttp.getAsString("url1"));
		assertEquals("response2", syncHttp.getAsString("url2"));
		syncHttp.clear();
		assertNull(syncHttp.getAsString("url1"));
		assertNull(syncHttp.getAsString("url2"));
		
		
		asyncHttp.setResponse("url1", "response1");
		asyncHttp.setResponse("url2", "response2");
		asyncHttp.get("someOtherUrl", mockAsyncHttpResponseHandler);
		mockAsyncHttpResponseHandler.await(500);
		assertNull(mockAsyncHttpResponseHandler.response);
		assertFalse(mockAsyncHttpResponseHandler.success);
		
		asyncHttp.get("url1", mockAsyncHttpResponseHandler);
		mockAsyncHttpResponseHandler.await(500);
		assertEquals("response1", mockAsyncHttpResponseHandler.response);
		
		asyncHttp.get("url2", mockAsyncHttpResponseHandler);
		mockAsyncHttpResponseHandler.await(500);
		assertEquals("response2", mockAsyncHttpResponseHandler.response);

		asyncHttp.clear();
		asyncHttp.get("url1", mockAsyncHttpResponseHandler);
		mockAsyncHttpResponseHandler.await(500);
		assertNull(mockAsyncHttpResponseHandler.response);
		assertFalse(mockAsyncHttpResponseHandler.success);
		
		asyncHttp.get("url2", mockAsyncHttpResponseHandler);
		mockAsyncHttpResponseHandler.await(500);
		assertNull(mockAsyncHttpResponseHandler.response);
		assertFalse(mockAsyncHttpResponseHandler.success);
		
		assertTrue(asyncHttp.headers.isEmpty());
		asyncHttp.setHeader("SOMEHEADER1", "SOMEVALUE1");
		asyncHttp.setHeader("SOMEHEADER2", "SOMEVALUE2");
		assertTrue(asyncHttp.headers.containsKey("SOMEHEADER1"));
		assertTrue(asyncHttp.headers.containsKey("SOMEHEADER2"));
		asyncHttp.removeHeader("SOMEHEADER1");
		assertFalse(asyncHttp.headers.containsKey("SOMEHEADER1"));
		assertTrue(asyncHttp.headers.containsKey("SOMEHEADER2"));
	}

	
	@Test
	public void testSyncGet() {
		final String expectedResponse = "RESPONSE1";
		final String url = "http://www.acme.com";
		Map<String, String> params = new HashMap<String, String>();
		params.put("key1", "value1");
		params.put("key2", "value2");
		params.put("key3", "value3");
		
		syncHttp.setResponse(HttpUtils.createUrlWithQueryString(url, params), expectedResponse);

		restClient.disableCaching();
		assertEquals(expectedResponse, restClient.get(url, params));
		assertNull(restClient.get(url + "/not/found", params));		
	}

	
	@Test
	public void testCaching() throws InterruptedException {
		final String url = "http://www.acme.com";

		
		String expectedResponse = "RESPONSE1";
		asyncHttp.setResponse(HttpUtils.createUrlWithQueryString(url, null), expectedResponse);

		restClient.enableCaching();
		restClient.get(url, null, mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);
		assertEquals(expectedResponse, restClient.get(url, null));
	
		// make sure the http layer won't answer and check that we still get a response (eg cached)
		asyncHttp.clear();
		restClient.get(url, null, mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);
		assertEquals(expectedResponse, restClient.get(url, null));
		
		// disable caching, make sure the server doesn't answer and check that we fail
		asyncHttp.clear();
		restClient.disableCaching();
		restClient.get(url, null, mockHttpResponseHandler);
		assertEquals(null, mockHttpResponseHandler.await(500).response);
		assertFalse(mockHttpResponseHandler.success);
		assertEquals(null, restClient.get(url, null));

		// enable caching again and make sure we get a response again
		restClient.enableCaching();
		restClient.get(url, null, mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);
		assertEquals(expectedResponse, restClient.get(url, null));

		// clear the cache, change the response from the server and make sure we get the new response
		expectedResponse = "RESPONSE2";
		restClient.clearCache();
		asyncHttp.setResponse(HttpUtils.createUrlWithQueryString(url, null), expectedResponse);
		restClient.get(url, null, mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);
		assertEquals(expectedResponse, restClient.get(url, null));

		// make sure the http layer won't answer and check that we still get the new response (eg cached)
		asyncHttp.clear();
		restClient.get(url, null, mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);
		assertEquals(expectedResponse, restClient.get(url, null));		
	}

	@Test
	public void testAsyncGet() throws InterruptedException {
		final String expectedResponse = "RESPONSE1";
		final String url = "http://www.acme.com";
		Map<String, String> params = new HashMap<String, String>();
		params.put("key1", "value1");
		params.put("key2", "value2");
		params.put("key3", "value3");

		asyncHttp.setResponse(HttpUtils.createUrlWithQueryString(url, params), expectedResponse);

		restClient.disableCaching();
		restClient.get(url, params, mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);

		restClient.get(url + "/not/found", params, mockHttpResponseHandler);
		assertNull(mockHttpResponseHandler.await(500).response);
		assertFalse(mockHttpResponseHandler.success);
	}
	

	@Test
	public void testAsyncPostParams() throws InterruptedException {
		final String expectedResponse = "RESPONSE1";
		final String url = "http://www.acme.com";
		Map<String, String> params = new HashMap<String, String>();
		params.put("key1", "value1");
		params.put("key2", "value2");
		params.put("key3", "value3");
		
		restClient.disableCaching();
		asyncHttp.setResponse(url, expectedResponse);
		restClient.post(url, params, mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);

		restClient.post(url + "/not/found", params, mockHttpResponseHandler);
		assertNull(mockHttpResponseHandler.await(500).response);
		assertFalse(mockHttpResponseHandler.success);
	}

	@Test
	public void testAsyncPostJson() throws InterruptedException {
		final String expectedResponse = "RESPONSE1";
		final String url = "http://www.acme.com";
		final String json = "{ \"key\":1234 }";
		
		asyncHttp.setResponse(url, expectedResponse);
		restClient.post(url, json, mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);
		
		restClient.post(url + "/not/found", json, mockHttpResponseHandler);
		assertNull(mockHttpResponseHandler.await(500).response);
		assertFalse(mockHttpResponseHandler.success);
	}
	
	
	

	@Test
	public void testAsyncDelete() throws InterruptedException {
		final String expectedResponse = "RESPONSE1";
		final String url = "http://www.acme.com";
		Map<String, String> params = new HashMap<String, String>();
		params.put("key1", "value1");
		params.put("key2", "value2");
		params.put("key3", "value3");
		
		asyncHttp.setResponse(url, expectedResponse);
		restClient.delete(url, mockHttpResponseHandler);
		assertTrue(mockHttpResponseHandler.await(500).success);

		restClient.delete(url + "/not/found", mockHttpResponseHandler);
		assertFalse(mockHttpResponseHandler.await(500).success);
	}


	@Test
	public void testSetBaseUrl() throws InterruptedException {
		final String expectedResponse = "RESPONSE1";
		final String url = "http://www.acme.com";
		Map<String, String> params = new HashMap<String, String>();
		params.put("key1", "value1");
		params.put("key2", "value2");
		params.put("key3", "value3");

		restClient.setBaseUrl(url);

		asyncHttp.setResponse(HttpUtils.createUrlWithQueryString(url + "/some/path", params), expectedResponse);
		
		restClient.get("/some/path", params, mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);
		restClient.get("/not/found", params, mockHttpResponseHandler);
		assertFalse(mockHttpResponseHandler.await(500).success);
		assertEquals(null, mockHttpResponseHandler.response);

		
		asyncHttp.setResponse(url + "/some/path", expectedResponse);
		restClient.post("/some/path", params, mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);
		restClient.post("/not/found", params, mockHttpResponseHandler);
		assertFalse(mockHttpResponseHandler.await(500).success);
		assertEquals(null, mockHttpResponseHandler.response);

		
		restClient.post("/some/path", "{ \"key\":1234 }", mockHttpResponseHandler);
		assertEquals(expectedResponse, mockHttpResponseHandler.await(500).response);
		restClient.post("/not/found", "{ \"key\":1234 }", mockHttpResponseHandler);
		assertFalse(mockHttpResponseHandler.await(500).success);
		assertEquals(null, mockHttpResponseHandler.response);

		
		restClient.delete("/some/path", mockHttpResponseHandler);
		assertTrue(mockHttpResponseHandler.await(500).success);		
		restClient.delete("/not/found", mockHttpResponseHandler);
		assertFalse(mockHttpResponseHandler.await(500).success);
	}
	
	
	@Test
	public void testClearCookies() {
		assertFalse(asyncHttp.cookiesCleared);
		restClient.clearCookies();
		assertTrue(asyncHttp.cookiesCleared);
	}
	
	
	@Test
	public void testSetHeader() {
		restClient.setHeader("SOMEHEADER1", "SOMEVALUE1");
		restClient.setHeader("SOMEHEADER2", "SOMEVALUE2");
		assertTrue(asyncHttp.headers.containsKey("SOMEHEADER1"));
		assertTrue(asyncHttp.headers.containsKey("SOMEHEADER2"));
		restClient.removeHeader("SOMEHEADER1");
		assertFalse(asyncHttp.headers.containsKey("SOMEHEADER1"));
		assertTrue(asyncHttp.headers.containsKey("SOMEHEADER2"));		
	}
}
