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
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.events.api.PlayerLoadedEvent;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class ConnexionListener extends BadListener {

	@EventHandler
	public void onLoaded(PlayerLoadedEvent event) {
		event.getPlayer().teleport(Configuration.SPAWN);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		e.setJoinMessage(null);
		e.getPlayer().teleport(Configuration.SPAWN);

		GameBadblockPlayer badblockPlayer = (GameBadblockPlayer) e.getPlayer();
		String realName = badblockPlayer.getRealName() != null ? badblockPlayer.getRealName() : badblockPlayer.getName();
		String realLowerName = realName.toLowerCase();;
		badblockPlayer.sendTranslatedTitle("login.welcome", realName);
		badblockPlayer.sendTimings(5, 200, 5);
		PlayerProfilesManager.getInstance().hasProfile(realLowerName, new Callback<Boolean>() {

			@Override
			public void done(Boolean result, Throwable error) {
				boolean bool = result.booleanValue();
				System.out.println("PlayerName(" + realName + ") / hasProfile(" + bool + ")");
				if (!bool) {
					TaskManager.runTaskLater(new Runnable() {
						@Override
						public void run() {
							PlayerProfilesManager.getInstance().canCreateAccount(badblockPlayer, new Callback<Boolean>() {

								@Override
								public void done(Boolean result2, Throwable error) {
									boolean bool2 = result2.booleanValue();
									if (!bool2) {
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
						}
					}, 1);
				}else{
					TaskManager.runTaskLater(new Runnable() {
						@Override
						public void run() {
							PlayerProfilesManager.getInstance().isOnline(badblockPlayer.getName(), new Callback<Boolean>() {

								@Override
								public void done(Boolean onlineMode, Throwable throwable) {
									boolean bool = onlineMode.booleanValue();
									if (bool) {
										AuthPlugin.getInstance().finishAuthentification(badblockPlayer);
										new DisconnectRunnable(e.getPlayer());
									}else{
										TaskManager.runTaskLater(new Runnable() {
											@Override
											public void run() {
												PlayerProfilesManager.getInstance().getAuthKey(realLowerName, new Callback<String>() {

													@Override
													public void done(String arg0, Throwable arg1) {
														badblockPlayer.sendTranslatedMessage("login.login.message");
														new DisconnectRunnable(e.getPlayer());
													}

												});
											}
										}, 1);
									}
								}
							});
						}
					}, 1);
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
		}, 20);
	}

}