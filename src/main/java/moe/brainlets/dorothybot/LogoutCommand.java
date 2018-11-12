package moe.brainlets.dorothybot;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class LogoutCommand implements Command {

	@Override
	public void run(MessageReceivedEvent event, List<String> arguments) {
		event.getClient().logout();
	}

}
