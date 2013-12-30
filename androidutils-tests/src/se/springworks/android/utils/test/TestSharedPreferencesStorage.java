package se.springworks.android.utils.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.MockModule;
import se.springworks.android.utils.inject.GrapeGuice;
import se.springworks.android.utils.persistence.IKeyValueStorage;
import android.test.AndroidTestCase;

import com.google.inject.Inject;

public class TestSharedPreferencesStorage extends AndroidTestCase {

	@Inject
	private IKeyValueStorage storage;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		GrapeGuice.getInjector(getContext()).rebind(new MockModule(getContext())).injectMembers(this);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		storage.removeAll();
	}

	@Test
	public void testPutString() {
		final String key1 = "KEY1";
		final String value1 = "somestring1";
		final String key2 = "KEY2";
		final String value2 = "somestring2";
		assertFalse(storage.contains(key1));
		assertFalse(storage.contains(key2));
		storage.put(key1, value1);
		storage.put(key2, value2);
		assertTrue(storage.contains(key1));
		assertTrue(storage.contains(key2));
		assertTrue(storage.getString(key1).equals(value1));
		assertTrue(storage.getString(key2).equals(value2));
	}

	@Test
	public void testGetStringDefault() {
		final String key = "KEY";
		final String defaultvalue = "defaultvalue";
		assertFalse(storage.contains(key));
		assertTrue(storage.getString(key, defaultvalue).equals(defaultvalue));
	}

	@Test
	public void testPutInts() {
		final String key = "KEY";
		final Integer[] a = { Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.valueOf(3), Integer.valueOf(4) };
		assertFalse(storage.contains(key));
		storage.put(key, a);
		assertTrue(storage.contains(key));
		assertTrue(Arrays.equals(a, storage.getInts(key)));
	}

	@Test
	public void testPutLongs() {
		final String key = "KEY";
		final Long[] a = { Long.MIN_VALUE, Long.MAX_VALUE, Long.valueOf(3), Long.valueOf(4) };
		assertFalse(storage.contains(key));
		storage.put(key, a);
		assertTrue(storage.contains(key));
		assertTrue(Arrays.equals(a, storage.getLongs(key)));
	}

	@Test
	public void testPutInt() {
		final String key = "KEY";
		final int value = Integer.MAX_VALUE;
		assertFalse(storage.contains(key));
		storage.put(key, value);
		assertTrue(storage.contains(key));
		assertTrue(storage.getInt(key) == value);
	}

	@Test
	public void testGetIntDefault() {
		final String key = "KEY";
		final int defaultvalue = 345;
		assertFalse(storage.contains(key));
		assertEquals(storage.getInt(key, defaultvalue), defaultvalue);
	}


	@Test
	public void testPutLong() {
		final String key = "KEY";
		final long value = Long.MAX_VALUE;
		assertFalse(storage.contains(key));
		storage.put(key, value);
		assertTrue(storage.contains(key));
		assertTrue(storage.getLong(key) == value);
	}

	@Test
	public void testGetLongDefault() {
		final String key = "KEY";
		final long defaultvalue = Long.MIN_VALUE;
		assertFalse(storage.contains(key));
		assertEquals(storage.getLong(key, defaultvalue), defaultvalue);
	}

	@Test
	public void testPutBoolean() {
		final String key = "KEY";
		final boolean value = true;
		assertFalse(storage.contains(key));
		storage.put(key, value);
		assertTrue(storage.contains(key));
		assertTrue(storage.getBoolean(key) == value);
	}

	@Test
	public void testGetBooleanDefault() {
		final String key = "KEY";
		final boolean defaultvalue = true;
		assertFalse(storage.contains(key));
		assertEquals(storage.getBoolean(key, defaultvalue), defaultvalue);
	}

	@Test
	public void testPutStrings() {
		final String key = "KEY";
		final Set<String> value = new HashSet<String>();
		value.add("A");
		value.add("B");
		value.add("C");
		assertFalse(storage.contains(key));
		storage.put(key, value);
		assertTrue(storage.contains(key));
		
		Set<String> retrievedSet = storage.getStrings(key);
		assertTrue(value.containsAll(retrievedSet));
	}


	@Test
	public void testRemove() {
		final String key = "STRING";
		final String value = "somestring";
		storage.put(key, value);
		assertTrue(storage.contains(key));
		storage.remove(key);
		assertFalse(storage.contains(key));
	}

	@Test
	public void testRemoveAll() {
		final String key1 = "STRING1";
		final String key2 = "STRING2";
		final String value = "somestring";
		storage.put(key1, value);
		storage.put(key2, value);
		storage.removeAll();
		assertFalse(storage.contains(key1));
		assertFalse(storage.contains(key2));
	}

}
