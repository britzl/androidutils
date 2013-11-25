package se.springworks.android.utils.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.cache.MemCache;
import android.test.AndroidTestCase;

public class TestMemCache extends AndroidTestCase {

	private MemCache<String> cache;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		cache = new MemCache<String>();
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testClearCache() {
		cache.cache("KEY1", "SOME DATA NEVER EXPIRING");
		cache.cache("KEY2", "SOME OTHER DATA EXPIRING SOON", 100);
		cache.clear();
		assertFalse(cache.contains("KEY1"));
		assertFalse(cache.contains("KEY2"));
	}
	
	@Test
	public void testExpired() throws InterruptedException {
		cache.cache("KEY1", "SOME DATA EXPIRING SOON", 10);
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
	public void testOverwrite() {
		cache.cache("KEY1", "SOME DATA");
		cache.cache("KEY1", "SOME OTHER DATA");
		assertEquals(cache.get("KEY1"), "SOME OTHER DATA");		
	}
	
	@Test
	public void testCache() {
		assertFalse(cache.contains("KEY1"));
		cache.cache("KEY1", "SOME DATA");
		assertTrue(cache.contains("KEY1"));
		assertEquals(cache.get("KEY1"), "SOME DATA");
	}
	
}
