package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import hw5.DB;
import hw5.DBCollection;
import hw5.DBCursor;

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
	 * Inserts
	 * Updates
	 * Deletes
	 * 
	 * getDocument (done?)
	 * drop
	 */
	
	@Test
	public void testGetDocument() {
		DB db = new DB("data");
		DBCollection test = db.getCollection("test");
		JsonObject primitive = test.getDocument(0);
		assertTrue(primitive.getAsJsonPrimitive("key").getAsString().equals("value"));
	}
}
