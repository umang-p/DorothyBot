package moe.brainlets.dorothybot.girlsfrontline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import moe.brainlets.dorothybot.BotUtils;
import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

/*
 * Command used to find recipes, ordered by success chance, to craft a given Tdoll. Data is sourced from GFDB.
 */
public class RecipesCommand implements Command {
	
	JSONArray gunData;
	
	public RecipesCommand() {
		StringBuilder json = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/gun_info.json"));

			String line;
			while ((line = br.readLine()) != null)
				json.append(line);

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		gunData = new JSONArray(json.toString());
	}

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		String usage = "Usage: "+BotUtils.CMD_PREFIX+"recipes <dollname>\n";
		if (arguments.isEmpty()) {
			event.getChannel().sendMessage(usage);
			return;
		}
		
		String dollname = "";
		for(String arg: arguments) {
			dollname += arg + " ";
		}
		dollname = dollname.trim();
		
		String query;
		if(BotUtils.aliases.containsKey(dollname.toLowerCase()))
			query = BotUtils.aliases.get(dollname.toLowerCase());
		else
			query = dollname;
		
		int gunID = -1;
		String en_name = "";
		for (int i = 0; i < gunData.length(); i++) {
			JSONObject gun = gunData.getJSONObject(i);
			en_name = gun.getString("en_name");
			if (query.equalsIgnoreCase(en_name)) {
				gunID = gun.getInt("id");
				break;
			}
		}		
		
		if(gunID == -1) {
			event.getChannel().sendMessage("Could not find doll. Check name and try again.");
			return;
		}
		
		JSONArray recipeArray = BotUtils.getJSONArray("https://ipick.baka.pw:444/stats/tdoll/id/" + gunID);
		if (recipeArray == null) {
			event.getChannel().sendMessage("Error getting data from GFDB, it may be down");
			return;
		}
		
		List<String> recipeData = new ArrayList<String>();
		for(int i = 0; i < recipeArray.length(); i++) {
			JSONObject data = recipeArray.getJSONObject(i);
			JSONObject formula = data.getJSONObject("formula");
			int mp = formula.getInt("mp");
			int ammo = formula.getInt("ammo");
			int mre = formula.getInt("mre");
			int parts = formula.getInt("part");
			int tier;
			try {
				tier = formula.getInt("input_level");
			} catch(JSONException e) {
				continue;
			}
			int successes = data.getInt("count");
			int attempts = data.getInt("total");
			
			double chance = (double) successes / attempts * 100;
			String recipe = mp + "/" + ammo + "/" + mre + "/" + parts;
			if(tier != 0)
				recipe += " Tier: " + tier;
			
			recipeData.add(String.format("%.2f", chance) + "\t" + String.format("%,9d", attempts) + "\t\t" + recipe);
		}
		
		Collections.sort(recipeData);
		
		List<String> messages = new ArrayList<String>();
		int j = 0;
		String message = "";
		message = "```" + "Doll: " + en_name + "\n";
		message += "Chance\tAttempts\t\tRecipe\n";
		for(int i = 0; i < recipeData.size(); i++) {
			
			message += recipeData.get(i) + "\n";
			j++;
			if(j == 50) {
				messages.add(message + "```");
				j = 0;
				message = "```" + "Doll: " + en_name + "\n";
				message += "Chance\tAttempts\t\tRecipe\n";
			}
		}
		
		messages.add(message + "```");
		
		RequestBuffer.request(() -> {
			for(String m: messages) {
				event.getChannel().sendMessage(m);
			}
		});
	}

}
