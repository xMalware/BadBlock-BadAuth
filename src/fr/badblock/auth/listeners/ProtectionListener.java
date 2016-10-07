package fr.badblock.auth.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ProtectionListener implements Listener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		e.setCancelled(true);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		e.setTo(e.getFrom());
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e){
		e.setCancelled(true);
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e){
		e.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e){
		if(e.toWeatherState())
			e.setCancelled(true);
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e){
		e.setCancelled(true);
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

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onSignChange(SignChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onSpread(BlockSpreadEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onFade(BlockFadeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamageBlock(BlockDamageEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockForm(BlockFormEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent e) {
		e.setCancelled(true);
	}
}
