package moe.brainlets.dorothybot.audio;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/*
 * Command used to display the title of the currently playing audio. Does nothing if no audio is playing.
 */
public class NowPlayingCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		AudioTrack currentTrack = AudioManager.getInstance().getAudioPlayer().getPlayingTrack();

		if (currentTrack != null)
			event.getChannel().sendMessage("Now Playing: " + currentTrack.getInfo().title);
	}

}
