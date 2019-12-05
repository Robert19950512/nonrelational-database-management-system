package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import hw5.DB;
import hw5.DBCollection;
import hw5.DBCursor;
import hw5.Document;

class CursorTester {
	
	/**
	 * Things to consider testing:
	 * 
	 * hasNext (done?)
	 * count (done?)
	 * next (done?)
	 */
	
	DB db;
	DBCollection test;
	DBCollection test2;
	@BeforeEach
	public void setup() {
		db = new DB("data");
		test = db.getCollection("test");
		test2 = db.getCollection("test2");
		
	}
	@Test
	public void testFindAll() {
		DBCursor results = test.find();
		assertTrue(results.count() == 3);
		assertTrue(results.hasNext());
		JsonObject d1 = results.next(); //pull first document
		assertTrue(results.hasNext());//still more documents
		JsonObject d2 = results.next(); //pull second document
		assertTrue(results.hasNext()); //still one more document
		JsonObject d3 = results.next();//pull last document
		assertFalse(results.hasNext());//no more documents
	}
		
	@Test
	public void testFindWithQuery() {
		String queryString = "{ \"name\":\"student1\" }";
		JsonObject query = Document.parse(queryString);
		DBCursor result = test2.find(query);
		assertTrue(result.hasNext());
		JsonObject d1 = result.next();
		assertTrue(d1.getAsJsonPrimitive("score1").getAsString().equals("95"));
		assertTrue(d1.getAsJsonPrimitive("score2").getAsString().equals("99"));
		assertFalse(result.hasNext());
		
	}
	
	@Test
	public void testHasNext() {
		String queryString = "{ \"name\":\"student1\" }";
		JsonObject query = Document.parse(queryString);
		DBCursor result = test2.find(query);
		assertTrue(result.hasNext());
		result.next();
		assertTrue(result.hasNext());
		result.next();
		assertFalse(result.hasNext());
		
	}
	
	@Test
	public void testFindWithMultipleResults() {
		String queryString = "{ \"name\":\"student1\" }";
		JsonObject query = Document.parse(queryString);
		DBCursor result = test2.find(query);
		JsonObject d1 = result.next();
		assertTrue(d1.getAsJsonPrimitive("score1").getAsString().equals("95"));
		assertTrue(d1.getAsJsonPrimitive("score2").getAsString().equals("99"));
		assertTrue(result.hasNext());//still more documents
		JsonObject d2 = result.next(); //pull second document
		assertTrue(d1.getAsJsonPrimitive("score3").getAsString().equals("90"));
		assertTrue(d1.getAsJsonPrimitive("score4").getAsString().equals("92"));
		
	}
	
