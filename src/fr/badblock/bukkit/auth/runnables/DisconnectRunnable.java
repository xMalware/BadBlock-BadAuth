package fr.badblock.bukkit.auth.runnables;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.bukkit.auth.AuthPlugin;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.StringUtils;

public class DisconnectRunnable extends BukkitRunnable {
	private UUID uniqueId;
	private int time = 60;
	
	public DisconnectRunnable(Player player){
		this.uniqueId = player.getUniqueId();
		runTaskTimer(AuthPlugin.getInstance(), 9, 20L);
	}
	
	@Override
	public void run(){
		BadblockPlayer player = (BadblockPlayer) Bukkit.getPlayer(uniqueId);
		
		if(player == null) {
			cancel();
		} else if(time == 0) {
			player.kickPlayer( StringUtils.join(player.getTranslatedMessage("login.too_long"), " ") );
			cancel();
		}
		
		time--;
	}
}
