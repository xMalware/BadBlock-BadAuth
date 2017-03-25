package fr.badblock.bukkit.auth;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class Configuration {
	public static Location	SPAWN				= Bukkit.getWorlds().get(0).getSpawnLocation();

	public static int 		MAX_ACCOUNT 		= 2;
	public static int		MIN_PASSWORD_SIZE   = 6;
	public static int		MAX_TIME_OPEN		= 7200;
	
	public static String	DATA_FOLDER			= new File("data").getAbsolutePath();

	public static void load(ConfigurationSection config){
		MAX_ACCOUNT 		= get(config, "MAX_ACCOUNT", MAX_ACCOUNT);
		MIN_PASSWORD_SIZE 	= get(config, "MIN_PASSWORD_SIZE", MIN_PASSWORD_SIZE);

		DATA_FOLDER 		= get(config, "DATA_FOLDER", DATA_FOLDER);
		MAX_TIME_OPEN 		= get(config, "MAX_TIME_OPEN", 7200);

		if(!config.contains("SPAWN")){
			config.set("SPAWN.world", SPAWN.getWorld().getName());
			config.set("SPAWN.x", SPAWN.getX());
			config.set("SPAWN.y", SPAWN.getY());
			config.set("SPAWN.z", SPAWN.getZ());
			config.set("SPAWN.yaw", SPAWN.getYaw());
			config.set("SPAWN.pitch", SPAWN.getPitch());
		}

		SPAWN = new Location(
				Bukkit.getWorld(config.getString("SPAWN.world")),
				config.getDouble("SPAWN.x"),
				config.getDouble("SPAWN.y"),
				config.getDouble("SPAWN.z"),
				(float) config.getDouble("SPAWN.yaw"),
				(float) config.getDouble("SPAWN.pitch")
				);
	}

	private static String get(ConfigurationSection config, String path, String def){
		if(!config.contains(path))
			config.set(path, def);
		return config.getString(path);
	}

	private static int get(ConfigurationSection config, String path, int def){
		if(!config.contains(path))
			config.set(path, def);
		return config.getInt(path);
	}
}
