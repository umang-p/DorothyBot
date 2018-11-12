package moe.brainlets.dorothybot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BotUtils {
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

		return new JSONArray(response.toString());
	}

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

		return new JSONObject(response.toString());
	}

	public static String getMeaningMessage(Element tags, Element meaningWrapper) {
		String message = "";

		if (tags != null)
			message += tags.ownText() + "\n";

		Elements divider = meaningWrapper.getElementsByClass("meaning-definition-section_divider");
		if (!divider.isEmpty())
			message += divider.get(0).ownText();

		Elements meaning = meaningWrapper.getElementsByClass("meaning-meaning");
		if (!meaning.isEmpty())
			message += meaning.get(0).ownText() + "\n";

		if (meaningWrapper.getElementsByClass("sentence").isEmpty())
			return message;

		Elements japSection = meaningWrapper.getElementsByClass("japanese japanese_gothic clearfix");
		Elements japWords = japSection.get(0).children();
		String furigana = "";
		String kanji = "";
		for (Element word : japWords) {
			if (word.className().equals("english"))
				break;

			Elements furi = word.getElementsByClass("furigana");
			Elements unlinked = word.getElementsByClass("unlinked");

			if (furi.isEmpty()) {
				for (int i = 0; i < unlinked.get(0).text().length(); i++)
					furigana += "  ";

				kanji += unlinked.get(0).text();
			} else {
				furigana += furi.get(0).text();
				kanji += unlinked.get(0).text();
				for (int i = 0; i < Math.abs(unlinked.get(0).text().length() - furi.get(0).text().length()); i++)
					kanji += "  ";
			}
		}
		message += furigana + "\n";
		message += kanji + "\n";

		Element english = meaningWrapper.getElementsByClass("english").get(0);
		message += english.ownText() + "\n";

		return message;
	}
}
