package moe.brainlets.dorothybot.gfl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import moe.brainlets.dorothybot.BotUtils;
import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/*
 * Command used to determine which Tdolls can be obtained at what % rate from a given recipe,
 * or which Tdolls can be crafted from a given timer. Data is sourced from GFDB.
 */
public class DollsCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		// non heavy doll recipes have min of 30 resources required, max of 999

		String usage = "Usage: "+BotUtils.CMD_PREFIX+"dolls <time-or-recipe>\nTime format example: \""+BotUtils.CMD_PREFIX+"dolls 00:50\"\nRecipe format example: \""+BotUtils.CMD_PREFIX+"dolls 130/130/130/130\" or \""+BotUtils.CMD_PREFIX+"dolls 6000/2000/6000/4000/1\" for heavy production where you add /1, /2, or /3 at the end to indicate production tier";
		if (arguments.size() != 1) {
			event.getChannel().sendMessage(usage);
			return;
		}

		if (arguments.get(0).contains("/")) {
			String[] args = arguments.get(0).split("/");
			if (args.length != 4 && args.length != 5) {
				event.getChannel().sendMessage(usage);
				return;
			}

			int mp, ammo, mre, parts, tier;
			try {
				mp = Integer.parseInt(args[0]);
				ammo = Integer.parseInt(args[1]);
				mre = Integer.parseInt(args[2]);
				parts = Integer.parseInt(args[3]);
				tier = args.length == 4 ? 0 : Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				event.getChannel().sendMessage(usage);
				return;
			}

			JSONObject recipeData = BotUtils.getJSONObject("https://ipick.baka.pw:444/stats/tdoll/formula/" + mp + ":"
					+ ammo + ":" + mre + ":" + parts + ":" + tier);
			if (recipeData == null) {
				event.getChannel().sendMessage("Error getting data from GFDB, it may be down");
				return;
			}

			int total = recipeData.getInt("total");

			String message = "```" + "Recipe: " + mp + "/" + ammo + "/" + mre + "/" + parts;
			if (args.length == 5)
				message += " Tier: " + tier;
			message += "\nTotal recorded attempts: " + total + "\nChance\tDoll\n";

			JSONArray rollData = recipeData.getJSONArray("data");
			HashMap<Integer, Double> chanceData = new HashMap<Integer, Double>();
			int id;
			double chance;
			for (int i = 0; i < rollData.length(); i++) {
				id = rollData.getJSONObject(i).getInt("gun_id");
				chance = (double) rollData.getJSONObject(i).getInt("count") / total * 100;
				chanceData.put(id, chance);
			}

			JSONArray gunData = BotUtils.getJSONArray("https://ipick.baka.pw:444/data/json/gun_info");
			if (gunData == null) {
				event.getChannel().sendMessage("Error getting data from GFDB, it may be down");
				return;
			}

			List<String> dataLines = new ArrayList<String>();
			for (int i = 0; i < gunData.length(); i++) {
				int gunID = gunData.getJSONObject(i).getInt("id");
				if (chanceData.containsKey(gunID)) {
					dataLines.add(String.format("%.2f", chanceData.get(gunID)) + "\t"
							+ gunData.getJSONObject(i).getString("en_name") + "\n");
				}
			}
			Collections.sort(dataLines);
			for (String line : dataLines) {
				message += line;
			}
			event.getChannel().sendMessage(message + "```");

		} else {
			int timer;
			try {
				String[] args = arguments.get(0).split(":");
				if (args.length != 2) {
					event.getChannel().sendMessage(usage);
					return;
				}
				timer = Integer.parseInt(args[0]) * 60 + Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				event.getChannel().sendMessage(usage);
				return;
			}
			int development_time = timer * 60;

			JSONArray jsonArray = BotUtils.getJSONArray("https://ipick.baka.pw:444/data/json/gun_info");
			if (jsonArray == null) {
				event.getChannel().sendMessage("Error getting data from GFDB, it may be down");
				return;
			}

			String message = "Possible dolls:\n";
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject gun = jsonArray.getJSONObject(i);
				if (gun.getInt("develop_duration") == development_time && gun.getInt("id") < 1000) {
					message += gun.getString("en_name") + "\n";
				}
			}
			event.getChannel().sendMessage(message);
		}
	}

}
