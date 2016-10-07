package fr.badblock.auth.commands;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.auth.AuthPlugin;
import fr.badblock.auth.Configuration;
import fr.badblock.auth.profile.IpProfilesManager;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.auth.profile.ProfileIP;
import fr.badblock.auth.profile.ProfilePlayer;
import fr.badblock.auth.utils.ChatUtils;

public class CommandRegister extends CommandAbstract {
	public CommandRegister() {
		super(false, Configuration.MESSAGE_REGISTER, 2, "register", "reg");
	}

	@Override
	public void doCommand(final CommandSender sender, String[] args) {
		String password = args[0];
		
		if(AuthPlugin.getInstance().isLogged((Player) sender)){
			ChatUtils.sendMessage(sender, Configuration.ALREADY_LOGGED); return;
		}
		
		if(PlayerProfilesManager.getInstance().hasProfile(sender.getName())){
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_LOGIN);
		} else if(!password.equals(args[1])){
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_NOT_SAME);
		} else if(password.length() < Configuration.MIN_PASSWORD_SIZE){
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_TOO_SHORT);
		} else {
			try {
				password = AuthPlugin.getInstance().getHasher().getHash(password);
			} catch (NoSuchAlgorithmException unused){}

			ProfileIP ip;
			try {
				ip = IpProfilesManager.getInstance().getProfile(IpProfilesManager.getIp((Player) sender));
			} catch (IOException unused) {
				ip = new ProfileIP(IpProfilesManager.getIp((Player) sender));
			}
			
			if(!ip.canCreateAccount()){
				ChatUtils.sendMessage(sender, Configuration.TOO_MANY_ACCOUNT); return;
			}
			
			ProfilePlayer player = new ProfilePlayer((Player) sender, password);
			ip.createAccount(sender.getName());
			
			PlayerProfilesManager.getInstance().savePlayer(player);
			IpProfilesManager.getInstance().saveIP(ip);
			
			AuthPlugin.getInstance().finishAuthentification((Player) sender);
		}
	}
	
	@Override
	public void sendHelp(CommandSender sender) {
		if(PlayerProfilesManager.getInstance().hasProfile(sender.getName())){
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_LOGIN);
		} else {
			ChatUtils.sendMessage(sender, Configuration.MESSAGE_REGISTER);
		}
	}
}