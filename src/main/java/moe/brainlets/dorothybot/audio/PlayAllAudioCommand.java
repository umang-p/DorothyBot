package moe.brainlets.dorothybot.audio;

import java.util.List;

import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class PlayAllAudioCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		if (arguments.isEmpty()) {
			event.getChannel().sendMessage("Usage: /playall <playlist-link>");
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

		if (event.getGuild().getAudioManager().getAudioProvider() != manager.getAudioProvider()) {
			event.getGuild().getAudioManager().setAudioProvider(manager.getAudioProvider());
			manager.resume();
		}

		manager.getAudioPlayerManager().loadItemOrdered(manager.getAudioPlayer(), audioIdentifier, new LoadResultHandler(event, true));
	}

}
