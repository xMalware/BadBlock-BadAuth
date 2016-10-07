package com.lelann.auth.commands;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.bukkit.command.CommandSender;

import com.lelann.auth.AuthPlugin;
import com.lelann.auth.profile.PlayerProfilesManager;
import com.lelann.auth.profile.ProfilePlayer;

import net.md_5.bungee.api.ChatColor;

public class CommandAChangepassword extends CommandAbstract {
	public CommandAChangepassword() {
		super(true, "&cUtilisation : /achangepassword <player> <password>", 2, "achangepassword");
	}

	@Override
	public void doCommand(CommandSender sender, String[] args) {
		if(!PlayerProfilesManager.getInstance().hasProfile(args[0])){
			sender.sendMessage(ChatColor.RED + "Le profile demandé n'existe pas !");
		} else {
			try {
				ProfilePlayer player = PlayerProfilesManager.getInstance().getProfile(args[0]);
				
				try {
					args[1] = AuthPlugin.getInstance().getHasher().getHash(args[1]);
				} catch (NoSuchAlgorithmException e) {
					sender.sendMessage(ChatColor.RED + "Erreur lors du changement !"); return;
				}
				
				player.setPassword(args[1]);
				PlayerProfilesManager.getInstance().savePlayer(player);
				
				sender.sendMessage(ChatColor.GREEN + "Le mot de passe a été changé correctement !");
			} catch (IOException e) {
				sender.sendMessage(ChatColor.RED + "Le profile demandé n'existe pas !");
			}
		}
	}
}
