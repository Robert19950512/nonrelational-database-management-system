package hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
/*
 * Student 1 name: Fa Long (id:462512)
 * Student 2 name: Zhuo Wei (id: 462473)
 * Date: Dec 6th, 2019
 */
public class DBCollection {

	/**
	 * Constructs a collection for the given database
	 * with the given name. If that collection doesn't exist
	 * it will be created.
	 */
	public List<JsonObject> documents;
	private String name;
	private String path;
	public DBCollection(DB database, String name) {
		documents = new ArrayList<>();
		this.name = name;
		this.path = "testfiles/" + database.getName() + "/" + name + ".json";
		File collection = new File(path);
		if (!collection.exists()) {
			try {
				collection.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileReader fl = new FileReader(collection);
			BufferedReader br = new BufferedReader(fl);
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				line = br.readLine();
				while(line != null) {
					if (!line.trim().isEmpty()) {
						sb.append(line);
					} else {
						this.documents.add(Document.parse(sb.toString()));
						sb = new StringBuilder();
					}
					line = br.readLine();
				}
				if (sb.length() != 0) {
					this.documents.add(Document.parse(sb.toString()));
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a cursor for all of the documents in
	 * this collection.
	 */
	public DBCursor find() {
		return new DBCursor(this, null, null);
	}
	
	/**
	 * Finds documents that match the given query parameters.
	 * 
	 * @param query relational select
	 * @return
	 */
	public DBCursor find(JsonObject query) {
		return new DBCursor(this,query,null);
	}
	
	/**
	 * Finds documents that match the given query parameters.
	 * 
	 * @param query relational select
	 * @param projection relational project
	 * @return
	 */
	public DBCursor find(JsonObject query, JsonObject projection) {
		return new DBCursor(this,query,projection);
	}
	
	/**
	 * Inserts documents into the collection
	 * Must create and set a proper id before insertion
	 * When this method is completed, the documents
	 * should be permanently stored on disk.
	 * @param documents
	 */
	public void insert(JsonObject... documents) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.path,true));
			for (JsonObject doc : documents) {
				doc.addProperty("_id", System.nanoTime());
				this.documents.add(doc);
				bw.write(doc.toString());
				bw.newLine();
				bw.newLine();
				
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * Locates one or more documents and replaces them
	 * with the update document.
	 * @param query relational select for documents to be updated
	 * @param update the document to be used for the update
	 * @param multi true if all matching documents should be updated
	 * 				false if only the first matching document should be updated
	 */
	public void update(JsonObject query, JsonObject update, boolean multi) {
		DBCursor target = this.find(query);
		if (multi) {
			while (target.hasNext()) {
				JsonObject jo = target.next();
				for (int i = 0 ; i < documents.size() ; i++) {
					JsonObject old = documents.get(i);
					if (old == jo) {
						documents.set(i, update);
						break;
					}
				}
			}
		}else {
			if (target.hasNext()) {
				JsonObject jo = target.next();
				for (int i = 0 ; i < documents.size() ; i++) {
					JsonObject old = documents.get(i);
					if (old == jo) {
						documents.set(i, update);
						break;
					}
				}
			}
		}
		writeToFile();
		 
	}
	public void writeToFile() {
		File oldf = new File(path);
		oldf.delete();
		try {
			oldf.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.path,true));
			for (JsonObject doc : documents) {
				doc.addProperty("_id", System.nanoTime());
				bw.write(doc.toString());
				bw.newLine();
				bw.newLine();
				
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes one or more documents that match the given
	 * query parameters
	 * @param query relational select for documents to be removed
	 * @param multi true if all matching documents should be updated
	 * 				false if only the first matching document should be updated
	 */
	public void remove(JsonObject query, boolean multi) {
		DBCursor target = this.find(query);
		if (multi) {
			while(target.hasNext()) {
				documents.remove(target.next());
			}
		} else {
			if (target.hasNext()) {
				documents.remove(target.next());
			}
		}
		writeToFile();
	}
	
	/**
	 * Returns the number of documents in this collection
	 */
	public long count() {
		return this.documents.size();
	}
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the ith document in the collection.
	 * Documents are separated by a line that contains only a single tab (\t)
	 * Use the parse function from the document class to create the document object
	 */
	public JsonObject getDocument(int i) {
		return this.documents.get(i);
	}
	
	/**
	 * Drops this collection, removing all of the documents it contains from the DB
	 */
	public void drop() {
		this.documents = new ArrayList<>();
		File fl = new File(this.path);
		fl.delete();
		
	}
	
}
