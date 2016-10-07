package fr.badblock.auth.profile;

import java.io.File;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.general.Callback;
import lombok.Getter;

public class PlayerProfilesManager {
	
	@Getter private static PlayerProfilesManager instance;
	
	public PlayerProfilesManager(File mainFolder){
		instance = this;
		
	}
	
	public void hasProfile(String player, Callback<Boolean> json){
		GameAPI.getAPI().getLadderDatabase().getPlayerData(player, new Callback<JsonObject>() {

			@Override
			public void done(JsonObject result, Throwable error) {
				json.done(result.has("lastIp"), null); 
			}
			
		});
	}
	
	public void getProfile(String player, Callback<JsonObject> json) {
		GameAPI.getAPI().getLadderDatabase().getPlayerData(player, json);
	}
	
	public void getPassword(String player, Callback<String> password) {
		getProfile(player, new Callback<JsonObject>() {

			@Override
			public void done(JsonObject result, Throwable error) {
				if (!result.has("loginPassword")) {
					password.done(null, null);
					return;
				}
				password.done(result.get("loginPassword").getAsString(), null);
			}
			
		});
	}
	
	public void savePlayer(String player, String password) {
		JsonObject object = new JsonObject();
		object.addProperty("loginPassword", password);
		GameAPI.getAPI().getLadderDatabase().updatePlayerData(player, object);
	}
}
