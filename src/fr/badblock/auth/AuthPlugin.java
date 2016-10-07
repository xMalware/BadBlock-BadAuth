package fr.badblock.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.badblock.auth.commands.CommandAChangepassword;
import fr.badblock.auth.commands.CommandAbstract;
import fr.badblock.auth.commands.CommandLogin;
import fr.badblock.auth.commands.CommandPChangePassword;
import fr.badblock.auth.commands.CommandRegister;
import fr.badblock.auth.commands.CommandUnregister;
import fr.badblock.auth.listeners.ConnexionListener;
import fr.badblock.auth.listeners.ProtectionListener;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.auth.runnables.SendRunnable;
import fr.badblock.auth.security.XAUTH;
import fr.badblock.auth.utils.ChatUtils;
import lombok.Getter;

public class AuthPlugin extends JavaPlugin {
	@Getter private static AuthPlugin instance;

	private List<UUID> 				loggedPlayers;
	private List<CommandAbstract> 	commands;
	@Getter private XAUTH 			hasher;

	public boolean isLogged(Player player){
		return loggedPlayers.contains(player.getUniqueId());
	}

	public void setLogged(Player player){
		loggedPlayers.add(player.getUniqueId());
	}

	public void removeLoggedData(Player player){
		loggedPlayers.remove(player.getUniqueId());
	}

	public void finishAuthentification(Player player){
		if(!Configuration.CHANGE_PASSWORD)
			ChatUtils.sendMessage(player, Configuration.SUCESS);
		else ChatUtils.sendMessage(player, Configuration.MESSAGE_CHANGEPW);
		
		setLogged(player);

		if(!Configuration.CHANGE_PASSWORD){
			kick(player);
		}
	}

	public void kick(Player player){
		new SendRunnable(player);
	}

	public void sendPlayer(Player p) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(p.getName());
		out.writeUTF("lobby");
		p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}

	@Override
	public void onEnable(){
		instance	  = this;

		loggedPlayers = new ArrayList<>();
		hasher		  = new XAUTH();
		commands	  = new ArrayList<>();

		Configuration.load(getConfig());

		new PlayerProfilesManager();

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getPluginManager().registerEvents(new ConnexionListener(), this);
		getServer().getPluginManager().registerEvents(new ProtectionListener(), this);

		commands.add(new CommandAChangepassword());
		commands.add(new CommandPChangePassword());
		commands.add(new CommandRegister());
		commands.add(new CommandLogin());
		commands.add(new CommandUnregister());

		saveConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		for(CommandAbstract command : commands){
			if(command.is(label))
				command.executeCommand(sender, args);
		}

		return true;
	}
}
