package fr.badblock.auth.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import fr.badblock.auth.utils.ByteInputStream;
import fr.badblock.auth.utils.ByteOutputStream;
import lombok.Data;

@Data public class ProfilePlayer {
	private String  	username;
	private String 		password;
	private long   		lastConnexion;
	private String 		ip;
	private String		firstIp;

	public ProfilePlayer(Player player, String password){
		this.username 		= player.getName();
		this.password 		= password;
		this.lastConnexion 	= System.currentTimeMillis();
		this.ip 			= IpProfilesManager.getIp(player);
		this.firstIp		= ip;
	}
	
	public ProfilePlayer(File file) throws IOException {
		ByteInputStream stream = new ByteInputStream(new FileInputStream(file));
		username 		= stream.readUTF();
		password 		= stream.readUTF();
		lastConnexion 	= stream.readLong();
		ip 				= stream.readUTF();
		firstIp 		= stream.readUTF();
	
		stream.close();
	}
	
	public void saveProfilePlayer(File file) throws IOException {
		ByteOutputStream stream = new ByteOutputStream(new FileOutputStream(file));
		stream.writeUTF (username     );
		stream.writeUTF (password     );
		stream.writeLong(lastConnexion);
		stream.writeUTF (ip			  );
		stream.writeUTF (firstIp	  );
		
		stream.close();
	}
	
	public void update(Player player){
		lastConnexion = System.currentTimeMillis();
		ip			  = IpProfilesManager.getIp(player);
	}
}
