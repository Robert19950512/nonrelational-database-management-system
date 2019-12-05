package hw5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;

public class DBCursor implements Iterator<JsonObject>{
	List<JsonObject> documents;
	public DBCursor(DBCollection collection, JsonObject query, JsonObject fields) {
		documents = new ArrayList<JsonObject> ();
		// if query is null, directly copy the document list from collection
		if (query == null) {
			documents = collection.documents;
		}
		//find the documents contains the content of query
		for (int i = 0; i< collection.count(); i++) {
			JsonObject curr = collection.getDocument(i);
			Set<String> queryKeySet = query.keySet();
			for(String key : queryKeySet) {
				if (curr.has(key) && curr.getAsJsonObject(key).equals(query.getAsJsonObject(key))) {
					documents.add(curr);
				}
				
			}
		}
		//projection
		if (fields != null) {
			//proje
		}
	}
	
	/**
	 * Returns true if there are more documents to be seen
	 */
	public boolean hasNext() {
		return false;
	}

	/**
	 * Returns the next document
	 */
	public JsonObject next() {
		return null;
	}
	
	/**
	 * Returns the total number of documents
	 */
	public long count() {
		return 0;
	}

}
