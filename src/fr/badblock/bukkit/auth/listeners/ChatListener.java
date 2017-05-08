package fr.badblock.bukkit.auth.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import fr.badblock.bukkit.auth.AuthPlugin;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;

public class ChatListener extends BadListener {

	public static GoogleAuthenticator gAuth = new GoogleAuthenticator();
	public static Map<Character, Character> map = new HashMap<Character, Character>();

	static
	{
		map.put('&', '1');
		map.put('!', '1');

		map.put('é', '2');
		map.put('@', '2');
		
		map.put('"', '3');
		map.put('#', '3');

		map.put('\'', '4');
		map.put('$', '4');

		map.put('(', '5');
		map.put('%', '5');

		map.put('-', '6');
		map.put('^', '6');

		map.put('è', '7');
		map.put('&', '7');

		map.put('_', '8');
		map.put('*', '8');

		map.put('ç', '9');
		map.put('(', '9');

		map.put('à', '0');
		map.put(')', '0');
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();
		if (e.getMessage().toCharArray()[0] != '/') {
			e.setCancelled(true);
			AuthPlugin authPlugin = AuthPlugin.getInstance();
			UUID uuid = player.getUniqueId();
			if (authPlugin.semiAuthPlayers.containsKey(uuid)) {
				String message = "";
				
				for(char c : e.getMessage().toCharArray())
				{
					if(Character.isDigit(c))
						message += c;
					else if(map.containsKey(c))
						message = map.get(c).toString();
				}
				
				try {
					int temporaryCode = Integer.parseInt( message );
					String secretKey = authPlugin.semiAuthPlayers.get(uuid);
					if (gAuth.authorize(secretKey, temporaryCode)) {
						player.sendTranslatedMessage("login.auth_success");
						authPlugin.kickAuth(player, true);
						return;
					}
					player.sendTranslatedMessage("login.error_auth");
				}catch(Exception error) {
					player.sendTranslatedMessage("login.please_type_only_numbers_auth");
				}
			}
		}
	}
	
}
