package fr.badblock.bukkit.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.badblock.bukkit.auth.commands.CommandLogin;
import fr.badblock.bukkit.auth.commands.CommandRegister;
import fr.badblock.bukkit.auth.listeners.ConnexionListener;
import fr.badblock.bukkit.auth.listeners.LoginMapProtector;
import fr.badblock.bukkit.auth.listeners.ProtectionListener;
import fr.badblock.bukkit.auth.profile.PlayerProfilesManager;
import fr.badblock.bukkit.auth.runnables.SendRunnable;
import fr.badblock.bukkit.auth.security.XAUTH;
import fr.badblock.gameapi.BadblockPlugin;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.RunType;
import lombok.Getter;

public class AuthPlugin extends BadblockPlugin {
	@Getter private static AuthPlugin instance;

	private List<UUID> 				loggedPlayers;
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
	}

	public void finishAuthentification(BadblockPlayer player){
		player.sendTranslatedMessage("login.success");
		
		setLogged(player);
		kick(player);
	}

	public void kick(Player player){
		new SendRunnable(player);
	}

	@Override
	public void onEnable(RunType runType){
		instance	  = this;

		loggedPlayers = new ArrayList<>();
		hasher		  = new XAUTH();
		startTime	  = System.currentTimeMillis();
		
		Configuration.load(getConfig());

		new PlayerProfilesManager();
		
		getAPI().setMapProtector(new LoginMapProtector());
		
		new ConnexionListener();
		new ProtectionListener();

		new CommandRegister();
		new CommandLogin();

		saveConfig();
	}
}
