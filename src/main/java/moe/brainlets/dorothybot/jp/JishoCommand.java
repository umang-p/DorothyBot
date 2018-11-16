package moe.brainlets.dorothybot.jp;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import moe.brainlets.dorothybot.BotUtils;
import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/*
 * Command used to assist in Kanji learning by using a japanese dictionary service. 
 * Contacts jisho.org with a search query and displays 
 * at most MAX_NUM_RESULTS results.
 */
public class JishoCommand implements Command {
	
	private final int MAX_NUM_RESULTS = 3;
	
	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		if (arguments.isEmpty()) {
			event.getChannel().sendMessage("Usage: "+BotUtils.CMD_PREFIX+"jisho <query>\nYou can look up "
					+ "english words, kana, kanji, etc.\nFor details, see: https://jisho.org/docs");
			return;
		}

		String query = "";
		for (String arg : arguments)
			query += arg + "%20"; //add spaces back as %20 for url
		
		Document doc;
		try {
			doc = Jsoup.connect("https://jisho.org/search/" + query).get();
		} catch (IOException e1) {
			e1.printStackTrace();
			event.getChannel().sendMessage("Error connecting to Jisho.org, the site may be down");
			return;
		}
		
		Element div = doc.getElementById("no-matches");
		if (div != null) {
			event.getChannel().sendMessage("No results");
			return;
		}

		String message = "";
		Elements allResults = doc.getElementsByClass("concept_light clearfix");
		for (int i = 0; i < Math.min(MAX_NUM_RESULTS, allResults.size()); i++) {
			Element result = allResults.get(i);

			String furigana = result.getElementsByClass("furigana").get(0).text();
			message += furigana + "\n";
			String kanji = result.getElementsByClass("text").get(0).text();
			message += kanji + "\n";
			
			//each search result can have multiple meanings
			Elements allMeanings = result.getElementsByClass("meanings-wrapper").get(0).children();
			for (int j = 0; j < allMeanings.size(); j++) {
				if (allMeanings.get(j).className().equals("meaning-tags")) {
					message += "\n" + getMeaningMessage(allMeanings.get(j), allMeanings.get(j + 1));
					j++;
				} else {
					message += "\n" + getMeaningMessage(null, allMeanings.get(j));
				}
			}
			event.getChannel().sendMessage("```" + message + "```");
			message = "";
		}
	}
	
	/*
	 * Helper function to extract meaning data and format appropriately for discord messages.
	 */
	private String getMeaningMessage(Element tags, Element meaningWrapper) {
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
					furigana += "  "; //padding for proper alignment with kanji

				kanji += unlinked.get(0).text();
			} else {
				furigana += furi.get(0).text();
				kanji += unlinked.get(0).text();
				
				for (int i = 0; i < Math.abs(unlinked.get(0).text().length() - furi.get(0).text().length()); i++)
					kanji += "  "; //padding for proper alignment with furigana
			}
		}
		message += furigana + "\n";
		message += kanji + "\n";

		Element english = meaningWrapper.getElementsByClass("english").get(0);
		message += english.ownText() + "\n";

		return message;
	}
}
