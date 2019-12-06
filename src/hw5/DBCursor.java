package hw5;
/*
 * Student 1 name: Fa Long (id:462512)
 * Student 2 name: Zhuo Wei (id: 462473)
 * Date: Dec 6th, 2019
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class DBCursor implements Iterator<JsonObject>{
	List<JsonObject> documents;
	public DBCursor(DBCollection collection, JsonObject query, JsonObject fields) {
		documents = new ArrayList<JsonObject> ();
		// if query is null, directly copy the document list from collection
		if (query == null) {
			documents = collection.documents;
		} else {
		//find the documents contains the content of query
			for (int i = 0; i< collection.count(); i++) {
				JsonObject curr = collection.getDocument(i);
				Set<String> queryKeySet = query.keySet();
				for(String key : queryKeySet) {
					if (query.get(key).isJsonPrimitive()) {
						if (curr.has(key) && curr.get(key).equals(query.get(key))) {
							documents.add(curr);
						}
					} else {
						valueObject (query, curr, key);
					}
				}	
			}
		}
		//projection
		if (fields != null) {
			List<JsonObject> docAfterProj = new ArrayList<JsonObject> ();
			Set<String> fieldKeySet = fields.keySet();
			//System.out.println("123" +fieldKeySet);
			for(String key : fieldKeySet) {
				if (fields.get(key).isJsonPrimitive()) {
					int projIndicator = fields.get(key).getAsInt();
					for(JsonObject doc : documents){
						JsonObject newDoc = new JsonObject();
						for(String k : doc.keySet()) {
							if (projIndicator == 0) {
								if (!k.equals(key)) {
									newDoc.add(k, doc.get(k));
								}
							} else if (projIndicator == 1) {
								if (k.equals(key)) {
									newDoc.add(k, doc.get(k));
								}
							}
						}
						docAfterProj.add(newDoc);
					}
					
				}
			}
			documents = docAfterProj;
		}
	}
	
	//return 0, ==
	//return -1, <
	//return 1, >
	private void valueObject (JsonObject query, JsonObject curr, String key) {
		Set<String> comparisonKeys = query.getAsJsonObject(key).keySet();
		if (comparisonKeys != null) {
			JsonObject comparisons = query.getAsJsonObject(key);
			for(String compKey : comparisonKeys) {
				if (compKey.contains("$")) {
					if (comparisons.get(compKey).isJsonPrimitive()) {
						JsonPrimitive target = comparisons.getAsJsonPrimitive(compKey);
						switch(compKey) {
						case "$eq":
							if (curr.has(key) && compareTo(curr.getAsJsonPrimitive(key),target) == 0) {
							documents.add(curr);
							}// else {
	//							//JsonObject target = comparisons.getAsJsonObject(compKey);
	//							//if (curr.has(key) && curr.get(key).equals(target)) {
	//							//documents.add(curr);
	//							}
	//						}
							break;
						case "$gt":
							if (curr.has(key) && compareTo(curr.getAsJsonPrimitive(key),target) > 0) {
								documents.add(curr);
							}
							break;
						case "$gte":
							if (curr.has(key) && compareTo(curr.getAsJsonPrimitive(key),target) >= 0) {
								documents.add(curr);
							}
							break;
						
						case "$lt":
							if (curr.has(key) && compareTo(curr.getAsJsonPrimitive(key),target) < 0) {
								documents.add(curr);
							}
							break;
						case "$lte":
							if (curr.has(key) && compareTo(curr.getAsJsonPrimitive(key),target) <= 0) {
								documents.add(curr);
							}
							break;
						case "$ne":
							if (curr.has(key) && compareTo(curr.getAsJsonPrimitive(key),target) != 0) {
								documents.add(curr);
							}
							break;
						}
					} else if (comparisons.get(compKey).isJsonArray()) {
						JsonArray target = comparisons.getAsJsonArray(compKey);
						switch(compKey) {
						case "$nin":
							boolean in = false;
							for(JsonElement t: target.getAsJsonArray()) {
								
								if (t.isJsonPrimitive() && curr.has(key)){ 
									if (compareTo(curr.getAsJsonPrimitive(key),t.getAsJsonPrimitive()) == 0) {
										in = true;
									}		
								} else {
									in = true;
								}
							}
							if (in == false) {
								documents.add(curr);
							}
						break;
					
						case "$in":
							for(JsonElement t: target.getAsJsonArray()) {
								if (t.isJsonPrimitive() && curr.has(key) && compareTo(curr.getAsJsonPrimitive(key),t.getAsJsonPrimitive()) == 0) {
									documents.add(curr);
								}
							}
						break;
						}
					
					}
				}else if (curr.has(key) && curr.getAsJsonObject(key).equals(query.getAsJsonObject(key))) {
					documents.add(curr);
				} 

			}
		}
	}
	private int compareTo (JsonPrimitive jp1, JsonPrimitive jp2) {
		if (jp1.equals(jp2)) {
			return 0;
		} else if (jp1.isString() && jp2.isString()){
			return jp1.getAsString().compareTo(jp2.getAsString());
		} else {
			return jp1.getAsInt() < jp2.getAsInt() ? -1: 1;
		}
	}
	
	/**
	 * Returns true if there are more documents to be seen
	 */
	public boolean hasNext() {
		if (documents.size() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns the next document
	 */
	public JsonObject next() {
		JsonObject jo;
		if (documents.size() == 0) {
			return null;
		} else {
			jo = documents.get(0);
			documents.remove(0);
		}
		return jo;
	}
	
	/**
	 * Returns the total number of documents
	 */
	public long count() {
		return documents.size();
	}

}
