package moe.brainlets.dorothybot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * Class containing utility functions that do not rely on state.
 */
public class BotUtils {
	
	public static final String CMD_PREFIX = "/";
	
	/*
	 * Obtains a json string from a given url. 
	 * Returns a JSONArray if nothing goes wrong, null otherwise.
	 */
	public static JSONArray getJSONArray(String url) {
		StringBuilder response = new StringBuilder();
		try {
			URLConnection connection = new URL(url).openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));

			String line;
			while ((line = br.readLine()) != null)
				response.append(line);

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		JSONArray json;
		try {
			json = new JSONArray(response.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}

	/*
	 * Obtains a json string from a given url. 
	 * Returns a JSONObject if nothing goes wrong, null otherwise.
	 */
	public static JSONObject getJSONObject(String url) {
		StringBuilder response = new StringBuilder();
		try {
			URLConnection connection = new URL(url).openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));

			String line;
			while ((line = br.readLine()) != null)
				response.append(line);

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		JSONObject json;
		try {
			json = new JSONObject(response.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}
}
