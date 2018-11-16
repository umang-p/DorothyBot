package moe.brainlets.dorothybot.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

/*
 * Class used to manage the audio queue and the audio player itself. Self-explanatory methods.
 */
public class AudioManager {

	private static AudioManager audioScheduler = null;
	private AudioPlayerManager audioPlayerManager;
	private AudioPlayer player;
	private AudioProvider audioProvider;

	private List<AudioTrack> queue;
	private boolean isPaused;

	private AudioManager() {
		audioPlayerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);

		player = audioPlayerManager.createPlayer();
		player.addListener(new AudioEventAdapter() {
			public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
				if (endReason.mayStartNext)
					playNextTrack();
			}

			public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
				if (exception.severity == FriendlyException.Severity.COMMON)
					playNextTrack();
			}
		});

//		methods from AudioEventAdapter, implement as needed
//		public void onPlayerPause(AudioPlayer player);
//		public void onPlayerResume(AudioPlayer player);
//		public void onTrackStart(AudioPlayer player, AudioTrack track);
//		public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs);

		audioProvider = new AudioProvider(player);

		queue = Collections.synchronizedList(new LinkedList<>());
		isPaused = false;
	}

	public synchronized boolean queueAndPlay(AudioTrack track) {
		boolean isPlaying = player.startTrack(track, true);

		if (!isPlaying)
			queue.add(track);

		return isPlaying;
	}

	public synchronized AudioTrack playNextTrack() {
		AudioTrack currentTrack = player.getPlayingTrack();
		AudioTrack nextTrack = queue.isEmpty() ? null : queue.remove(0);

		if (isPaused)
			this.resume();

		player.startTrack(nextTrack, false);

		return currentTrack;
	}

	public synchronized AudioTrack removeTrack(int index) {
		AudioTrack track = null;

		try {
			track = queue.remove(index);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return track;
	}

	public synchronized void stop() {
		player.stopTrack();
	}

	public synchronized void pause() {
		player.setPaused(true);
		isPaused = true;
	}

	public synchronized void resume() {
		player.setPaused(false);
		isPaused = false;
	}

	public synchronized void changeVolume(int volume) {
		player.setVolume(volume);
	}

	public synchronized void clearQueue() {
		queue.clear();
	}

	public List<String> getQueuedTracks() {
		List<String> titles = new ArrayList<>();
		synchronized (queue) {
			Iterator<AudioTrack> tracks = queue.iterator();
			while (tracks.hasNext()) {
				titles.add(tracks.next().getInfo().title);
			}
		}

		return titles;
	}

	public AudioPlayer getAudioPlayer() {
		return player;
	}

	public AudioPlayerManager getAudioPlayerManager() {
		return audioPlayerManager;
	}

	public AudioProvider getAudioProvider() {
		return audioProvider;
	}

	public static AudioManager getInstance() {
		if (audioScheduler == null)
			audioScheduler = new AudioManager();

		return audioScheduler;
	}
}
