package fr.badblock.auth.commands;

import java.security.NoSuchAlgorithmException;

import org.bukkit.command.CommandSender;

import fr.badblock.auth.AuthPlugin;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class CommandAChangepassword extends AbstractCommand {
	public CommandAChangepassword() {
		super("achangepassword", new TranslatableString("login.admin.changepassword.usage"), GamePermission.ADMIN);
	}


	@Override
	public boolean executeCommand(CommandSender sender, String[] args){
		if(args.length < 2)
			return false;
		
		PlayerProfilesManager.getInstance().hasProfile(args[0], new Callback<Boolean>() {

			@Override
			public void done(Boolean result, Throwable error) {
				if (!result) {
					new TranslatableString("login.admin.unknow-profile").send(sender);
					return;
				}
				
				try {
					args[1] = AuthPlugin.getInstance().getHasher().getHash(args[1]);
				} catch (NoSuchAlgorithmException e) {
					new TranslatableString("login.admin.changepassword.error").send(sender);
					return;
				}

				PlayerProfilesManager.getInstance().savePlayer(args[0], args[1]);
				new TranslatableString("login.admin.changepassword.success").send(sender);
			}
		});
		
		return true;
	}
}
