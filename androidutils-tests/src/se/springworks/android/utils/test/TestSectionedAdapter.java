package se.springworks.android.utils.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.adapter.SectionedAdapter;
import android.content.Context;
import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestSectionedAdapter extends AndroidTestCase {

	private class TestAdapter extends SectionedAdapter<String, String> {
		
		private Context context;

		public TestAdapter(Context context) {
			this.context = context;
		}
		
		@Override
		public View getSectionView(String section, View convertView,
				ViewGroup parent) {
			if(convertView == null) {
				convertView = new TextView(context);
			}
			TextView tv = (TextView)convertView;
			tv.setText(section);
			return tv;
		}

		@Override
		public View getItemView(String item, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = new TextView(context);
			}
			TextView tv = (TextView)convertView;
			tv.setText(item);
			return tv;
		}	
	}
	
	private TestAdapter adapter;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		adapter = new TestAdapter(getContext());
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCount() {
		assertEquals(0, adapter.getCount());
		adapter.addItem("SECTION1", "ITEM1");
		assertEquals(2, adapter.getCount());
	}
	
	@Test
	public void testAddSection() {
		adapter.addSection("SECTION1");
		assertEquals(1, adapter.getCount());
		adapter.addSection("SECTION2");
		assertEquals(2, adapter.getCount());
	}
	
	@Test
	public void testAddItem() {
		adapter.addItem("SECTION1", "ITEM1");
		assertEquals(2, adapter.getCount());
		adapter.addItem("SECTION1", "ITEM2");
		assertEquals(3, adapter.getCount());
	}
	
	@Test
	public void testAddItems() {
		List<String> itemsInSection1 = new ArrayList<String>();
		itemsInSection1.add("S1_1");
		itemsInSection1.add("S1_2");
		itemsInSection1.add("S1_3");
		adapter.addItems("SECTION1", itemsInSection1);

		
		List<String> itemsInSection2 = new ArrayList<String>();
		itemsInSection2.add("S2_1");
		itemsInSection2.add("S2_2");
		itemsInSection2.add("S2_3");
		adapter.addItems("SECTION2", itemsInSection2);

		assertEquals(8, adapter.getCount());
	}
	
	@Test
	public void testGetViewType() {
		List<String> itemsInSection1 = new ArrayList<String>();
		itemsInSection1.add("S1_1");
		itemsInSection1.add("S1_2");
		itemsInSection1.add("S1_3");
		adapter.addItems("SECTION1", itemsInSection1);
		adapter.addSection("SECTION2");

		assertEquals(0, adapter.getItemViewType(0));
		assertEquals(1, adapter.getItemViewType(1));
		assertEquals(1, adapter.getItemViewType(2));
		assertEquals(1, adapter.getItemViewType(3));
		assertEquals(0, adapter.getItemViewType(4));
	}
	
	@Test
	public void testGetViewTypeCount() {
		assertEquals(2, adapter.getViewTypeCount());
	}
		
	
	@Test
	public void testIsEnabled() {
		adapter.addItem("SECTION1", "ITEM1");
		assertFalse(adapter.isEnabled(0));
		assertTrue(adapter.isEnabled(1));
	}
	
	@Test
	public void testIsSection() {
		adapter.addItem("SECTION1", "ITEM1");
		assertTrue(adapter.isSection(0));
		assertFalse(adapter.isSection(1));
	}
	
	@Test
	public void testGetItem() {
		adapter.addItem("SECTION1", "ITEM1");
		assertEquals("SECTION1", adapter.getItem(0));
		assertEquals("ITEM1", adapter.getItem(1));
	}
	
	
	@Test
	public void testGetView() {
		adapter.addItem("SECTION1", "ITEM1");
		adapter.addItem("SECTION1", "ITEM2");
		adapter.addItem("SECTION2", "ITEM1");
		
		TextView tv = (TextView)adapter.getView(0, null, null);
		assertNotNull(tv);
		assertEquals("SECTION1", tv.getText());

		tv = (TextView)adapter.getView(1, null, null);
		assertNotNull(tv);
		assertEquals("ITEM1", tv.getText());
	}
	
	@Test
	public void testGetViewRecycled() {
		adapter.addItem("SECTION1", "ITEM1");
		final TextView original = new TextView(getContext());
		TextView tv = (TextView)adapter.getView(0, original, null);
		assertEquals(original, tv);
	}

}
