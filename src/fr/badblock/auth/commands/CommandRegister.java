package fr.badblock.auth.commands;

import java.security.NoSuchAlgorithmException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.auth.AuthPlugin;
import fr.badblock.auth.Configuration;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.auth.utils.ChatUtils;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;

public class CommandRegister extends CommandAbstract {
	public CommandRegister() {
		super(false, Configuration.MESSAGE_REGISTER, 2, "register", "reg");
	}

	@Override
	public void doCommand(final CommandSender sender, String[] args) {

		if(AuthPlugin.getInstance().isLogged((Player) sender)){
			ChatUtils.sendMessage(sender, Configuration.ALREADY_LOGGED); return;
		}

		PlayerProfilesManager.getInstance().hasProfile(sender.getName(), new Callback<Boolean>() {

			@Override
			public void done(Boolean result, Throwable error) {
				String password = args[0];
				if (result) {
					ChatUtils.sendMessage(sender, Configuration.MESSAGE_LOGIN);
				} else if(!password.equals(args[1])){
					ChatUtils.sendMessage(sender, Configuration.MESSAGE_NOT_SAME);
				} else if(password.length() < Configuration.MIN_PASSWORD_SIZE){
					ChatUtils.sendMessage(sender, Configuration.MESSAGE_TOO_SHORT);
				} else {
					try {
						password = AuthPlugin.getInstance().getHasher().getHash(password);
					} catch (NoSuchAlgorithmException unused){}
					final String finalPassword = password;
					PlayerProfilesManager.getInstance().canCreateAccount((((BadblockPlayer)((Player) sender))), new Callback<Boolean>() {

						@Override
						public void done(Boolean result, Throwable error) {
							if (!result) {
								ChatUtils.sendMessage(sender, Configuration.TOO_MANY_ACCOUNT); return;
							}else{
								PlayerProfilesManager.getInstance().savePlayer(sender.getName(), finalPassword);
								AuthPlugin.getInstance().finishAuthentification((Player) sender);
							}
						}
					});
				}
			}
		});
	}

	@Override
	public void sendHelp(CommandSender sender) {
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