package fr.badblock.auth.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import fr.badblock.auth.Configuration;
import fr.badblock.auth.utils.ByteInputStream;
import fr.badblock.auth.utils.ByteOutputStream;
import lombok.Data;

@Data public class ProfileIP {
	private String   ip;
	private String[] createdAccounts;
	
	public ProfileIP(Player player){
		this.ip 			 = IpProfilesManager.getIp(player);
		this.createdAccounts = new String[]{ player.getName() };
	}
	
	public ProfileIP(String ip){
		this.ip 			 = ip;
		this.createdAccounts = new String[0];
	}
	
	public ProfileIP(File file) throws IOException {
		ByteInputStream stream = new ByteInputStream(new FileInputStream(file));
		ip 				= stream.readUTF();
		createdAccounts = stream.readArrayUTF();
		
		stream.close();
	}
	
	public void saveProfileIP(File file) throws IOException {
		ByteOutputStream stream = new ByteOutputStream(new FileOutputStream(file));
		stream.writeUTF      (ip			 );
		stream.writeArrayUTF (createdAccounts);
		
		stream.close();
	}
	
	public boolean canCreateAccount(){
		return createdAccounts.length < Configuration.MAX_ACCOUNT;
	}
	
	public void createAccount(String account){
		String[] result = new String[createdAccounts.length + 1];
		
		for(int i=0;i<createdAccounts.length;i++){
			result[i] = createdAccounts[i];
		}
		
		result[createdAccounts.length] = account;
		
		this.createdAccounts = result;
	}
	
	public void removeAccount(String account){
		String[] result = new String[createdAccounts.length - 1];
		
		int y = 0;
		for(int i=0;i<createdAccounts.length;i++){
			if(!createdAccounts[i].equalsIgnoreCase(account)) {
				result[y] = createdAccounts[i]; y++;
			}
		}
		
		this.createdAccounts = result;
	}
}
