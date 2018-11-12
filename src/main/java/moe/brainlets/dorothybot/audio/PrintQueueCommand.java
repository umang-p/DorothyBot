package moe.brainlets.dorothybot.audio;

import java.util.List;

import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class PrintQueueCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		List<String> titles = AudioManager.getInstance().getQueuedTracks();

		if (titles.isEmpty()) {
			event.getChannel().sendMessage("Nothing in queue");
			return;
		}

		// send titles/numbers to channel, 10 at a time
		String message = "";
		for (int i = 0; i < titles.size(); i++) {
			message += (i + 1) + ": " + titles.get(i) + "\n";
			if (i % 10 == 9) {
				event.getChannel().sendMessage(message);
				message = "";
			}
		}
		if ((titles.size() - 1) % 10 != 9)
			event.getChannel().sendMessage(message);
	}

}
