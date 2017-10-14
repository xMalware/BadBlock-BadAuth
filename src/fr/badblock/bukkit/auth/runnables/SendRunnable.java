package fr.badblock.bukkit.auth.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.badblock.bukkit.auth.AuthPlugin;
import fr.badblock.gameapi.players.BadblockPlayer;

public class SendRunnable extends BukkitRunnable {

	private Player bukkitPlayer;

	public SendRunnable(Player bukkitPlayer){
		this.bukkitPlayer = bukkitPlayer;
		runTaskTimer(AuthPlugin.getInstance(), 0L, 60L);
	}

	@Override
	public void run(){
		BadblockPlayer player = (BadblockPlayer) bukkitPlayer;

		if(player == null || !player.isOnline()) {
			cancel();
		} else {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Auth");
			out.writeUTF(AuthPlugin.getInstance().getRealName(player));
			player.sendPluginMessage(AuthPlugin.getInstance(), "BungeeCord", out.toByteArray());
			player.sendPlayer("lobby");
		}
	}
}
