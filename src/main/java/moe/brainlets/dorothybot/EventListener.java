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
import moe.brainlets.dorothybot.misc.FetchGFLDataCommand;
import moe.brainlets.dorothybot.misc.HelpCommand;
import moe.brainlets.dorothybot.misc.LogoutCommand;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class EventListener {

	Map<String, Command> commandMap = new HashMap<String, Command>();

	public EventListener() {
		// general commands
		commandMap.put("shutdown", new LogoutCommand());
		commandMap.put("logout", new LogoutCommand());
		commandMap.put("fetchgfldata", new FetchGFLDataCommand());
		commandMap.put("help", new HelpCommand());

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

		// jp commands
		commandMap.put("jisho", new JishoCommand());
	}
	
	/*
	 * Called whenever the bot receives a message.
	 */
	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContent();

		if (!message.startsWith(BotUtils.CMD_PREFIX))
			return;

		String[] args = message.split(" ");

		List<String> arguments = new ArrayList<>(Arrays.asList(args));
		String command = arguments.remove(0); //remove the command itself from the full message
		command = command.substring(BotUtils.CMD_PREFIX.length());

		if (commandMap.containsKey(command))
			commandMap.get(command).run(event, arguments);
	}
}
