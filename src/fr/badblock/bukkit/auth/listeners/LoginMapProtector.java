package fr.badblock.bukkit.auth.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.servers.MapProtector;

public class LoginMapProtector implements MapProtector {
	@Override
	public boolean allowBlockFormChange(Block block) {
		return false;
	}

	@Override
	public boolean allowBlockPhysics(Block block) {
		return false;
	}

	@Override
	public boolean allowExplosion(Location location) {
		return false;
	}

	@Override
	public boolean allowFire(Block block) {
		return false;
	}

	@Override
	public boolean allowInteract(Entity entity) {
		return false;
	}

	@Override
	public boolean allowLeavesDecay(Block block) {
		return false;
	}

	@Override
	public boolean allowMelting(Block block) {
		return false;
	}

	@Override
	public boolean allowPistonMove(Block block) {
		return false;
	}

	@Override
	public boolean allowRaining() {
		return false;
	}

	@Override
	public boolean blockBreak(BadblockPlayer player, Block block) {
		return false;
	}

	@Override
	public boolean blockPlace(BadblockPlayer player, Block block) {
		return false;
	}

	@Override
	public boolean canBeingDamaged(BadblockPlayer player) {
		return false;
	}

	@Override
	public boolean canBlockDamage(Block block) {
		return false;
	}

	@Override
	public boolean canCombust(Entity entity) {
		return false;
	}

	@Override
	public boolean canCreatureSpawn(Entity creature, boolean isPlugin) {
		return false;
	}

	@Override
	public boolean canDrop(BadblockPlayer player) {
		return false;
	}

	@Override
	public boolean canEmptyBucket(BadblockPlayer player) {
		return false;
	}

	@Override
	public boolean canEnchant(BadblockPlayer player, Block table) {
		return false;
	}

	@Override
	public boolean canEntityBeingDamaged(Entity entity) {
		return false;
	}

	@Override
	public boolean canFillBucket(BadblockPlayer player) {
		return false;
	}

	@Override
	public boolean canInteract(BadblockPlayer player, Action action, Block block) {
		return false;
	}

	@Override
	public boolean canInteractArmorStand(BadblockPlayer player, ArmorStand entity) {
		return false;
	}

	@Override
	public boolean canInteractEntity(BadblockPlayer player, Entity entity) {
		return false;
	}

	@Override
	public boolean canItemDespawn(Item item) {
		return false;
	}

	@Override
	public boolean canItemSpawn(Item item) {
		return false;
	}

	@Override
	public boolean canLostFood(BadblockPlayer player) {
		return false;
	}

	@Override
	public boolean canPickup(BadblockPlayer player) {
		return false;
	}

	@Override
	public boolean canSoilChange(Block soil) {
		return false;
	}

	@Override
	public boolean canSpawn(Entity entity) {
		return false;
	}

	@Override
	public boolean canUseBed(BadblockPlayer player, Block bed) {
		return false;
	}

	@Override
	public boolean canUsePortal(BadblockPlayer player) {
		return false;
	}

	@Override
	public boolean destroyArrow() {
		return true;
	}

	@Override
	public boolean healOnJoin(BadblockPlayer player) {
		return true;
	}

	@Override
	public boolean modifyItemFrame(BadblockPlayer player, Entity itemFrame) {
		return false;
	}

	@Override
	public boolean modifyItemFrame(Entity itemframe) {
		return false;
	}

	@Override
	public boolean canEntityBeingDamaged(Entity entity, BadblockPlayer badblockPlayer) {
		return false;
	}

}