	@Test
	public void testFindWithQueryAndProject1() {
		String queryString = "{ \"name\":\"student1\" }";
		String projectString = "{ \"name\":\"1\"}";
		// only the key that is mentioned with "1" are preserved.
		JsonObject query = Document.parse(queryString);
		JsonObject project = Document.parse(projectString);
		DBCursor result = test2.find(query,project);
		assertTrue(result.count() == 2);
		JsonObject d1 = result.next();
		assertTrue(d1.size() == 1);
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("student1"));
		
	}
	
	@Test
	public void testFindWithQueryAndProject0() {
		String queryString = "{ \"name\":\"student1\"}";
		// I'm assuming all other keys are preserved if we specify "0"
		// in the project operation.
		// if the project key doesn't exist, just preserve everything.
		String projectString = "{ \"score1\":\"0\"}";
		JsonObject query = Document.parse(queryString);
		JsonObject project = Document.parse(projectString);
		DBCursor result = test2.find(query,project);
		assertTrue(result.count() == 2);
		JsonObject d1 = result.next();
		assertTrue(d1.size() == 2);  // score1, score2
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("student1"));
		assertTrue(d1.getAsJsonPrimitive("score2").getAsString().equals("99"));
		JsonObject d2 = result.next();
		assertTrue(d2.size() == 3);//name
		assertTrue(d2.getAsJsonPrimitive("name").getAsString().equals("student1"));
		assertTrue(d2.getAsJsonPrimitive("score3").getAsString().equals("90"));
		assertTrue(d2.getAsJsonPrimitive("score4").getAsString().equals("92"));
		
	}

	@Test
	public void testEqQuery() {
		// find query with euals relational operation
		String queryString = "{ \"value\":\"{\"$eq\":1}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 1);
		JsonObject d1 = results.next();
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("s1"));
			
	}
	
	@Test
	public void testGtQuery() {
		
		String queryString = "{ \"value\":\"{\"$gt\":2}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 1);
		JsonObject d1 = results.next();
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("s3"));
	}
	
	@Test
	public void testGteQuery() {
		String queryString = "{ \"value\":\"{\"$gte\":2}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 2);
		JsonObject d1 = results.next();
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("s2"));
		JsonObject d2 = results.next();
		assertTrue(d2.getAsJsonPrimitive("name").getAsString().equals("s3"));
		
	}
	
	@Test
	public void testInQuery() {
		String queryString = "{ \"value\":\"{\"$in\":[1,2,3]}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 3);
		JsonObject d1 = results.next();
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("s1"));
		JsonObject d2 = results.next();
		assertTrue(d2.getAsJsonPrimitive("name").getAsString().equals("s2"));
		JsonObject d3 = results.next();
		assertTrue(d3.getAsJsonPrimitive("name").getAsString().equals("s3"));
	}
	
	@Test
	public void testLtQuery() {
		String queryString = "{ \"value\":\"{\"$lt\":2}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 1);
		JsonObject d1 = results.next();
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("s1"));
		
	}
	
	@Test
	public void testLteQuery() {
		String queryString = "{ \"value\":\"{\"$lte\":2}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 2);
		JsonObject d1 = results.next();
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("s1"));
		JsonObject d2 = results.next();
		assertTrue(d2.getAsJsonPrimitive("name").getAsString().equals("s2"));
		
	}
	
	@Test
	public void testNeQuery() {
		String queryString = "{ \"value\":\"{\"$ne\":2}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 2);
		JsonObject d1 = results.next();
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("s1"));
		JsonObject d2 = results.next();
		assertTrue(d2.getAsJsonPrimitive("name").getAsString().equals("s3"));
	}
	
	@Test
	public void testNinQuery() {
		String queryString = "{ \"value\":\"{\"$nin\":[1,2,3]}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 0);
		
	}
	
	@Test
	public void testInStringQuery() {
		String queryString = "{ \"name\":\"{\"$in\":[\"student1\",\"s1\"]}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 3);
		JsonObject d1 = results.next();
		assertTrue(d1.getAsJsonPrimitive("score1").getAsString().equals("95"));
		JsonObject d2 = results.next();
		assertTrue(d2.getAsJsonPrimitive("score3").getAsString().equals("90"));
		JsonObject d3 = results.next();
		assertTrue(d3.getAsJsonPrimitive("value").getAsInt() == 1);
		
	}
	
	@Test
	public void testEmbeddedQuery1() {
		String queryString = "{ \"embedded\":{\"key\":\"value2\"}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 1);
		JsonObject d1 = results.next();
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("embedded2"));
	}
	
	// not necessarily to pass(to many layer of embedded)
	@Test
	public void testEmbeddedQuery2() {
		String queryString = "{ \"embedded\":{\"key\":{\"$in\":[\"value1\",\"value2\"]}}}";
		JsonObject query = Document.parse(queryString);
		DBCursor results = test2.find(query);
		assertTrue(results.count() == 2);
		JsonObject d1 = results.next();
		assertTrue(d1.getAsJsonPrimitive("name").getAsString().equals("embedded1"));
		JsonObject d2 = results.next();
		assertTrue(d2.getAsJsonPrimitive("name").getAsString().equals("embedded2"));
	}
	
	
}
