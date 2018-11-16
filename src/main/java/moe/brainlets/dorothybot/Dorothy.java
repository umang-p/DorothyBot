package moe.brainlets.dorothybot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class Dorothy {


	public static void main(String[] args) {
		IDiscordClient bot = new ClientBuilder().withToken(TOKEN).build();
		EventListener listener = new EventListener();
		bot.getDispatcher().registerListener(listener);
		bot.login();
	}
}
/*
 * gfdb endpoints --------------
 * 
 * https://ipick.baka.pw:444/
 * 
 * https://ipick.baka.pw:444/data/json/gun_info
 * https://ipick.baka.pw:444/data/json/gun_info_simple
 * https://ipick.baka.pw:444/data/json/equip_info
 * https://ipick.baka.pw:444/data/json/fairy_info
 * https://ipick.baka.pw:444/stats/tdoll/id/157
 * https://ipick.baka.pw:444/stats/equip/id/4
 * https://ipick.baka.pw:444/stats/fairy/id/4
 * https://ipick.baka.pw:444/stats/tdoll/formula/130:130:130:130:1
 * https://ipick.baka.pw:444/stats/equip/formula/10:10:10:10:0
 * https://ipick.baka.pw:444/stats/fairy/formula/10:10:10:10:0
 * https://ipick.baka.pw:444/stats/info
 * 
 * 
 * data/json/gun_info_simple
 *  type(1=hg, 2=smg, 3=rf, 4=ar,5=mg, 6=sg),
 *  rank(2=2*, 3=3*, 4=4*, 5=5*), code(name?), develop_duration(minutes * 60),
 * 
 */