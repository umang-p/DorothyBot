package moe.brainlets.dorothybot.audio;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class PlayAudioCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		if (arguments.isEmpty()) {
			event.getChannel().sendMessage("Usage: /play <link>");
			return;
		}

		String audioIdentifier = "";
		for (String arg : arguments)
			audioIdentifier += arg;

		IVoiceChannel voiceChannel = event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel();
		if (voiceChannel == null) {
			event.getChannel().sendMessage("You must be in a voice channel");
			return;
		}
		if (!voiceChannel.isConnected())
			voiceChannel.join();

		AudioManager manager = AudioManager.getInstance();

		// only the first time this command runs
		if (event.getGuild().getAudioManager().getAudioProvider() != manager.getAudioProvider()) {
			event.getGuild().getAudioManager().setAudioProvider(manager.getAudioProvider());
			manager.resume();
		}

		manager.getAudioPlayerManager().loadItemOrdered(manager.getAudioPlayer(), audioIdentifier,
				new AudioLoadResultHandler() {

					@Override
					public void trackLoaded(AudioTrack track) {
						boolean isPlaying = manager.queueAndPlay(track);
						if (isPlaying)
							event.getChannel().sendMessage("Now Playing: " + track.getInfo().title);
						else
							event.getChannel().sendMessage("Added to queue: " + track.getInfo().title);
					}

					@Override
					public void playlistLoaded(AudioPlaylist playlist) {
						AudioTrack track = playlist.getSelectedTrack();
						boolean isPlaying = manager.queueAndPlay(track);
						if (isPlaying)
							event.getChannel().sendMessage("Now Playing: " + track.getInfo().title);
						else
							event.getChannel().sendMessage("Added to queue: " + track.getInfo().title);
					}

					@Override
					public void noMatches() {
						event.getChannel().sendMessage("Could not find audio: Probably blocked in this country");
					}

					@Override
					public void loadFailed(FriendlyException exception) {
						event.getChannel().sendMessage("Could not load audio: " + exception.getMessage());
					}

				});
	}

}
