package fr.badblock.auth.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.auth.AuthPlugin;
import fr.badblock.auth.Configuration;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.auth.profile.ProfilePlayer;
import fr.badblock.auth.utils.ChatUtils;

public class CommandLogin extends CommandAbstract {
	public CommandLogin() {
		super(false, Configuration.MESSAGE_LOGIN, 1, "login", "log", "l");
	}

	@Override
	public void doCommand(final CommandSender sender, String[] args) {
		String password = args[0];

		if(AuthPlugin.getInstance().isLogged((Player) sender)){
			ChatUtils.sendMessage(sender, Configuration.ALREADY_LOGGED); return;
		}
		
		if(!PlayerProfilesManager.getInstance().hasProfile(sender.getName())){
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_REGISTER);
		} else {
			ProfilePlayer player;
			try {
				player = PlayerProfilesManager.getInstance().getProfile(sender.getName());

				if(!AuthPlugin.getInstance().getHasher().comparePassword(player.getPassword(), password)){
					ChatUtils.sendMessage(sender, Configuration.MESSAGE_PASSWORD);
				} else {
					player.update((Player) sender);
					PlayerProfilesManager.getInstance().savePlayer(player);
					
					AuthPlugin.getInstance().finishAuthentification((Player) sender);
				}
			} catch (Exception unused) { 
				unused.printStackTrace();
				return; }
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