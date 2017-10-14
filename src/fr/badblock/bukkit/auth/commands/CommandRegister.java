package fr.badblock.bukkit.auth.commands;

import java.security.NoSuchAlgorithmException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.bukkit.auth.AuthPlugin;
import fr.badblock.bukkit.auth.Configuration;
import fr.badblock.bukkit.auth.profile.PlayerProfilesManager;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class CommandRegister extends AbstractCommand {
	public CommandRegister() {
		super("register", new TranslatableString("login.login.message"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER, "reg");
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(final CommandSender sender, String[] args) {
		if(args.length < 2)
			return false;

		BadblockPlayer player = (BadblockPlayer) sender;

		if(AuthPlugin.getInstance().isLogged(player)){
			player.sendTranslatedMessage("login.login.bad_password");
			return true;
		}
		String realName = AuthPlugin.getInstance().getRealName(player);
		PlayerProfilesManager.getInstance().hasProfile(realName, new Callback<Boolean>() {

			@Override
			public void done(Boolean result, Throwable error) {
				String password = args[0];
				if (result) {
					player.sendTranslatedMessage("login.login.message");
				} else if(!password.equals(args[1])){
					player.sendTranslatedMessage("login.register.not_same");
				} else if(password.length() < Configuration.MIN_PASSWORD_SIZE){
					player.sendTranslatedMessage("login.register.too_short");
				} else {
					try {
						password = AuthPlugin.getInstance().getHasher().getHash(password);
					} catch (NoSuchAlgorithmException unused){}
					final String finalPassword = password;
					PlayerProfilesManager.getInstance().canCreateAccount((((BadblockPlayer)((Player) sender))), new Callback<Boolean>() {

						@Override
						public void done(Boolean result, Throwable error) {
							if (!result) {
								player.sendTranslatedMessage("login.too_many_account");
							} else{
								PlayerProfilesManager.getInstance().savePlayer(realName, finalPassword);
								AuthPlugin.getInstance().finishAuthentification(player);
							}
						}
					});
				}
			}
		});
		
		return true;
	}

	@Override
	public void sendUsage(CommandSender sender) {
		PlayerProfilesManager.getInstance().hasProfile(sender.getName(), new Callback<Boolean>() {

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