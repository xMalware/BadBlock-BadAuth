package fr.badblock.auth.listeners;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.badblock.auth.AuthPlugin;
import fr.badblock.auth.Configuration;
import fr.badblock.auth.profile.IpProfilesManager;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.auth.profile.ProfileIP;
import fr.badblock.auth.profile.ProfilePlayer;
import fr.badblock.auth.runnables.DisconnectRunnable;
import fr.badblock.auth.utils.ChatUtils;

public class ConnexionListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		e.setJoinMessage(null);
		e.getPlayer().teleport(Configuration.SPAWN);
		
		ProfilePlayer player = null;
		ProfileIP	  ip 	 = null;
		
		try {
			player = PlayerProfilesManager.getInstance().getProfile(e.getPlayer().getName());
		} catch (IOException unused){}
		
		try {
			ip     = IpProfilesManager.getInstance().getProfile(IpProfilesManager.getIp(e.getPlayer()));
		} catch (IOException unused){}
		
		boolean canCreateAccount = true;
		
		if(ip != null){
			canCreateAccount = ip.canCreateAccount();
		}
		
		if(player != null){
			ChatUtils.sendMessage(e.getPlayer(), Configuration.MESSAGE_LOGIN);
		} else if(!canCreateAccount) {
			e.getPlayer().kickPlayer(ChatUtils.colorReplace(Configuration.TOO_MANY_ACCOUNT));
			return;
		} else {
			ChatUtils.sendMessage(e.getPlayer(), Configuration.MESSAGE_REGISTER);
		}
		
		new DisconnectRunnable(e.getPlayer());
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(!p.getUniqueId().equals(e.getPlayer().getUniqueId())){
				p.hidePlayer(e.getPlayer());
				e.getPlayer().hidePlayer(p);
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		e.setQuitMessage(null);
		AuthPlugin.getInstance().removeLoggedData(e.getPlayer());
	}
}
