package com.lelann.auth.runnables;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.lelann.auth.AuthPlugin;

public class SendRunnable extends BukkitRunnable {
	private UUID uniqueId;
	
	public SendRunnable(Player player){
		this.uniqueId = player.getUniqueId();
		runTaskTimer(AuthPlugin.getInstance(), 0, 60L);
	}
	
	@Override
	public void run(){
		Player player = Bukkit.getPlayer(uniqueId);
		
		if(player == null) cancel();
		else AuthPlugin.getInstance().sendPlayer(player);
	}
}
