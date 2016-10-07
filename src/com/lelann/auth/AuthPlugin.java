package com.lelann.auth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.lelann.auth.commands.CommandAChangepassword;
import com.lelann.auth.commands.CommandAbstract;
import com.lelann.auth.commands.CommandLogin;
import com.lelann.auth.commands.CommandPChangePassword;
import com.lelann.auth.commands.CommandRegister;
import com.lelann.auth.commands.CommandUnregister;
import com.lelann.auth.listeners.ConnexionListener;
import com.lelann.auth.listeners.ProtectionListener;
import com.lelann.auth.profile.IpProfilesManager;
import com.lelann.auth.profile.PlayerProfilesManager;
import com.lelann.auth.runnables.SendRunnable;
import com.lelann.auth.security.XAUTH;
import com.lelann.auth.utils.ChatUtils;

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

		new PlayerProfilesManager(new File(Configuration.DATA_FOLDER));
		new IpProfilesManager(new File(Configuration.DATA_FOLDER));

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
