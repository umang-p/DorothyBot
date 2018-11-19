package moe.brainlets.dorothybot.audio;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import moe.brainlets.dorothybot.BotUtils;
import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/*
 * Command used to remove a track from the queue. 
 */
public class RemoveAudioCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		if (arguments.size() != 1) {
			event.getChannel().sendMessage("Usage: "+BotUtils.CMD_PREFIX+"remove <number-of-track-to-remove>\nUse the playlist command to find track numbers");
			return;
		}

		int index = 0;
		try {
			index = Integer.parseInt(arguments.get(0)) - 1;
		} catch (NumberFormatException e) {
			event.getChannel().sendMessage("Usage: "+BotUtils.CMD_PREFIX+"remove <number>");
			return;
		}

		AudioTrack removedTrack = AudioManager.getInstance().removeTrack(index);

		if (removedTrack != null)
			event.getChannel().sendMessage("Removed: " + removedTrack.getInfo().title);
	}

}
