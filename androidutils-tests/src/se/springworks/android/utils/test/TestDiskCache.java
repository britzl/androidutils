package se.springworks.android.utils.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.cache.DiskCache;
import se.springworks.android.utils.cache.ICache;
import se.springworks.android.utils.cache.ICache.CacheException;
import se.springworks.android.utils.file.IFileHandler;
import se.springworks.android.utils.file.StorageFileHandler;
import se.springworks.android.utils.file.StorageFileHandler.StorageMode;
import se.springworks.android.utils.inject.GrapeGuice;
import se.springworks.android.utils.json.IJsonParser;
import se.springworks.android.utils.json.JacksonParser;
import se.springworks.android.utils.persistence.IKeyValueStorage;
import se.springworks.android.utils.persistence.NamedSharedPreferencesStorage;
import android.content.Context;
import android.test.AndroidTestCase;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class TestDiskCache extends AndroidTestCase {
	
	public class TestDiskCacheModule extends AbstractModule {
		private Context context;

		public TestDiskCacheModule(Context context) {
			this.context = context;
		}
		
		@Override
		protected void configure() {
			bind(Context.class).toInstance(context);

			install(new FactoryModuleBuilder().implement(IKeyValueStorage.class, NamedSharedPreferencesStorage.class).build(IKeyValueStorage.NamedKeyValueStorageFactory.class));

//			bind(IKeyValueStorage.class).to(SharedPreferencesStorage.class);
			bind(IJsonParser.class).to(JacksonParser.class);
			bind(IFileHandler.class).to(StorageFileHandler.class);
			bind(new TypeLiteral<ICache<String>>(){}).to(new TypeLiteral<DiskCache<String>>(){});

		}
	}
	
	@Inject
	private ICache<String> cache;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		StorageFileHandler.globalStorageMode = StorageMode.PREFEREXTERNALFILES;
		GrapeGuice.getInjector(this).rebind(new TestDiskCacheModule(getContext())).injectMembers(this);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		cache.clear();
	}


	@Test
	public void testClearCache() throws CacheException {
		cache.cache("KEY1", "SOME DATA NEVER EXPIRING");
		cache.cache("KEY2", "SOME OTHER DATA EXPIRING SOON", 100);
		cache.clear();
		assertFalse(cache.contains("KEY1"));
		assertFalse(cache.contains("KEY2"));
	}
	
	@Test
	public void testExpired() throws InterruptedException, CacheException {
		cache.cache("KEY1", "SOME DATA EXPIRING SOON", 800);
		cache.cache("KEY2", "SOME DATA NEVER EXPIRING");
		cache.cache("KEY3", "SOME DATA EXPIRING LATER", 1500);
		assertTrue(cache.contains("KEY1"));
		assertTrue(cache.contains("KEY2"));
		assertTrue(cache.contains("KEY3"));
		Thread.sleep(1000);
		assertFalse(cache.contains("KEY1"));
		assertTrue(cache.contains("KEY2"));
		assertTrue(cache.contains("KEY3"));
		Thread.sleep(1000);
		assertFalse(cache.contains("KEY3"));
	}
	
	@Test
	public void testOverwrite() throws CacheException {
		cache.cache("KEY1", "SOME DATA");
		cache.cache("KEY1", "SOME OTHER DATA");
		assertEquals("SOME OTHER DATA", cache.get("KEY1"));		
	}
	
	@Test
	public void testCache() throws CacheException {
		assertFalse(cache.contains("KEY1"));
		cache.cache("KEY1", "SOME DATA");
		assertTrue(cache.contains("KEY1"));
		assertEquals("SOME DATA", cache.get("KEY1"));
	}
}
