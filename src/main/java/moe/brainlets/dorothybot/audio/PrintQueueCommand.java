package moe.brainlets.dorothybot.audio;

import java.util.ArrayList;
import java.util.List;

import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

/*
 * Command used to display a numbered list of the titles of the tracks currently in the queue.
 * Each number corresponds to 1-indexed position in the queue, 
 * used by users to remove specific tracks from the queue.
 */
public class PrintQueueCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		List<String> titles = AudioManager.getInstance().getQueuedTracks();

		if (titles.isEmpty()) {
			event.getChannel().sendMessage("Nothing in queue");
			return;
		}
		
		List<String> messages = new ArrayList<String>();
		String message = "";
		for (int i = 0; i < titles.size(); i++) {
			message += (i + 1) + ": " + titles.get(i) + "\n";
			if (i % 50 == 49) {
				messages.add(message);
				message = "";
			}
		}
		if ((titles.size() - 1) % 50 != 49)
			messages.add(message);
		
		RequestBuffer.request(() -> {
			for(String m: messages) {
				event.getChannel().sendMessage(m);
			}
		});
	}

}
