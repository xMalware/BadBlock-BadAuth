package fr.badblock.auth.commands;

import java.security.NoSuchAlgorithmException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.auth.AuthPlugin;
import fr.badblock.auth.Configuration;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.auth.utils.ChatUtils;
import fr.badblock.gameapi.utils.general.Callback;

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

		PlayerProfilesManager.getInstance().hasProfile(sender.getName(), new Callback<Boolean>() {

			@Override
			public void done(Boolean result, Throwable error) {
				if (!result) {
					ChatUtils.sendMessage(sender, Configuration.MESSAGE_REGISTER);
				}else{
					try {
						PlayerProfilesManager.getInstance().getPassword(sender.getName(), new Callback<String>() {

							@Override
							public void done(String result, Throwable error) {
								try {
									if(!AuthPlugin.getInstance().getHasher().comparePassword(result, password)){
										ChatUtils.sendMessage(sender, Configuration.MESSAGE_PASSWORD);
									}else{
										AuthPlugin.getInstance().finishAuthentification((Player) sender);
									}
								} catch (NoSuchAlgorithmException e) {
									e.printStackTrace();
								}
							}

						});
					} catch (Exception unused) { 
						unused.printStackTrace();
						return; 
					}
				}
			}
		});
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