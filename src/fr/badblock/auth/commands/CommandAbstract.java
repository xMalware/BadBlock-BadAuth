package fr.badblock.auth.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.auth.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;

public abstract class CommandAbstract {
	private boolean   admin;
	private String    use;
	private int       argsLength;
	private String[]  names;
	
	public CommandAbstract(boolean admin, String use, int args, String... names){
		this.admin 		 = admin;
		this.use   		 = use;
		this.argsLength  = args;
		this.names       = names;
	}
	
	public boolean is(String name){
		for(String trueName : names){
			if(trueName.equalsIgnoreCase(name))
				return true;
		}
		
		return false;
	}
	
	public void sendHelp(CommandSender sender) {
		ChatUtils.sendMessage(sender, use);
	}
	
	public void executeCommand(CommandSender sender, String[] args){
		if(!(sender instanceof Player)){
			if(!admin){
				sender.sendMessage(ChatColor.RED + "Cette commande est reservée aux joueurs !");
				return;
			}
		} else if(admin){
			return;
		}
		
		if(args.length != argsLength){
			sendHelp(sender);
		} else doCommand(sender, args);
	}
	
	public abstract void doCommand(CommandSender sender, String[] args);
}
