package hw5;
/*
 * Student 1 name: Fa Long (id:462512)
 * Student 2 name: Zhuo Wei (id: 462473)
 * Date: Dec 6th, 2019
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gson.*;


public class Document {
	
	/**
	 * Parses the given json string and returns a JsonObject
	 * This method should be used to convert text data from
	 * a file into an object that can be manipulated.
	 */
	public static JsonObject parse(String json) {
		//Gson gson = new Gson();
		JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
		return jsonObject;
	}
	
	/**
	 * Takes the given object and converts it into a
	 * properly formatted json string. This method should
	 * be used to convert JsonObjects to strings
	 * when writing data to disk.
	 */
	public static String toJsonString(JsonObject json) {
		String jsonString = json.toString();
		return jsonString;
	}
}
