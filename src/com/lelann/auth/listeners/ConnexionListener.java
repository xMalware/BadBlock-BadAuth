package com.lelann.auth.listeners;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lelann.auth.AuthPlugin;
import com.lelann.auth.Configuration;
import com.lelann.auth.profile.IpProfilesManager;
import com.lelann.auth.profile.PlayerProfilesManager;
import com.lelann.auth.profile.ProfileIP;
import com.lelann.auth.profile.ProfilePlayer;
import com.lelann.auth.runnables.DisconnectRunnable;
import com.lelann.auth.utils.ChatUtils;

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
