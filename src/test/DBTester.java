package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.After;
import org.junit.jupiter.api.Test;

import hw5.DB;
import hw5.DBCollection;

class DBTester {
	
	/**
	 * Things to consider testing:
	 * 
	 * Properly creates directory for new DB (done)
	 * Properly accesses existing directory for existing DB (done)
	 * Properly accesses collection
	 * Properly drops a database
	 * Special character handling?
	 */
	
	@Test
	public void testCreateDB() {
		DB hw5 = new DB("hw5"); //call method
		assertTrue(new File("testfiles/hw5").exists()); //verify results
	}
	
	@Test
	public void testAccessDirAndCollection() {
		DB hw5 = new DB("data"); //call method
		DBCollection test = hw5.getCollection("test");
		assertTrue(new File("testfiles/data/test.json").exists());
	}
	
	@Test
	public void testDrop() {
		DB hw5 = new DB("hw5"); 
		hw5.dropDatabase();
		assertFalse(new File("testfiles/hw5").exists());
	}
	@Test
	public void testmakeCollection() {
		DB hw5 = new DB("hw5"); 
		hw5.getCollection("newCollection");
		assertTrue(new File("testfiles/hw5/newCollection.json").exists());
	}
	
	@Test
	public void testDropWithCollection() {
		DB hw5 = new DB("hw5"); 
		hw5.getCollection("newCollection");
		hw5.dropDatabase();
		assertFalse(new File("testfiles/hw5").exists());
	}
	
	@After
	public void cleanUp() {
		DB hw5 = new DB("hw5"); 
		hw5.dropDatabase();
	}
	

}
