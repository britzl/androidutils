package se.springworks.android.utils.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import android.test.AndroidTestCase;

import se.springworks.android.utils.time.Period;

public class TestPeriod extends AndroidTestCase {
	
	private static final int SECOND = 1000;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;
	private static final int DAY = 24 * HOUR;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDays() {
		Period p = new Period(3 * DAY + 13 * HOUR + 33 * MINUTE + 44 * SECOND);
		assertTrue(p.hasDays());
		assertEquals(3, p.getDays());
	}

	@Test
	public void testHours() {
		Period p = new Period(3 * DAY + 13 * HOUR + 33 * MINUTE + 44 * SECOND);
		assertTrue(p.hasHours());
		assertEquals(13, p.getHours());
		
		p = new Period(3 * DAY + 0 * HOUR + 33 * MINUTE + 44 * SECOND);
		assertTrue(p.hasHours());
		assertEquals(0, p.getHours());
	}

	@Test
	public void testMinutes() {
		Period p = new Period(3 * DAY + 13 * HOUR + 33 * MINUTE + 44 * SECOND);
		assertTrue(p.hasMinutes());
		assertEquals(33, p.getMinutes());
		
		p = new Period(3 * DAY + 13 * HOUR + 0 * MINUTE + 44 * SECOND);
		assertTrue(p.hasMinutes());
		assertEquals(0, p.getMinutes());
 
	}

	@Test
	public void testSeconds() {
		Period p = new Period(3 * DAY + 13 * HOUR + 33 * MINUTE + 44 * SECOND);
		assertTrue(p.hasSeconds());
		assertEquals(44, p.getSeconds());
		
		p = new Period(3 * DAY + 13 * HOUR + 33 * MINUTE + 0 * SECOND);
		assertTrue(p.hasSeconds());
		assertEquals(0, p.getSeconds());
	}

}
