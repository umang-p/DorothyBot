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

public class JishoCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		if (arguments.isEmpty()) {
			event.getChannel().sendMessage(
					"Usage: /jisho <query>\nYou can look up english words, kana, kanji, etc.\nFor details, see: https://jisho.org/docs");
			return;
		}

		String query = "";
		for (String arg : arguments)
			query += arg + "%20";

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
		for (int i = 0; i < Math.min(3, allResults.size()); i++) {
			Element result = allResults.get(i);

			String furigana = result.getElementsByClass("furigana").get(0).text();
			message += furigana + "\n";
			String kanji = result.getElementsByClass("text").get(0).text();
			message += kanji + "\n";

			Elements allMeanings = result.getElementsByClass("meanings-wrapper").get(0).children();
			for (int j = 0; j < allMeanings.size(); j++) {
				if (allMeanings.get(j).className().equals("meaning-tags")) {
					message += "\n" + BotUtils.getMeaningMessage(allMeanings.get(j), allMeanings.get(j + 1));
					j++;
				} else {
					message += "\n" + BotUtils.getMeaningMessage(null, allMeanings.get(j));
				}
			}
			event.getChannel().sendMessage("```" + message + "```");
			message = "";
		}
	}
}
