package moe.brainlets.dorothybot.misc;

import java.util.List;

import moe.brainlets.dorothybot.BotUtils;
import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class HelpCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		if(arguments.isEmpty()) {
			event.getChannel().sendMessage("For a list of commands, try "+BotUtils.CMD_PREFIX+"help <category>\nCategories:\naudio\ngfl\njp");
			return;
		}
		
		String audioHelp = "[nowplaying][np]: Show title of the currently playing track\n"
				+ "[pause]: Pause audio\n"
				+ "[resume]: Resume playing audio\n"
				+ "[volume]: Change volume (value can be between 1 and 1000)\n"
				+ "[skip]: Skip the currently playing track\n"
				+ "[playlist][pl][queue]: Show the tracks that are in the queue\n"
				+ "[remove][rm]: Remove a specific track from the queue\n"
				+ "[stop]: Clear queue, stop playing audio, and have bot leave voice channel\n"
				+ "[play]: Start playing a track or add it to the queue if something is playing already. If the link provided is a playlist, only uses the selected track\n"
				+ "[playall]: Same as [play] but if given a playlist link, all tracks in the playlist will be added to the queue\n";
		
		String gflHelp = "[dolls]: Show which Tdolls can be obtained from a given recipe or timer\n"
				+ "[equips]: Show which equips can be obtained from a given recipe or timer\n"
				+ "[recipes]: Show which recipes can be used to obtain a specific Tdoll\n";
		
		String jpHelp = "[jisho]: Search a japanese dictionary\n";
		
		String category = arguments.get(0);
		switch (category.toLowerCase()) {
		case "audio": event.getChannel().sendMessage(audioHelp); break;
		case "gfl": event.getChannel().sendMessage(gflHelp); break;
		case "jp": event.getChannel().sendMessage(jpHelp); break;
		default: event.getChannel().sendMessage("Category does not exist");
		}
	}

}
