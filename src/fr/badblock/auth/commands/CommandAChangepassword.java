package fr.badblock.auth.commands;

import java.security.NoSuchAlgorithmException;

import org.bukkit.command.CommandSender;

import fr.badblock.auth.AuthPlugin;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.gameapi.utils.general.Callback;
import net.md_5.bungee.api.ChatColor;

public class CommandAChangepassword extends CommandAbstract {
	public CommandAChangepassword() {
		super(true, "&cUtilisation : /achangepassword <player> <password>", 2, "achangepassword");
	}

	@Override
	public void doCommand(CommandSender sender, String[] args) {
		PlayerProfilesManager.getInstance().hasProfile(args[0], new Callback<Boolean>() {

			@Override
			public void done(Boolean result, Throwable error) {
				if (!result) {
					sender.sendMessage(ChatColor.RED + "Le profile demandé n'existe pas !");
					return;
				}
				try {
					args[1] = AuthPlugin.getInstance().getHasher().getHash(args[1]);
				} catch (NoSuchAlgorithmException e) {
					sender.sendMessage(ChatColor.RED + "Erreur lors du changement !"); return;
				}

				PlayerProfilesManager.getInstance().savePlayer(args[0], args[1]);
				sender.sendMessage(ChatColor.GREEN + "Le mot de passe a été changé correctement !");
			}

		});
	}
	
}
