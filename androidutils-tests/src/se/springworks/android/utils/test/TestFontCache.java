package se.springworks.android.utils.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.font.FontCache;
import android.graphics.Typeface;
import android.test.AndroidTestCase;

public class TestFontCache extends AndroidTestCase {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		FontCache.clear();
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGet() {
		assertNotNull(FontCache.get("fonts/impact.ttf", getContext()));
		assertSame(FontCache.get("fonts/impact.ttf", getContext()), FontCache.get("fonts/impact.ttf", getContext()));
		assertNull(FontCache.get("fonts/doesnotexist.ttf", getContext()));
	}
	
	@Test
	public void testClear() {
		Typeface tf = FontCache.get("fonts/impact.ttf", getContext());
		FontCache.clear();
		assertNotSame(tf, FontCache.get("fonts/impact.ttf", getContext()));
	}
	
	@Test
	public void testRemove() {
		Typeface impact = FontCache.get("fonts/impact.ttf", getContext());
		Typeface tahoma = FontCache.get("fonts/tahoma.ttf", getContext());
		FontCache.remove("fonts/impact.ttf");
		assertNotSame(impact, FontCache.get("fonts/impact.ttf", getContext()));
		assertSame(tahoma, FontCache.get("fonts/tahoma.ttf", getContext()));		
	}

}
