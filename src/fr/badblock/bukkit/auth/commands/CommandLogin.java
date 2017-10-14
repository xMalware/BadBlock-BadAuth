package fr.badblock.bukkit.auth.commands;

import java.security.NoSuchAlgorithmException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.bukkit.auth.AuthPlugin;
import fr.badblock.bukkit.auth.profile.PlayerProfilesManager;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class CommandLogin extends AbstractCommand {
	public CommandLogin() {
		super("login", new TranslatableString("login.login.message"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER, "log", "l");
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(final CommandSender sender, String[] args) {
		if(args.length < 1)
			return false;

		BadblockPlayer player = (BadblockPlayer) sender;

		String password = args[0];

		if(AuthPlugin.getInstance().isLogged(player)){
			player.sendTranslatedMessage("login.login.already_logged");
			return true;
		}
		String realName = AuthPlugin.getInstance().getRealName(player);
		PlayerProfilesManager.getInstance().hasProfile(realName, new Callback<Boolean>() {

			@Override
			public void done(Boolean result, Throwable error) {
				if (!result) {
					player.sendTranslatedMessage("login.register.message");
				} else{
					try {
						PlayerProfilesManager.getInstance().getPassword(realName, new Callback<String>() {

							@Override
							public void done(String result, Throwable error) {
								try {
									if(!AuthPlugin.getInstance().getHasher().comparePassword(result, password)){
										player.sendTranslatedMessage("login.login.bad_password");
									} else{
										AuthPlugin.getInstance().finishAuthentification(player);
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

		return true;
	}

	@Override
	public void sendUsage(CommandSender sender) {
		if (!(sender instanceof Player)) return;
		String realName = AuthPlugin.getInstance().getRealName((BadblockPlayer) sender);
		PlayerProfilesManager.getInstance().hasProfile(realName, new Callback<Boolean>() {

			@Override
			public void done(Boolean result, Throwable error) {
				if (result) {
					new TranslatableString("login.login.message").send(sender);
				} else {
					new TranslatableString("login.register.message").send(sender);
				}
			}
		});
	}
}