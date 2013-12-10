package se.springworks.android.utils.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.adapter.SectionedFragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.test.AndroidTestCase;

public class TestSectionedFragmentPagerAdapter extends AndroidTestCase {

	
	private SectionedFragmentPagerAdapter<Fragment> adapter;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		adapter = new SectionedFragmentPagerAdapter<Fragment>(null);
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCount() {
		Fragment f1 = new Fragment();
		adapter.add(f1, "FRAGMENT1");
		assertEquals(1, adapter.getCount());
		
		Fragment f2 = new Fragment();
		adapter.add(f2, "FRAGMENT2");
		assertEquals(2, adapter.getCount());
	}
	
	@Test
	public void testGetItem() {
		Fragment f1 = new Fragment();
		adapter.add(f1, "FRAGMENT1");
		assertEquals(f1, adapter.getItem(0));

		Fragment f2 = new Fragment();
		adapter.add(f2, "FRAGMENT2");
		assertEquals(f2, adapter.getItem(1));		
	}
	
	@Test
	public void testGetTitle() {
		Fragment f1 = new Fragment();
		adapter.add(f1, "FRAGMENT1");
		assertEquals("FRAGMENT1", adapter.getPageTitle(0));

		Fragment f2 = new Fragment();
		adapter.add(f2, "FRAGMENT2");
		assertEquals("FRAGMENT2", adapter.getPageTitle(1));
	}
}
