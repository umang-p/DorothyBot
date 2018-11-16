package moe.brainlets.dorothybot.audio;

import java.util.List;

import moe.brainlets.dorothybot.BotUtils;
import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class VolumeCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		if (arguments.size() != 1) {
			event.getChannel()
					.sendMessage("Usage: "+BotUtils.CMD_PREFIX+"volume <number>" + "\nVolume can be between 0 and 1000 (default: 100)");
			return;
		}

		int volume = 0;
		try {
			volume = Integer.parseInt(arguments.get(0));
		} catch (NumberFormatException e) {
			event.getChannel()
					.sendMessage("Usage: "+BotUtils.CMD_PREFIX+"volume <number>" + "\nVolume can be between 0 and 1000 (default: 100)");
			return;
		}

		AudioManager.getInstance().changeVolume(volume);
	}

}
