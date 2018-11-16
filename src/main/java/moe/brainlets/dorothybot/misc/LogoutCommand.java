package moe.brainlets.dorothybot.misc;

import java.util.List;

import moe.brainlets.dorothybot.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/*
 * Logout from all shards.
 */
public class LogoutCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		event.getClient().logout();
	}

}
