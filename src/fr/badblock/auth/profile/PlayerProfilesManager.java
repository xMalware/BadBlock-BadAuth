package fr.badblock.auth.profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.auth.AuthPlugin;
import lombok.Getter;

public class PlayerProfilesManager {
	@Getter private static PlayerProfilesManager instance;
	
	private File PLAYER_FOLDER;
	
	public PlayerProfilesManager(File mainFolder){
		instance = this;
		
		this.PLAYER_FOLDER = new File(mainFolder, "players");
		if(!PLAYER_FOLDER.exists()) PLAYER_FOLDER.mkdirs();
	}

	public File getPlayerFile(String name){
		return new File(PLAYER_FOLDER, name.toLowerCase() + ".dat");
	}
	
	public boolean hasProfile(String player){
		return getPlayerFile(player).exists();
	}
	
	public ProfilePlayer getProfile(String player) throws IOException {
		File file = getPlayerFile(player);
		if(!file.exists()) throw new FileNotFoundException();
		
		ProfilePlayer profile = new ProfilePlayer(file);
		return profile;
	}
	
	public ProfilePlayer registerPlayer(Player player, String password){
		return new ProfilePlayer(player, password);
	}
	
	public void savePlayer(ProfilePlayer player) {
		new BukkitRunnable(){
			@Override
			public void run(){
				try {
					player.saveProfilePlayer(getPlayerFile(player.getUsername()));
				} catch (IOException unused){}
			}
		}.runTaskAsynchronously(AuthPlugin.getInstance());
	}
}
