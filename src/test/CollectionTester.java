package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import hw5.DB;
import hw5.DBCollection;
import hw5.DBCursor;
import hw5.Document;

class CollectionTester {
	
	/**
	 * Things to consider testing
	 * 
	 * Queries:
	 * 	Find all
	 * 	Find with relational select
	 * 		Conditional operators
	 * 		Embedded documents
	 * 		Arrays
	 * 	Find with relational project
	 * 
	 * all query tests is inside the CursorTest.java.
	 * Inserts
	 * Updates
	 * Deletes
	 * 
	 * getDocument (done?)
	 * drop
	 */
	
	@Before
	public void setup() {
		DB db = new DB("CollectionTest");
		db.dropDatabase();
	}
	@Test
	public void testGetDocument() {
		DB db = new DB("data");
		DBCollection test = db.getCollection("test");
		System.out.println(test.documents.size());
		JsonObject primitive = test.getDocument(0);
		assertTrue(primitive.getAsJsonPrimitive("key").getAsString().equals("value"));
		assertTrue(test.getDocument(1).getAsJsonObject("embedded").getAsJsonPrimitive("key2").getAsString().equals("value2"));
		assertTrue(test.getDocument(2).getAsJsonArray("array").get(0).getAsString().equals("one"));
	}
	
	@Test
	public void testCreatNewCollection() {
		DB db = new DB("CollectionTest");
		db.getCollection("cTest");
		assertTrue(new File("testfiles/CollectionTest/cTest.json").exists());
	}
	
	@Test
	public void testInsert() {
		DB db = new DB("CollectionTest");
		DBCollection testCollection = db.getCollection("cTest");
		String json = "{ \"key\":\"value\" }";//setup
		JsonObject toInsert = Document.parse(json); //call method to be tested
		testCollection.insert(toInsert);		
		assertTrue(testCollection.count() == 1);
		//id is added
		assertFalse(testCollection.getDocument(0).getAsJsonPrimitive("_id").isJsonNull());
		assertTrue(testCollection.getDocument(0).getAsJsonPrimitive("key").getAsString().equals("value"));
		
	}
	
	@Test
	public void testUpdate() {
		DB db = new DB("CollectionTest");
		DBCollection testCollection = db.getCollection("cTest");
		String json = "{ \"key\":\"value\" }";//setup
		JsonObject toInsert = Document.parse(json); //call method to be tested
		testCollection.insert(toInsert);
		assertTrue(testCollection.getDocument(0).getAsJsonPrimitive("key").getAsString().equals("value"));
		
		String updateJson = "{ \"key\":\"updateValue\" }";
		JsonObject update = Document.parse(updateJson);
		testCollection.update(toInsert, update, false);
		testCollection = db.getCollection("cTest");
		// reload from file system to check weather actaull file is changed.
		assertTrue(testCollection.getDocument(0).getAsJsonPrimitive("key").getAsString().equals("updateValue"));
		
	}
	
	@Test
	public void testMultipleUpdate() {
		DB db = new DB("CollectionTest");
		DBCollection testCollection = db.getCollection("cTest");
		String json = "{ \"key\":\"value\" }";//setup
		JsonObject toInsert = Document.parse(json); //call method to be tested
		// insert two identical documents
		testCollection.insert(toInsert);
		testCollection.insert(toInsert);
		assertTrue(testCollection.getDocument(0).getAsJsonPrimitive("key").getAsString().equals("value"));
		String updateJson = "{ \"key\":\"updateValue\" }";
		JsonObject update = Document.parse(updateJson);
		testCollection.update(toInsert, update, true);
		assertTrue(testCollection.getDocument(0).getAsJsonPrimitive("key").getAsString().equals("updateValue"));
		assertTrue(testCollection.getDocument(1).getAsJsonPrimitive("key").getAsString().equals("updateValue"));
		
	}
	
	@Test
	public void testDelete() {
		DB db = new DB("CollectionTest");
		DBCollection testCollection = db.getCollection("cTest");
		String json = "{ \"key\":\"value\" }";//setup
		JsonObject toInsert = Document.parse(json); //call method to be tested
		testCollection.insert(toInsert);
		assertTrue(testCollection.count()==1);
		testCollection.remove(toInsert, false);
		assertTrue(testCollection.count()==0);
		
	}
	
	@Test
	public void testMultipleDelete() {
		DB db = new DB("CollectionTest");
		DBCollection testCollection = db.getCollection("cTest");
		String json = "{ \"key\":\"value\" }";//setup
		JsonObject toInsert = Document.parse(json); //call method to be tested
		testCollection.insert(toInsert);
		assertTrue(testCollection.count()==1);
		testCollection.insert(toInsert);
		assertTrue(testCollection.count()==2);
		testCollection.insert(toInsert);
		assertTrue(testCollection.count()==3);
		testCollection.remove(toInsert, true);
		assertTrue(testCollection.count()==0);
		
	}
	
	
	@Test
	public void testDrop() {
		DB db = new DB("CollectionTest");
		DBCollection testCollection = db.getCollection("cTest");
		testCollection.drop();
		assertFalse(new File("testfiles/CollectionTest/cTest").exists());
	}
	
	@After
	public void cleanUp() {
		DB db = new DB("CollectionTest");
		DBCollection testCollection = db.getCollection("cTest");
		testCollection.drop();
	}
	
}
