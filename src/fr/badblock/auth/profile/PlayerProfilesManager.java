package fr.badblock.auth.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fr.badblock.auth.Configuration;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;
import lombok.Getter;

public class PlayerProfilesManager {

	@Getter private static PlayerProfilesManager instance;

	public PlayerProfilesManager(){
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

	public void savePlayer(BadblockPlayer player, String password) {
		JsonObject object = new JsonObject();
		if (!object.has("loginPassword")) {
			GameAPI.getAPI().getLadderDatabase().getIpPlayerData(player, new Callback<JsonObject>() {

				@Override
				public void done(JsonObject result, Throwable error) {
					JsonArray jsonArray = null;
					if (!result.has("players")) jsonArray = null;
					else jsonArray = (JsonArray) result.get("players");
					jsonArray.add(new JsonPrimitive(player.getName()));
					result.add("players", jsonArray);
					GameAPI.getAPI().getLadderDatabase().updateIpPlayerData(player, result);
				}

			});
		}
		object.addProperty("loginPassword", password);
		GameAPI.getAPI().getLadderDatabase().updatePlayerData(player, object);
	}

	public void removePlayer(String player) {
		JsonObject object = new JsonObject();
		if (!object.has("loginPassword")) return;
		object.remove("loginPassword");
		GameAPI.getAPI().getLadderDatabase().updatePlayerData(player, object);
	}

	public void savePlayer(String player, String password) {
		JsonObject object = new JsonObject();
		object.addProperty("loginPassword", password);
		GameAPI.getAPI().getLadderDatabase().updatePlayerData(player, object);
	}

	public void canCreateAccount(BadblockPlayer player, Callback<Boolean> isAble) {
		GameAPI.getAPI().getLadderDatabase().getIpPlayerData(player, new Callback<JsonObject>() {

			@Override
			public void done(JsonObject result, Throwable error) {
				if (!result.has("players")) {
					isAble.done(true, null);
					return;
				}
				JsonArray jsonArray = (JsonArray) result.get("players");
				isAble.done(jsonArray.size() < Configuration.MAX_ACCOUNT, null);
			}

		});
	}

}