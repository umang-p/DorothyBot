package moe.brainlets.dorothybot.girlsfrontline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
 * Command used to determine which equips can be obtained at what % rate from a given recipe,
 * or which equips can be crafted from a given timer. Data is sourced from GFDB.
 */
public class EquipsCommand implements Command {
	
	JSONArray equipData;
	
	public EquipsCommand() {
		StringBuilder json = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/equip_info.json"));

			String line;
			while ((line = br.readLine()) != null)
				json.append(line);

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		equipData = new JSONArray(json.toString());
	}

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		// equip recipes have min of 10 resources required, max of 300

		String usage = "Usage: "+BotUtils.CMD_PREFIX+"equips <time-or-recipe>\nTime format (# of minutes) example: "+BotUtils.CMD_PREFIX+"equips 58\nRecipe format example: "+BotUtils.CMD_PREFIX+"equips 50/250/150/50";
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

			JSONObject recipeData = BotUtils.getJSONObject("https://ipick.baka.pw:444/stats/equip/formula/" + mp + ":"
					+ ammo + ":" + mre + ":" + parts + ":" + tier);
			if (recipeData == null) {
				event.getChannel().sendMessage("Error getting data from GFDB, it may be down");
				return;
			}

			int total = recipeData.getInt("total");

			String message = "```" + "Recipe: " + mp + "/" + ammo + "/" + mre + "/" + parts;
			if (args.length == 5)
				message += " Tier: " + tier;
			message += "\nTotal recorded attempts: " + total + "\nChance\tEquip\n";

			JSONArray rollData = recipeData.getJSONArray("data");
			HashMap<Integer, Double> chanceData = new HashMap<Integer, Double>();
			int id;
			double chance;
			for (int i = 0; i < rollData.length(); i++) {
				id = rollData.getJSONObject(i).getInt("equip_id");
				chance = (double) rollData.getJSONObject(i).getInt("count") / total * 100;
				chanceData.put(id, chance);
			}

			List<String> dataLines = new ArrayList<String>();
			for (int i = 0; i < equipData.length(); i++) {
				JSONObject equip = equipData.getJSONObject(i);
				int equipID = equip.getInt("id");
				if (chanceData.containsKey(equipID)) {
					String line = String.format("%.2f", chanceData.get(equipID)) + "\t";
					switch (equip.getInt("type")) {
					case 1:
						line += equip.getInt("rank") + "\u2606" + " scope";
						break;
					case 2:
						line += equip.getInt("rank") + "\u2606" + " EOT";
						break;
					case 3:
						line += equip.getInt("rank") + "\u2606" + " red dot sight";
						break;
					case 4:
						line += equip.getInt("rank") + "\u2606" + " PEQ";
						break;
					case 5:
						line += equip.getInt("rank") + "\u2606" + " AP ammo";
						break;
					case 6:
						line += equip.getInt("rank") + "\u2606" + " HP ammo";
						break;
					case 7:
						line += equip.getInt("rank") + "\u2606";
						if(equip.getString("cn_name").contains("#"))
							line += " buckshot";
						else
							line += " slug";
						break;
					case 8:
						line += equip.getInt("rank") + "\u2606" + " HV ammo";
						break;
					case 10:
						line += equip.getInt("rank") + "\u2606";
						if(equip.getString("cn_name").contains("T"))
							line += " T-exo";
						else
							line += " X-exo";
						break;
					case 11:
						line += equip.getInt("rank") + "\u2606" + " armor";
						break;
					case 13:
						line += equip.getInt("rank") + "\u2606" + " suppressor";
						break;
					case 14:
						line += equip.getInt("rank") + "\u2606" + " ammo box";
						break;
					case 15:
						line += equip.getInt("rank") + "\u2606" + " cape";
						break;
					default:
						line += "unknown/special type";
					}
					line += "\n";
					dataLines.add(line);
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
				timer = Integer.parseInt(arguments.get(0));
			} catch (NumberFormatException e) {
				event.getChannel().sendMessage(usage);
				return;
			}
			int development_time = timer * 60;

			String message = "Possible equips:\n";
			for (int i = 0; i < equipData.length(); i++) {
				JSONObject equip = equipData.getJSONObject(i);
				if (equip.getInt("develop_duration") == development_time
						&& equip.getString("fit_guns").equals("")
						&& (!equip.getString("cn_name").contains("16Lab") && !equip.getString("cn_name").contains("16lab"))
						&& !equip.getString("company").equals("")
						&& equip.getInt("max_level") != 0) {
					switch (equip.getInt("type")) {
					case 1:
						message += equip.getInt("rank") + "\u2606" + " scope\n";
						break;
					case 2:
						message += equip.getInt("rank") + "\u2606" + " EOT\n";
						break;
					case 3:
						message += equip.getInt("rank") + "\u2606" + " red dot sight\n";
						break;
					case 4:
						message += equip.getInt("rank") + "\u2606" + " PEQ\n";
						break;
					case 5:
						message += equip.getInt("rank") + "\u2606" + " AP ammo\n";
						break;
					case 6:
						message += equip.getInt("rank") + "\u2606" + " HP ammo\n";
						break;
					case 7:
						message += equip.getInt("rank") + "\u2606";
						if(equip.getString("cn_name").contains("#"))
							message += " buckshot\n";
						else
							message += " slug\n";
						break;
					case 8:
						message += equip.getInt("rank") + "\u2606" + " HV ammo\n";
						break;
					case 10:
						message += equip.getInt("rank") + "\u2606";
						if(equip.getString("cn_name").contains("T"))
							message += " T-exo\n";
						else
							message += " X-exo\n";
						break;
					case 11:
						message += equip.getInt("rank") + "\u2606" + " armor\n";
						break;
					case 13:
						message += equip.getInt("rank") + "\u2606" + " suppressor\n";
						break;
					case 14:
						message += equip.getInt("rank") + "\u2606" + " ammo box\n";
						break;
					case 15:
						message += equip.getInt("rank") + "\u2606" + " cape\n";
						break;
					default:
						message += "unknown/special type\n";
					}
				}
			}
			event.getChannel().sendMessage(message);
			// rank=rarity, type: 1=scope, 2=eot/holosight, 3=red dot sight, 4=peq/night
			// vision, 5=AP ammo,
			// 6=HP ammo, 7=Shotgun ammo, 8=HV ammo, 9=uknown, 10=exo,11=armor
			// 13=suppressor, 14=ammo box, 15=cape,
		}
	}

}
