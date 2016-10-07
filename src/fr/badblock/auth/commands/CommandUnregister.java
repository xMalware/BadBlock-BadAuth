package fr.badblock.auth.commands;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.badblock.auth.profile.IpProfilesManager;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.auth.profile.ProfileIP;
import fr.badblock.auth.profile.ProfilePlayer;

public class CommandUnregister extends CommandAbstract {
	public CommandUnregister() {
		super(true, "&cUtilisation : /unregister <player>", 1, "unregister");
	}

	@Override
	public void doCommand(CommandSender sender, String[] args) {
		if(!PlayerProfilesManager.getInstance().hasProfile(args[0])){
			sender.sendMessage(ChatColor.RED + "Le profile demandé n'existe pas !");
		} else {
			try {
				ProfilePlayer player = PlayerProfilesManager.getInstance().getProfile(args[0]);
				ProfileIP	  ip	 = IpProfilesManager.getInstance().getProfile(player.getFirstIp());

				ip.removeAccount(player.getUsername());

				if(ip.getCreatedAccounts().length == 0){
					IpProfilesManager.getInstance().getIPFile(ip.getIp()).delete();
				} else {
					IpProfilesManager.getInstance().saveIP(ip);
				}

				PlayerProfilesManager.getInstance().getPlayerFile(args[0]).delete();
				sender.sendMessage(ChatColor.GREEN + "Le profile a bien été supprimé !");
			} catch (IOException e) {
				sender.sendMessage(ChatColor.RED + "Une erreur a eu lieue !");
			}

		}
	}
}