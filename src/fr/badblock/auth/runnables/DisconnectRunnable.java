package fr.badblock.auth.runnables;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.auth.AuthPlugin;
import fr.badblock.auth.utils.ChatUtils;

public class DisconnectRunnable extends BukkitRunnable {
	private UUID uniqueId;
	private int time = 60;
	
	public DisconnectRunnable(Player player){
		this.uniqueId = player.getUniqueId();
		runTaskTimer(AuthPlugin.getInstance(), 9, 20L);
	}
	
	@Override
	public void run(){
		Player player = Bukkit.getPlayer(uniqueId);
		
		if(player == null) {
			cancel();
		} else if(time == 0) {
			player.kickPlayer(ChatUtils.colorReplace("&cVous avez été trop long à vous connecté !"));
			cancel();
		}
		
		time--;
	}
}
