package fr.badblock.bukkit.auth.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.bukkit.auth.AuthPlugin;
import fr.badblock.gameapi.players.BadblockPlayer;

public class SendRunnable extends BukkitRunnable {
	
	private Player bukkitPlayer;
	
	public SendRunnable(Player bukkitPlayer){
		this.bukkitPlayer = bukkitPlayer;
		runTaskTimer(AuthPlugin.getInstance(), 20L, 60L);
	}
	
	@Override
	public void run(){
		BadblockPlayer player = (BadblockPlayer) bukkitPlayer;
		
		if(player == null || !player.isOnline()) {
			cancel();
		} else player.sendPlayer("lobby");
	}
}
