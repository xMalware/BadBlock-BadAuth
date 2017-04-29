package fr.badblock.bukkit.auth.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.badblock.bukkit.auth.AuthPlugin;
import fr.badblock.bukkit.auth.Configuration;
import fr.badblock.bukkit.auth.profile.PlayerProfilesManager;
import fr.badblock.bukkit.auth.runnables.DisconnectRunnable;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class ConnexionListener extends BadListener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		e.setJoinMessage(null);
		e.getPlayer().teleport(Configuration.SPAWN);

		BadblockPlayer badblockPlayer = (BadblockPlayer) e.getPlayer();


		PlayerProfilesManager.getInstance().isOnline(badblockPlayer.getName(), new Callback<Boolean>() {

			@Override
			public void done(Boolean onlineMode, Throwable throwable) {
				boolean bool = onlineMode.booleanValue();
				System.out.println(badblockPlayer.getName() + " / " + bool);
				if (bool) {
					/*PlayerProfilesManager.getInstance().getAuthKey(badblockPlayer.getName(), new Callback<String>() {
						@Override
						public void done(String secretKey, Throwable throwable) {
							if (secretKey != null && !secretKey.isEmpty()) {*/
								AuthPlugin.getInstance().finishAuthentification(badblockPlayer);
							/*}
						}
					});*/
				}
			}
		});
		badblockPlayer.sendTranslatedTitle("login.welcome", badblockPlayer.getName());
		badblockPlayer.sendTimings(5, 200, 5);
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
		AuthPlugin main = AuthPlugin.getInstance();
		main.removeLoggedData((BadblockPlayer) e.getPlayer());
		TaskManager.runTaskLater(new Runnable() {
			@Override
			public void run() {
				if (main.getServer().getOnlinePlayers().size() == 0) {
					long maxTime = main.getStartTime() + (Configuration.MAX_TIME_OPEN * 1000L);
					if (System.currentTimeMillis() >= maxTime) {
						main.getServer().getConsoleSender().sendMessage("§b[AuthPlugin] I need to reboot NOW.");
						main.getServer().shutdown();
					}
				}
			}
		}, 1);
	}

}