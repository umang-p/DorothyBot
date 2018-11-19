package moe.brainlets.dorothybot.misc;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.json.JSONArray;

import moe.brainlets.dorothybot.BotUtils;
import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/*
 * Command used to fetch the latest data from GFDB and update the local JSON files.
 */
public class FetchGFLDataCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		//only allow bot owner to use this command
		if(!event.getClient().getApplicationOwner().equals(event.getAuthor())) {
			event.getChannel().sendMessage("You do not have permission to use this command.");
			return;
		}
		
		JSONArray gunData = BotUtils.getJSONArray("https://ipick.baka.pw:444/data/json/gun_info");
		if (gunData == null) {
			event.getChannel().sendMessage("Error getting gun_info from GFDB, it may be down");
			return;
		}
		
		JSONArray equipData = BotUtils.getJSONArray("https://ipick.baka.pw:444/data/json/equip_info");
		if (equipData == null) {
			event.getChannel().sendMessage("Error getting equip_info from GFDB, it may be down");
			return;
		}
		
		try {
			String data = gunData.toString(2);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")+"/gun_info.json"), StandardCharsets.UTF_8));
			bw.write(data);
			bw.close();
		} catch (IOException e) {
			event.getChannel().sendMessage("Error writing gun_info.json");
			e.printStackTrace();
			return;
		}
		
		try {
			String data = equipData.toString(2);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")+"/equip_info.json"), StandardCharsets.UTF_8));
			bw.write(data);
			bw.close();
		} catch (IOException e) {
			event.getChannel().sendMessage("Error writing equip_info.json");
			e.printStackTrace();
			return;
		}
		
		event.getChannel().sendMessage("Successfully updated gun_info and equip_info");
	}

}
