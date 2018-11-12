package moe.brainlets.dorothybot.audio;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class RemoveAudioCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		if (arguments.size() != 1) {
			event.getChannel().sendMessage("Usage: /remove <number>");
			return;
		}

		int index = 0;
		try {
			index = Integer.parseInt(arguments.get(0)) - 1;
		} catch (NumberFormatException e) {
			event.getChannel().sendMessage("Usage: /remove <number>");
			return;
		}

		AudioTrack removedTrack = AudioManager.getInstance().removeTrack(index);

		if (removedTrack != null)
			event.getChannel().sendMessage("Removed: " + removedTrack.getInfo().title);
	}

}
