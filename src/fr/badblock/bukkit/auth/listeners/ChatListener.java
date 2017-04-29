package fr.badblock.bukkit.auth.listeners;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import fr.badblock.bukkit.auth.AuthPlugin;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;

public class ChatListener extends BadListener {

	public static GoogleAuthenticator gAuth = new GoogleAuthenticator();
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();
		if (e.getMessage().toCharArray()[0] != '/') {
			e.setCancelled(true);
			AuthPlugin authPlugin = AuthPlugin.getInstance();
			UUID uuid = player.getUniqueId();
			if (authPlugin.semiAuthPlayers.containsKey(uuid)) {
				try {
					int temporaryCode = Integer.parseInt(e.getMessage());
					String secretKey = authPlugin.semiAuthPlayers.get(uuid);
					int correctTemporaryCode = gAuth.getTotpPassword(secretKey);
					if (temporaryCode == correctTemporaryCode) {
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
