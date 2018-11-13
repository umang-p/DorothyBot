package moe.brainlets.dorothybot.audio;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

public class LoadResultHandler implements AudioLoadResultHandler {
	
	private MessageReceivedEvent event;
	private boolean playall;
	
	public LoadResultHandler(MessageReceivedEvent event, boolean playall) {
		this.event = event;
		this.playall = playall;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		boolean isPlaying = AudioManager.getInstance().queueAndPlay(track);
		if (isPlaying)
			event.getChannel().sendMessage("Now Playing: " + track.getInfo().title);
		else
			event.getChannel().sendMessage("Added to queue: " + track.getInfo().title);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		AudioManager manager = AudioManager.getInstance();
		boolean isPlaying;
		
		if(!playall) {
			AudioTrack track = playlist.getSelectedTrack();
			
			if(track == null)
				return;
			
			isPlaying = manager.queueAndPlay(track);
			if (isPlaying)
				event.getChannel().sendMessage("Now Playing: " + track.getInfo().title);
			else
				event.getChannel().sendMessage("Added to queue: " + track.getInfo().title);
			
		} else {
			List<String> messages = new ArrayList<String>();
			String message = "";
			List<AudioTrack> tracks = playlist.getTracks();
			for(int i = 0; i < tracks.size(); i++) {
				AudioTrack track = tracks.get(i);
				
				isPlaying = manager.queueAndPlay(track);
				
				if(isPlaying)
					event.getChannel().sendMessage("Now Playing: " + track.getInfo().title);
				else
					message += "Added to queue: " + track.getInfo().title + "\n";
				
				if (i % 50 == 49) {
					messages.add(message);
					message = "";
				}
			}
			if ((tracks.size() - 1) % 50 != 49)
				messages.add(message);
			
			RequestBuffer.request(() -> {
				for(String m: messages) {
					event.getChannel().sendMessage(m);
				}
			});
		}
	}

	@Override
	public void noMatches() {
		event.getChannel().sendMessage("Could not find audio: Probably blocked in this country");		
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		event.getChannel().sendMessage("Could not load audio: Probably blocked in this country, " + exception.getMessage());		
	}

}
