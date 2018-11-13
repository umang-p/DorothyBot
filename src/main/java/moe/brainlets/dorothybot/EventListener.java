package moe.brainlets.dorothybot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.brainlets.dorothybot.audio.*;
import moe.brainlets.dorothybot.gfl.DollsCommand;
import moe.brainlets.dorothybot.gfl.EquipsCommand;
import moe.brainlets.dorothybot.gfl.RecipesCommand;
import moe.brainlets.dorothybot.jp.JishoCommand;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class EventListener {

	Map<String, Command> commandMap = new HashMap<String, Command>();

	public EventListener() {
		// general commands
		commandMap.put("shutdown", new LogoutCommand());
		commandMap.put("logout", new LogoutCommand());

		// audio commands
		commandMap.put("play", new PlayAudioCommand());
		commandMap.put("playall", new PlayAllAudioCommand());
		commandMap.put("stop", new StopAudioCommand());
		commandMap.put("skip", new SkipAudioCommand());
		commandMap.put("remove", new RemoveAudioCommand());
		commandMap.put("rm", new RemoveAudioCommand());
		commandMap.put("playlist", new PrintQueueCommand());
		commandMap.put("pl", new PrintQueueCommand());
		commandMap.put("queue", new PrintQueueCommand());
		commandMap.put("nowplaying", new NowPlayingCommand());
		commandMap.put("np", new NowPlayingCommand());
		commandMap.put("pause", new PauseAudioCommand());
		commandMap.put("resume", new ResumeAudioCommand());
		commandMap.put("volume", new VolumeCommand());

		// gfl commands
		commandMap.put("dolls", new DollsCommand());
		commandMap.put("equips", new EquipsCommand());
		commandMap.put("recipes", new RecipesCommand());

		// jp
		commandMap.put("jisho", new JishoCommand());
	}

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContent();

		if (!message.startsWith(Dorothy.CMD_PREFIX))
			return;

		String[] args = message.split(" ");

		String command = args[0].substring(Dorothy.CMD_PREFIX.length());

		List<String> arguments = new ArrayList<>(Arrays.asList(args));
		arguments.remove(0);

		if (commandMap.containsKey(command))
			commandMap.get(args[0].substring(Dorothy.CMD_PREFIX.length())).run(event, arguments);
	}
}
