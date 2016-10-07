package fr.badblock.auth.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.gameapi.utils.general.Callback;

public class CommandUnregister extends CommandAbstract {
	public CommandUnregister() {
		super(true, "&cUtilisation : /unregister <player>", 1, "unregister");
	}

	@Override
	public void doCommand(CommandSender sender, String[] args) {
		PlayerProfilesManager.getInstance().hasProfile(args[0], new Callback<Boolean>() {

			@Override
			public void done(Boolean result, Throwable error) {
				if (!result) {
					sender.sendMessage(ChatColor.RED + "Le profile demandé n'existe pas !");
				}else {
					PlayerProfilesManager.getInstance().removePlayer(args[0]);
					sender.sendMessage(ChatColor.GREEN + "Le profile a bien été supprimé !");
				}

			}
		});
	}
}