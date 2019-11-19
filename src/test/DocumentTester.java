package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import hw5.Document;

class DocumentTester {
	
	/*
	 * Things to consider testing:
	 * 
	 * Invalid JSON
	 * 
	 * Properly parses embedded documents
	 * Properly parses arrays
	 * Properly parses primitives (done!)
	 * 
	 * Object to embedded document
	 * Object to array
	 * Object to primitive
	 */
	
	@Test
	public void testParsePrimitive() {
		String json = "{ \"key\":\"value\" }";//setup
		JsonObject results = Document.parse(json); //call method to be tested
		assertTrue(results.getAsJsonPrimitive("key").getAsString().equals("value")); //verify results
	}

}
