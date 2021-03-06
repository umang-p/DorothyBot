package moe.brainlets.dorothybot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * Class containing utility functions that do not rely on state.
 */
public class BotUtils {
	
	public static final String CMD_PREFIX = "/";
	public static Map<String, String> aliases;
	
	static {
		aliases = new HashMap<String,String>();
		aliases.put("cola", "m1873");
		aliases.put("bepis", "m1873");
		aliases.put("colt", "m1873");
		aliases.put("colt revolver", "m1873");
		
		aliases.put("babushka", "m1895");
		aliases.put("nagant", "m1895");
		aliases.put("nagant revolver", "m1895");
		
		aliases.put("tokarev", "tt33");
		
		aliases.put("stechkin", "aps");
		
		aliases.put("makarov", "pm");
		
		aliases.put("t92", "type 92");
		
		aliases.put("astra", "357");
		aliases.put("astra revolver", "357");
		
		aliases.put("glock", "glock 17");
		
		aliases.put("thompson", "m1928a1");
		aliases.put("chicago typewriter", "m1928a1");
		
		aliases.put("ingram", "mac-10");
		aliases.put("mac10", "mac-10");
		
		aliases.put("fmg9", "fmg-9");
		
		aliases.put("papasha", "PPSh-41");
		aliases.put("papasha!", "PPSh-41");
		
		aliases.put("pps43", "pps-43");
		
		aliases.put("pp90", "pp-90");
		
		aliases.put("pp2000", "pp-2000");
		
		aliases.put("skorpion", "vz. 61");
		aliases.put("skorp", "vz. 61");
		
		aliases.put("sten", "sten mk ii");
		
		aliases.put("m38", "mab 38");
		
		aliases.put("uzi", "micro uzi");
		
		aliases.put("m1garand", "m1");
		aliases.put("garand", "m1");
		
		aliases.put("springfield", "m1903");
		
		aliases.put("mosin", "m1891");
		aliases.put("nagant", "m1891");
		aliases.put("mosin nagant", "m1891");
		aliases.put("moist nugget", "m1891");
		
		aliases.put("svt38", "svt-38");
		
		aliases.put("simonova", "sks");
		aliases.put("simonov", "sks");
		
		aliases.put("sv98", "sv-98");
		
		aliases.put("kar", "kar98k");
		aliases.put("kar98", "kar98k");
		
		aliases.put("wa", "wa2000");
		aliases.put("wa2k", "wa2000");
		aliases.put("watuk", "wa2000");
		
		aliases.put("t56", "type 56 r");
		aliases.put("type56", "type 56 r");
		
		aliases.put("lee", "MLE Mk I");
		aliases.put("enfield", "MLE Mk I");
		aliases.put("lee enfield", "MLE Mk I");
		aliases.put("aunt lee", "MLE Mk I");
		aliases.put("lee-enfield", "MLE Mk I");
		
		aliases.put("fn49", "fn-49");
		
		aliases.put("vm59", "bm59");
		
		aliases.put("ntw", "ntw-20");
		aliases.put("ntw20", "ntw-20");
		
		aliases.put("ak", "ak-47");
		aliases.put("ak47", "ak-47");
		
		aliases.put("asval", "as val");
		aliases.put("val", "as val");
		
		aliases.put("stg", "stg44");
		
		aliases.put("homete", "g41");
		
		aliases.put("maid", "g36");
		
		aliases.put("416", "hk416");
		aliases.put("all you need", "hk416");
		
		aliases.put("t56", "type 56-1 a");
		aliases.put("type56-1", "type 56-1 a");
		aliases.put("56-1", "type 56-1 a");
		
		aliases.put("galil", "ar");
		
		aliases.put("tar", "tar-21");
		aliases.put("tavor", "tar-21");
		
		aliases.put("sig", "sig-510");
		aliases.put("sig", "sig510");
		
		aliases.put("bar", "m1918");
		aliases.put("auntie bar", "m1918");
		
		aliases.put("saw", "m249 saw");
		aliases.put("m249saw", "m249 saw");
		
		aliases.put("bren", "bren mk i");
		
		aliases.put("fnp9", "fnp-9");
		
		aliases.put("viking", "mp-446");
		aliases.put("mp446", "mp-446");
		
		aliases.put("spectre", "spectre m4");
		aliases.put("spectrem4", "spectre m4");
		
		aliases.put("t64", "type 64");
		aliases.put("type64", "type 64");
		
		aliases.put("t88", "type 88");
		aliases.put("type88", "type 88");
		
		aliases.put("grizzly!", "grizzly");
		
		aliases.put("calico", "m950a");
		
		aliases.put("spp1", "spp-1");
		
		aliases.put("ots12", "ots-12");
		aliases.put("small oats", "ots-12");
		
		aliases.put("aat52", "aat-52");
		
		aliases.put("welrod", "welrod mk ii");
		
		aliases.put("suomi", "KP/-31 ");
		
		aliases.put("z62", "z-62");
		
		aliases.put("psg1", "psg-1");
		
		aliases.put("night princess", "9a-91");
		aliases.put("9a91", "9a-91");
		
		aliases.put("arx160", "arx-160");
		aliases.put("arx", "arx-160");
		
		aliases.put("t79", "type 79");
		aliases.put("type79", "type 79");
		
		aliases.put("t95", "type 95");
		aliases.put("type95", "type 95");
		
		aliases.put("t97", "type 97");
		aliases.put("type97", "type 97");
		
		aliases.put("evo", "evo 3");
		
		aliases.put("t59", "type 59");
		aliases.put("type59", "type 59");
		
		aliases.put("t63", "type 63");
		aliases.put("type63", "type 63");
		
		aliases.put("shrimp", "sr-3mp");
		aliases.put("sr3mp", "sr-3mp");
		
		aliases.put("pp19", "pp-19");
		
		aliases.put("brenten", "bren ten");
		
		aliases.put("usp", "usp compact");
		aliases.put("uspcompact", "usp compact");
		
		aliases.put("iws", "iws 2000");
		aliases.put("iws2k", "iws 2000");
		aliases.put("iws 2k", "iws 2000");
		
		aliases.put("aek", "aek-999");
		aliases.put("aek999", "aek-999");
		
		aliases.put("ithaca", "m37");
		
		aliases.put("super shorty", "super-shorty");
		aliases.put("shorty", "super-shorty");
		
		aliases.put("shark", "ks-23");
		aliases.put("ks23", "ks-23");
		
		aliases.put("rmb93", "rmb-93");
		aliases.put("rmb", "rmb-93");
		
		aliases.put("saiga", "saiga-12");
		aliases.put("saiga12", "saiga-12");
		
		aliases.put("capt china", "type 97 s");
		aliases.put("t97s", "type 97 s");
		aliases.put("captain china", "type 97 s");
		
		aliases.put("spas", "spas-12");
		aliases.put("spas12", "spas-12");
		
		aliases.put("aa12", "aa-12");
		
		aliases.put("fp6", "fp-6");
		
		aliases.put("bird", "m1014");
		aliases.put("birb", "m1014");
		
		aliases.put("axe loli", "cz75");
		
		aliases.put("scw", "colt scw");
		
		aliases.put("ribey", "ribeyrolles1918");
		aliases.put("ribeyrolles", "ribeyrolles1918");
		
		aliases.put("pkp", "Pecheneg");
		
		aliases.put("art", "art556");
		
		aliases.put("dsr", "dsr-50");
		aliases.put("dsr50", "dsr-50");
		
		aliases.put("t5k", "t-5000");
		aliases.put("t5000", "t-5000");
		
		aliases.put("pasta", "sat8");
		
		aliases.put("zas", "zastava m21");
		aliases.put("zas21", "zastava m21");
		
		aliases.put("strawberry", "carcano m1891");
		aliases.put("strawberry carcano", "carcano m1891");
		aliases.put("red carcano", "carcano m1891");
		
		aliases.put("grape", "carcano m91/38");
		aliases.put("grape juice", "carcano m91/38");
		aliases.put("grape carcano", "carcano m91/38");
		aliases.put("purple carcano", "carcano m91/38");
		
		aliases.put("t80", "80type");
		aliases.put("type80", "80type");
		
		aliases.put("thunder", "thunder.50");
		
		aliases.put("honey badger", "pdw");
		
		aliases.put("cz2k", "cz2000");
		
		aliases.put("ots39", "ots-39");
		
		aliases.put("srs", "dtasrs");
		
		aliases.put("cms", "c-ms");
		
		aliases.put("sm1", "sm-1");
		
		aliases.put("mp443", "mp-443");
		
		aliases.put("gsh18", "gsh-18");
		
		aliases.put("tac50", "tac-50");
		aliases.put("tac", "tac-50");
		
		aliases.put("pm06", "pm-06");
		
		aliases.put("cstorm", "cx4 storm");
		aliases.put("cx4", "cx4 storm");
		
		aliases.put("mk12", "mk.12 mod 01");
		
		aliases.put("a91", "a-91");
		
		aliases.put("debiru", "m870p");
		aliases.put("m870", "m870p");
		
		aliases.put("inori", "m82a1");
		
		aliases.put("mp448", "mp-448");
		
		aliases.put("pstorm", "px4 storm");
		aliases.put("px4", "px4 storm");
		
		aliases.put("js9", "js 9 mm");
		
		aliases.put("sar21", "sar-21");
		
		aliases.put("t88", "type88");
	}
	
	
	/*
	 * Obtains a json string from a given url. 
	 * Returns a JSONArray if nothing goes wrong, null otherwise.
	 */
	public static JSONArray getJSONArray(String url) {
		StringBuilder response = new StringBuilder();
		try {
			URLConnection connection = new URL(url).openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));

			String line;
			while ((line = br.readLine()) != null)
				response.append(line);

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		JSONArray json;
		try {
			json = new JSONArray(response.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}

	/*
	 * Obtains a json string from a given url. 
	 * Returns a JSONObject if nothing goes wrong, null otherwise.
	 */
	public static JSONObject getJSONObject(String url) {
		StringBuilder response = new StringBuilder();
		try {
			URLConnection connection = new URL(url).openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));

			String line;
			while ((line = br.readLine()) != null)
				response.append(line);

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		JSONObject json;
		try {
			json = new JSONObject(response.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}
}
