package moe.brainlets.dorothybot.audio;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class SkipAudioCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		AudioTrack currentTrack = AudioManager.getInstance().playNextTrack();

		if (currentTrack != null)
			event.getChannel().sendMessage("Skipped: " + currentTrack.getInfo().title);
	}
}
