package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import hw5.DB;
import hw5.DBCollection;
import hw5.DBCursor;

class CursorTester {
	
	/**
	 * Things to consider testing:
	 * 
	 * hasNext (done?)
	 * count (done?)
	 * next (done?)
	 */

	@Test
	public void testFindAll() {
		DB db = new DB("data");
		DBCollection test = db.getCollection("test");
		DBCursor results = test.find();
		
		assertTrue(results.count() == 3);
		assertTrue(results.hasNext());
		JsonObject d1 = results.next(); //pull first document
		//verify contents?
		assertTrue(results.hasNext());//still more documents
		JsonObject d2 = results.next(); //pull second document
		//verfiy contents?
		assertTrue(results.hasNext()); //still one more document
		JsonObject d3 = results.next();//pull last document
		assertFalse(results.hasNext());//no more documents
	}
}
