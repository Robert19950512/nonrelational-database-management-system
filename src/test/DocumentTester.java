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
		String json = "{\"key\":\"value\"}";//setup
		JsonObject results = Document.parse(json); //call method to be tested
		
		assertTrue(results.getAsJsonPrimitive("key").getAsString().equals("value")); //verify results
	}
	
	@Test
	public void testParseArray1() {
		String json = "{\"key\":[1,2,3]}";
		JsonObject results = Document.parse(json); //call method to be tested
		assertTrue(results.getAsJsonArray("key").get(0).getAsInt() == 1); //verify results
		assertTrue(results.getAsJsonArray("key").get(1).getAsInt() == 2);
		assertTrue(results.getAsJsonArray("key").get(2).getAsInt() == 3);
	}
	
	@Test
	public void testParseArray2() {
		String json = "{\"key\":[1,\"two\",true]}";
		JsonObject results = Document.parse(json); //call method to be tested
		assertTrue(results.getAsJsonArray("key").get(0).getAsInt() == 1); //verify results
		assertTrue(results.getAsJsonArray("key").get(1).getAsString().equals("two"));
		assertTrue(results.getAsJsonArray("key").get(2).getAsBoolean());
	}
	
	@Test
	public void testParseDoc() {
		String json = "{ \"key\":{\"docKey\":\"docValue\"}}";
		JsonObject results = Document.parse(json); //call method to be tested
		assertTrue(results.getAsJsonObject("key").getAsJsonPrimitive("docKey").getAsString().equals("docValue"));
	}
	
	@Test
	public void testParseDoc2Layer() {
		String json = "{ \"key\":{\"docKey\":[1,2]}}";
		JsonObject results = Document.parse(json); //call method to be tested
		//System.out.println(results.getAsJsonObject("key").getAsJsonPrimitive("docKey"));
		assertTrue(results.getAsJsonObject("key").getAsJsonArray("docKey").get(0).getAsInt()==1);
		assertTrue(results.getAsJsonObject("key").getAsJsonArray("docKey").get(1).getAsInt()==2);
		
		json = "{ \"key\":{\"docKey\":{\"eDockey\":\"eDocValue\"}}}";
		results = Document.parse(json);
		assertTrue(results.getAsJsonObject("key").getAsJsonObject("docKey").getAsJsonPrimitive("eDockey").getAsString().equals("eDocValue"));
	}
	@Test
	public void testParseDoc2Layer2() {
		String json = "{ \"key\":[[1,2],[3,4]]}";
		JsonObject results = Document.parse(json); //call method to be tested
		assertTrue(results.getAsJsonArray("key").get(0).getAsJsonArray().get(0).getAsInt() == 1);
		assertTrue(results.getAsJsonArray("key").get(1).getAsJsonArray().get(0).getAsInt() == 3);
		
		json = "{\"key\":[{\"docKey\":\"docValue1\"},{\"docKey\":\"docValue2\"}]}";
		results = Document.parse(json);
		
		assertTrue(results.getAsJsonArray("key").get(0).getAsJsonObject().getAsJsonPrimitive("docKey").getAsString().equals("docValue1"));
		//System.out.println(results.getAsJsonArray("key").get(0));
		assertTrue(results.getAsJsonArray("key").get(1).getAsJsonObject().getAsJsonPrimitive("docKey").getAsString().equals("docValue2"));
	}
	
	@Test
	public void testJsonToStringEasy() {
		String json = "{\"key\":\"value\"}";//setup
		JsonObject obj = Document.parse(json); 
		String result = Document.toJsonString(obj);
		assertTrue(json.equals(result));
	}
	
	@Test
	public void testJsonToString() {
		String json = "{\"key\":[1,2,3]}";
		JsonObject results = Document.parse(json);
		assertTrue(Document.toJsonString(results).equals(json));
		json = "{\"key\":[{\"docKey\":\"docValue1\"},{\"docKey\":\"docValue2\"}]}";
		results = Document.parse(json);
		assertTrue(Document.toJsonString(results).equals(json));
	}
	

}
