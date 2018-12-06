package moe.brainlets.dorothybot.girlsfrontline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import moe.brainlets.dorothybot.BotUtils;
import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class DollTimerCommand implements Command {
	
	JSONArray gunData;
	
	public DollTimerCommand() {
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
		String usage = "Usage: "+BotUtils.CMD_PREFIX+"timer <dollname>\n";
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
		
		int developTime = -1;
		String en_name = "";
		for (int i = 0; i < gunData.length(); i++) {
			JSONObject gun = gunData.getJSONObject(i);
			en_name = gun.getString("en_name");
			if (query.equalsIgnoreCase(en_name)) {
				developTime = gun.getInt("develop_duration");
				break;
			}
		}
		
		if(developTime == -1) {
			event.getChannel().sendMessage("Could not find doll. Check name and try again.");
			return;
		}
		
		int totalMinutes = developTime / 60;
		int hours = totalMinutes / 60;
		int minutes = totalMinutes % 60;
		String message = "Timer for " + en_name + " is " + String.format("%02d:%02d", hours, minutes);
		event.getChannel().sendMessage(message);
	}

}
