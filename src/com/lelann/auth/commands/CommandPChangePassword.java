package com.lelann.auth.commands;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lelann.auth.AuthPlugin;
import com.lelann.auth.Configuration;
import com.lelann.auth.profile.PlayerProfilesManager;
import com.lelann.auth.profile.ProfilePlayer;
import com.lelann.auth.utils.ChatUtils;

import net.md_5.bungee.api.ChatColor;

public class CommandPChangePassword extends CommandAbstract {
	public CommandPChangePassword() {
		super(false, Configuration.MESSAGE_CHANGEPW, 2, "changepassword", "changepw", "cpw");
	}

	@Override
	public void doCommand(CommandSender sender, String[] args) {
		if(!AuthPlugin.getInstance().isLogged((Player) sender)){
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_LOGIN); return;
		} else if(!Configuration.CHANGE_PASSWORD){
			ChatUtils.sendMessage(sender, Configuration.ALREADY_LOGGED);
		} else if(!PlayerProfilesManager.getInstance().hasProfile(sender.getName())){
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_REGISTER);
		} else if(!args[0].equals(args[1])){
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_NOT_SAME);
		} else if(args[0].length() < Configuration.MIN_PASSWORD_SIZE){
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_TOO_SHORT);
		} else {
			try {
				ProfilePlayer player = PlayerProfilesManager.getInstance().getProfile(sender.getName());
				
				try {
					args[1] = AuthPlugin.getInstance().getHasher().getHash(args[1]);
				} catch (NoSuchAlgorithmException e) {
					sender.sendMessage(ChatColor.RED + "Erreur lors du changement ! Veuillez contacter un administrateur."); return;
				}
				
				player.setPassword(args[1]);
				PlayerProfilesManager.getInstance().savePlayer(player);

				ChatUtils.sendMessage(sender, Configuration.MESSAGE_CHANGED);
				AuthPlugin.getInstance().kick((Player) sender);
			} catch (IOException e) {
				e.printStackTrace();
				ChatUtils.sendMessage(sender, Configuration.MESSAGE_REGISTER);
			}
		}
	}
	
	@Override
	public void sendHelp(CommandSender sender) {
		if(AuthPlugin.getInstance().isLogged((Player) sender)){
			if(Configuration.CHANGE_PASSWORD){
				ChatUtils.sendMessage(sender, Configuration.MESSAGE_CHANGEPW);
			} else {
				ChatUtils.sendMessage(sender, Configuration.ALREADY_LOGGED);
			}
		} else if(PlayerProfilesManager.getInstance().hasProfile(sender.getName())){
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_LOGIN);
		} else {
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_REGISTER);
		}
	}
}
