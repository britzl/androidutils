package se.springworks.android.utils.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.springworks.android.utils.json.JacksonParser;
import se.springworks.android.utils.reflect.JavaTypeToken;
import android.test.AndroidTestCase;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestJacksonParser extends AndroidTestCase {
	
	public static class SimpleClass {
		@JsonProperty
		public String someString = "ABCDEF";
		
		@JsonProperty
		public int someInt = 12345;
		
		@JsonProperty
		public float someFloat = 1.0002f;
		
		@JsonProperty
		public String nullObject = null;
		
		public SimpleClass() {
			
		}
	}

	private static final String[] STRINGS = {"one", "two", "three", "four" }; 
	private static final List<String> LISTOFSTRINGS = Arrays.asList(STRINGS);

	private JacksonParser parser;
	private SimpleClass simpleClass = new SimpleClass();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	public void setUp() throws Exception {
		parser = new JacksonParser();		
	}

	@Override
	@After
	public void tearDown() throws Exception {
	}
	
	private void assertSimpleClass(SimpleClass expected, SimpleClass actual) {
		assertEquals(expected.someString, actual.someString);
		assertEquals(expected.someInt, actual.someInt);
		assertEquals(expected.someFloat, actual.someFloat);
		assertEquals(expected.nullObject, actual.nullObject);
	}
	
	private void assertLists(List<?> expected, List<?> actual) {
		assertEquals(expected.size(), actual.size());
		for(int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i), actual.get(i));	
			assertEquals(expected.get(i).getClass(), actual.get(i).getClass());
		}
	}

	@Test
	public void testToFromObject() {
		String json = parser.toJson(simpleClass);
		json = json.replaceAll("(\\r|\\n)", "");
		json = json.replaceAll("\\s", "");
		assertTrue(json.contains("\"ABCDEF\""));
		assertTrue(json.contains("12345"));
		assertTrue(json.contains("1.0002"));
		assertTrue(json.contains("null"));
		assertTrue(json.contains("\"someString\":"));
		assertTrue(json.contains("\"someInt\":"));
		assertTrue(json.contains("\"someFloat\":"));
		assertTrue(json.contains("\"nullObject\":"));
		
		SimpleClass simpleClassFromJson = parser.fromJson(json, SimpleClass.class);
		assertSimpleClass(simpleClass, simpleClassFromJson);
	}

	@Test
	public void testToFromCollection() {
		String json = parser.toJson(LISTOFSTRINGS);
		json = json.replaceAll("(\\r|\\n)", "");
		json = json.replaceAll("\\s", "");
		assertTrue(json.contains("one"));
		assertTrue(json.contains("two"));
		assertTrue(json.contains("three"));
		assertTrue(json.contains("four"));
		
		ArrayList<String> listFromJson = parser.fromJson(json, new JavaTypeToken<ArrayList<String>>() {});
		assertLists(LISTOFSTRINGS, listFromJson);
	}

	@Test
	public void testToFromFile() throws IOException {
		File tempFile = File.createTempFile("testToFromFile_", null);
		parser.toJson(tempFile, simpleClass);		
		SimpleClass simpleClassFromJson = parser.fromJson(new FileInputStream(tempFile), SimpleClass.class);
		assertSimpleClass(simpleClass, simpleClassFromJson);
		
		tempFile = File.createTempFile("testToFromFile_", null);
		parser.toJson(tempFile, LISTOFSTRINGS);		
		ArrayList<String> listFromJson = parser.fromJson(new FileInputStream(tempFile), new JavaTypeToken<ArrayList<String>>() {});		
		assertLists(LISTOFSTRINGS, listFromJson);
		
		tempFile = File.createTempFile("testToFromFile_", null);
		parser.toJson(new FileOutputStream(tempFile), simpleClass);
		simpleClassFromJson = parser.fromJson(new FileInputStream(tempFile), SimpleClass.class);
		assertSimpleClass(simpleClass, simpleClassFromJson);
	}

}
