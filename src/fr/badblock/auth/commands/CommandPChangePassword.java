package fr.badblock.auth.commands;

import java.security.NoSuchAlgorithmException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.auth.AuthPlugin;
import fr.badblock.auth.Configuration;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.auth.utils.ChatUtils;
import fr.badblock.gameapi.utils.general.Callback;
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
		} else{
			PlayerProfilesManager.getInstance().hasProfile(sender.getName(), new Callback<Boolean>() {

				@Override
				public void done(Boolean result, Throwable error) {
					if (result) {
						if(!args[0].equals(args[1])){
							ChatUtils.sendMessage(sender, Configuration.MESSAGE_NOT_SAME);
						} else if(args[0].length() < Configuration.MIN_PASSWORD_SIZE){
							ChatUtils.sendMessage(sender, Configuration.MESSAGE_TOO_SHORT);
						} else {
							try {
								args[1] = AuthPlugin.getInstance().getHasher().getHash(args[1]);
							} catch (NoSuchAlgorithmException e) {
								sender.sendMessage(ChatColor.RED + "Erreur lors du changement ! Veuillez contacter un administrateur."); return;
							}

							PlayerProfilesManager.getInstance().savePlayer(sender.getName(), args[1]);
							ChatUtils.sendMessage(sender, Configuration.MESSAGE_CHANGED);
							AuthPlugin.getInstance().kick((Player) sender);
						}
					} else {
						ChatUtils.sendMessage(sender, Configuration.MESSAGE_REGISTER);
					}
				}
			});
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
		} else{
			PlayerProfilesManager.getInstance().hasProfile(sender.getName(), new Callback<Boolean>() {

				@Override
				public void done(Boolean result, Throwable error) {
					if (result) {
						ChatUtils.sendMessage(sender, Configuration.MESSAGE_LOGIN);
					} else {
						ChatUtils.sendMessage(sender, Configuration.MESSAGE_REGISTER);
					}
				}
			});
		}
	}

}
