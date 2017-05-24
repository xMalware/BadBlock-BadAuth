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

		BadblockPlayer badblockPlayer = (BadblockPlayer) e.getPlayer();
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
						}
					}
				});
			}
		}, 1);
		badblockPlayer.sendTranslatedTitle("login.welcome", badblockPlayer.getName());
		badblockPlayer.sendTimings(5, 200, 5);
		System.out.println(badblockPlayer.getName() + " > A");
		PlayerProfilesManager.getInstance().hasProfile(badblockPlayer.getName(), new Callback<Boolean>() {

			public boolean d;
			
			@Override
			public void done(Boolean result, Throwable error) {
				System.out.println(badblockPlayer.getName() + " > B");
				if (d) return;
				System.out.println(badblockPlayer.getName() + " > C");
				boolean bool = result.booleanValue();
				System.out.println("PlayerName(" + badblockPlayer.getName() + ") / hasProfile(" + bool + ")");
				if (!bool) {
					System.out.println(badblockPlayer.getName() + " > C");
					PlayerProfilesManager.getInstance().canCreateAccount(badblockPlayer, new Callback<Boolean>() {

						@Override
						public void done(Boolean result2, Throwable error) {
							System.out.println(badblockPlayer.getName() + " > D");
							boolean bool2 = result2.booleanValue();
							if (!bool2) {
								System.out.println(badblockPlayer.getName() + " > E");
								Bukkit.getScheduler().runTask(AuthPlugin.getInstance(), new Runnable() {
									@Override
									public void run() {
										badblockPlayer.kickPlayer( StringUtils.join( badblockPlayer.getTranslatedMessage("login.too_many_account"), "\n" ) );
									}
								});
								d = true;
							}else{
								System.out.println(badblockPlayer.getName() + " > F");
								badblockPlayer.sendTranslatedMessage("login.register.message");
								new DisconnectRunnable(e.getPlayer());
								d = true;
							}
						}

					});
				}else{
					System.out.println(badblockPlayer.getName() + " > G");
					PlayerProfilesManager.getInstance().getAuthKey(badblockPlayer.getName().toLowerCase(), new Callback<String>() {

						@Override
						public void done(String arg0, Throwable arg1) {
							System.out.println(badblockPlayer.getName() + " > H");
							if (arg0 == null || arg0.isEmpty()) {
								System.out.println(badblockPlayer.getName() + " > I");
								badblockPlayer.sendTranslatedMessage("login.login.message");
								new DisconnectRunnable(e.getPlayer());
							}
						}
						
					});
					d = true;
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