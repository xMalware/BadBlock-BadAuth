package fr.badblock.auth;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class Configuration {
	public static Location	SPAWN				= Bukkit.getWorlds().get(0).getSpawnLocation();

	public static int 		MAX_ACCOUNT 		= 2;
	public static int		MIN_PASSWORD_SIZE   = 6;
	
	public static boolean   CHANGE_PASSWORD		= false;

	public static String	DATA_FOLDER			= new File("data").getAbsolutePath();

	public static String 	MESSAGE_LOGIN 		= "Utilise /login <password>";
	public static String    MESSAGE_PASSWORD    = "Mauvais mot de passe !";

	public static String 	TOO_MANY_ACCOUNT	= "Vous avez trop de compte sur BadBlock !";
	public static String    ALREADY_LOGGED		= "Vous êtes déjà connecté !";
	public static String    SUCESS				= "Vous allez être téléporté !";

	public static String 	MESSAGE_REGISTER 	= "Utilise /register <password> <password>";
	public static String 	MESSAGE_NOT_SAME 	= "Les deux mots de passes sont différents.";
	public static String	MESSAGE_TOO_SHORT   = "Le mot de passe est trop court !";
	
	public static String	MESSAGE_CHANGEPW	= "Utilise /changepassword <pw> <pw>";
	public static String	MESSAGE_CHANGED		= "Votre mot de passe a été changé ! Vous allez être TP !";

	public static void load(ConfigurationSection config){
		MAX_ACCOUNT 		= get(config, "MAX_ACCOUNT", MAX_ACCOUNT);
		MIN_PASSWORD_SIZE 	= get(config, "MIN_PASSWORD_SIZE", MIN_PASSWORD_SIZE);

		CHANGE_PASSWORD		= get(config, "CHANGE_PASSWORD", CHANGE_PASSWORD);
		
		DATA_FOLDER 		= get(config, "DATA_FOLDER", DATA_FOLDER);

		MESSAGE_LOGIN 	 	= get(config, "MESSAGE_LOGIN", MESSAGE_LOGIN);
		MESSAGE_PASSWORD 	= get(config, "MESSAGE_PASSWORD", MESSAGE_PASSWORD);

		TOO_MANY_ACCOUNT 	= get(config, "TOO_MANY_ACCOUNT", TOO_MANY_ACCOUNT);
		ALREADY_LOGGED 		= get(config, "ALREADY_LOGGED", ALREADY_LOGGED);
		SUCESS		 		= get(config, "SUCESS", SUCESS);

		MESSAGE_REGISTER 	= get(config, "MESSAGE_REGISTER", MESSAGE_REGISTER);
		MESSAGE_NOT_SAME 	= get(config, "MESSAGE_NOT_SAME", MESSAGE_NOT_SAME);
		MESSAGE_TOO_SHORT 	= get(config, "MESSAGE_TOO_SHORT", MESSAGE_TOO_SHORT);

		MESSAGE_CHANGEPW 	= get(config, "MESSAGE_CHANGEPW", MESSAGE_CHANGEPW);
		MESSAGE_CHANGED 	= get(config, "MESSAGE_CHANGED", MESSAGE_CHANGED);

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
	
	private static boolean get(ConfigurationSection config, String path, boolean def){
		if(!config.contains(path))
			config.set(path, def);
		return config.getBoolean(path);
	}
}
