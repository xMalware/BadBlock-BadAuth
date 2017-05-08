package fr.badblock.bukkit.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.badblock.bukkit.auth.commands.CommandLogin;
import fr.badblock.bukkit.auth.commands.CommandRegister;
import fr.badblock.bukkit.auth.listeners.ChatListener;
import fr.badblock.bukkit.auth.listeners.ConnexionListener;
import fr.badblock.bukkit.auth.listeners.LoginMapProtector;
import fr.badblock.bukkit.auth.listeners.ProtectionListener;
import fr.badblock.bukkit.auth.profile.PlayerProfilesManager;
import fr.badblock.bukkit.auth.runnables.SendRunnable;
import fr.badblock.bukkit.auth.security.XAUTH;
import fr.badblock.gameapi.BadblockPlugin;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.Callback;
import lombok.Getter;

public class AuthPlugin extends BadblockPlugin {
	@Getter private static AuthPlugin instance;

	public List<UUID> 				loggedPlayers;
	public Map<UUID, String> 		semiAuthPlayers;
	@Getter private XAUTH 			hasher;
	@Getter private long			startTime;

	public boolean isLogged(BadblockPlayer player){
		return loggedPlayers.contains(player.getUniqueId());
	}

	public void setLogged(BadblockPlayer player){
		loggedPlayers.add(player.getUniqueId());
	}

	public void removeLoggedData(BadblockPlayer player){
		loggedPlayers.remove(player.getUniqueId());
		semiAuthPlayers.remove(player.getUniqueId());
	}

	public void finishAuthentification(BadblockPlayer player) {
		if (semiAuthPlayers.containsKey(player.getUniqueId())) return;
		PlayerProfilesManager.getInstance().getAuthKey(player.getName(), new Callback<String>() {

			@Override
			public void done(String string, Throwable throwable) {
				if (string == null || string.isEmpty()) {
					kickAuth(player, false);
				}else{
					if (semiAuthPlayers.containsKey(player.getUniqueId())) return;
					semiAuthPlayers.put(player.getUniqueId(), string);
					player.sendTranslatedMessage("login.please_now_type_double_auth");
				}
			};
		
		});
	}
	
	public void kickAuth(BadblockPlayer player, boolean googleAuth) {
		if (!googleAuth) {
			player.sendTranslatedMessage("login.success");
			player.sendTranslatedMessage("login.you_can_enable_auth");
		}
		setLogged(player);
		kick(player);
	}
	
	public void kick(Player player){
		new SendRunnable(player);
	}

	@Override
	public void onEnable(RunType runType){
		instance	  	= this;

		semiAuthPlayers = new HashMap<>();
		loggedPlayers 	= new ArrayList<>();
		hasher		  	= new XAUTH();
		startTime	  	= System.currentTimeMillis();
		
		Configuration.load(getConfig());

		new PlayerProfilesManager();
		
		getAPI().setMapProtector(new LoginMapProtector());

		new ChatListener();
		new ConnexionListener();
		new ProtectionListener();

		new CommandRegister();
		new CommandLogin();

		saveConfig();
	}
}
