package fr.badblock.auth.profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.auth.AuthPlugin;
import lombok.Getter;

public class IpProfilesManager {
	public static String getIp(Player player){
		return player.getAddress().getAddress().toString().replace("/", "");
	}
	
	@Getter private static IpProfilesManager instance;
	
	private File IP_FOLDER;
	
	public IpProfilesManager(File mainFolder){
		instance = this;
		
		this.IP_FOLDER     = new File(mainFolder, "ips");
	
		if(!IP_FOLDER.exists())     IP_FOLDER.mkdirs();
	}

	public File getIPFile(String ip){
		return new File(IP_FOLDER, ip.toLowerCase() + ".dat");
	}
	
	public boolean hasProfile(String ip){
		return getIPFile(ip).exists();
	}
	
	public ProfileIP getProfile(String ip) throws IOException {
		File file = getIPFile(ip);
		if(!file.exists()) throw new FileNotFoundException();
		
		ProfileIP profile = new ProfileIP(file);
		return profile;
	}
	
	public void saveIP(ProfileIP ip) {
		new BukkitRunnable(){
			@Override
			public void run(){
				try {
					ip.saveProfileIP(getIPFile(ip.getIp()));
				} catch (IOException unused){}
			}
		}.runTaskAsynchronously(AuthPlugin.getInstance());

	}
}
