package fr.badblock.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.badblock.auth.commands.CommandLogin;
import fr.badblock.auth.commands.CommandRegister;
import fr.badblock.auth.listeners.ConnexionListener;
import fr.badblock.auth.listeners.LoginMapProtector;
import fr.badblock.auth.listeners.ProtectionListener;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.auth.runnables.SendRunnable;
import fr.badblock.auth.security.XAUTH;
import fr.badblock.gameapi.BadblockPlugin;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.RunType;
import lombok.Getter;

public class AuthPlugin extends BadblockPlugin {
	@Getter private static AuthPlugin instance;

	private List<UUID> 				loggedPlayers;
	@Getter private XAUTH 			hasher;

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
		player.sendMessage("login.success");
		
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
