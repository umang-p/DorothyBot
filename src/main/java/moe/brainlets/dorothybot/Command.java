package moe.brainlets.dorothybot;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/*
 * Interface used to create any text commands
 */
public interface Command {

	void run(MessageReceivedEvent event, List<String> arguments);
}
