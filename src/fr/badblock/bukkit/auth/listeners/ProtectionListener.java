package fr.badblock.bukkit.auth.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.badblock.gameapi.BadListener;

public class ProtectionListener extends BadListener {
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Location to = e.getTo(), from = e.getFrom();
		
		to.setX(from.getX());
		to.setY(from.getY());
		to.setZ(from.getZ());
		
		e.setTo(to);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (e.getMessage().toCharArray()[0] != '/') {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerInventoryOpen(InventoryOpenEvent event){
		event.setCancelled(true);
		event.getPlayer().closeInventory();
	}
}
