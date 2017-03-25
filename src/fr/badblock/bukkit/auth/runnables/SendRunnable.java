package fr.badblock.bukkit.auth.runnables;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.bukkit.auth.AuthPlugin;
import fr.badblock.gameapi.players.BadblockPlayer;

public class SendRunnable extends BukkitRunnable {
	private UUID uniqueId;
	
	public SendRunnable(Player player){
		this.uniqueId = player.getUniqueId();
		runTaskTimer(AuthPlugin.getInstance(), 0, 60L);
	}
	
	@Override
	public void run(){
		BadblockPlayer player = (BadblockPlayer) Bukkit.getPlayer(uniqueId);
		
		if(player == null) {
			cancel();
		} else player.sendPlayer("lobby");
	}
}
