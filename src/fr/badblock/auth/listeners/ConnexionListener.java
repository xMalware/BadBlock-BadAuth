package fr.badblock.auth.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.badblock.auth.AuthPlugin;
import fr.badblock.auth.Configuration;
import fr.badblock.auth.profile.PlayerProfilesManager;
import fr.badblock.auth.runnables.DisconnectRunnable;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.general.StringUtils;

public class ConnexionListener extends BadListener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		e.setJoinMessage(null);
		e.getPlayer().teleport(Configuration.SPAWN);

		BadblockPlayer badblockPlayer = (BadblockPlayer) e.getPlayer();


		PlayerProfilesManager.getInstance().hasProfile(badblockPlayer.getName(), new Callback<Boolean>() {

			boolean has = false;
			
			@Override
			public void done(Boolean result, Throwable error) {
				if(has)
					return;
				has = true;
				
				if (!result) {
					PlayerProfilesManager.getInstance().canCreateAccount(badblockPlayer, new Callback<Boolean>() {

						@Override
						public void done(Boolean result, Throwable error) {
							if (!result) {
								Bukkit.getScheduler().runTask(AuthPlugin.getInstance(), new Runnable() {
									@Override
									public void run() {
										badblockPlayer.kickPlayer( StringUtils.join( badblockPlayer.getTranslatedMessage("login.too_many_account"), "\n" ) );
									}
								});
							}else{
								badblockPlayer.sendTranslatedMessage("login.register.message");
								new DisconnectRunnable(e.getPlayer());
							}
						}

					});
				}else{
					badblockPlayer.sendTranslatedMessage("login.login.message");
					new DisconnectRunnable(e.getPlayer());
				}
			}

		});
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
		AuthPlugin.getInstance().removeLoggedData((BadblockPlayer) e.getPlayer());
	}

}