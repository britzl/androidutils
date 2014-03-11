package se.springworks.android.utils.test;

import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.file.StorageFileHandler;
import se.springworks.android.utils.uuid.UUIDAndroidIdProvider;
import android.test.AndroidTestCase;

public class TestUUIDAndroidIdProvider  extends AndroidTestCase {
	
	private UUIDAndroidIdProvider provider;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		provider = new UUIDAndroidIdProvider(getContext());
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetUUIDForBrokenAndroidId() {
		UUID uuid1 = provider.getUUIDForAndroidId(UUIDAndroidIdProvider.BROKEN_ANDROID_ID);
		UUID uuid2 = provider.getUUIDForAndroidId(UUIDAndroidIdProvider.BROKEN_ANDROID_ID);
		assertNotNull(uuid1);
		assertNotNull(uuid2);
		assertFalse(uuid1.equals(uuid2));
	}

	@Test
	public void testGetUUIDForAndroidId() {
		UUID uuid1 = provider.getUUIDForAndroidId("9774d56d682e549d");
		UUID uuid2 = provider.getUUIDForAndroidId("9774d56d682e549d");
		assertNotNull(uuid1);
		assertNotNull(uuid2);
		assertTrue(uuid1.equals(uuid2));
	}

	@Test
	public void testGetUUID() {
		UUID uuid1 = provider.get();
		assertNotNull(uuid1);
		UUID uuid2 = provider.get();
		assertTrue(uuid1.equals(uuid2));
	}

}
