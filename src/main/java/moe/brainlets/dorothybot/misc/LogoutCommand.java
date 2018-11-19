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
		if(!event.getClient().getApplicationOwner().equals(event.getAuthor())) {
			event.getChannel().sendMessage("You do not have permission to use this command.");
			return;
		}
		
		event.getClient().logout();
	}

}
