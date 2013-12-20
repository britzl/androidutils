package se.springworks.android.utils.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.resource.ParameterLoader;

import android.test.AndroidTestCase;

public class TestParameterLoader extends AndroidTestCase {

	private ParameterLoader loader;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		loader = new ParameterLoader(getContext());
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetString() {
		assertTrue(loader.hasString("parameterloaderstring"));
		assertEquals("Lorem ipsum", loader.getString("parameterloaderstring"));

		assertFalse(loader.hasString("doesnotexist"));
		assertNull(loader.getString("doesnotexist"));
	}
	
	@Test
	public void testGetInt() {
		assertTrue(loader.hasInt("parameterloaderint"));
		assertEquals(12345, loader.getInt("parameterloaderint", 0));

		assertEquals(56789, loader.getInt("parameterloaderstring", 56789));

		assertFalse(loader.hasInt("doesnotexist"));
		assertEquals(56789, loader.getInt("doesnotexist", 56789));
	}
	
	@Test
	public void testGetBoolean() {
		assertTrue(loader.hasBoolean("parameterloaderbool"));
		assertTrue(loader.getBoolean("parameterloaderbool"));
		assertFalse(loader.hasBoolean("doesnotexist"));
		assertFalse(loader.getBoolean("doesnotexist"));
	}
}
