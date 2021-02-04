package me.BoyJamal.practice.utils;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.BoyJamal.practice.Main;

public class DataManagment {
	
	public static HashMap<String,PlayerData> activeData = new HashMap<>();
	private static File dataFolder = new File(Main.getInstance().getDataFolder() + "/data");
	
	public static void loadData()
	{
		if (!(dataFolder.exists()))
		{
			try {
				dataFolder.mkdir();
			} catch (Exception e) {}
		}
		File[] eachFile = dataFolder.listFiles();
		
		for (File playerFile : eachFile)
		{
			FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
			String key = "playerdata";
			String uuid;
			int kills,hits,wins,losses,elo,deaths;
			double damage;
			
			try {
				uuid = config.getString(key + ".uuid");
				kills = config.getInt(key + ".kills");
				deaths = config.getInt(key + ".deaths");
				hits = config.getInt(key + ".hits");
				wins = config.getInt(key + ".wins");
				losses = config.getInt(key + ".losses");
				elo = config.getInt(key + ".elo");
				damage = config.getDouble(key + ".damage");
				activeData.put(uuid, new PlayerData(uuid,deaths,kills,hits,damage,wins,losses,elo));
			} catch (Exception exc ) {
				Bukkit.getLogger().severe("Error loading in data for: " + playerFile.getName());
				continue;
			}
		}
		
		System.out.println(MainUtils.chatColor("&a&lSuccess! &7&oLoaded all player data!"));
	}
	
	public static void saveData()
	{
		for (PlayerData each : activeData.values())
		{
			File file = new File(Main.getInstance().getDataFolder() + File.separator + "data/" + each.getUUID() + ".yml");
			if (!(file.exists()))
			{
				try {
					file.createNewFile();
				} catch (Exception e) {
					continue;
				}
			}
			
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			String key = "playerdata";
			if (config.getConfigurationSection(key) != null)
			{
				config.set(key, null);
			}
			
			config.set(key + ".uuid", each.getUUID());
			config.set(key + ".kills", each.getKills());
			config.set(key + ".hits", each.getHitsLanded());
			config.set(key + ".damage", each.getDamageDealt());
			config.set(key + ".deaths", each.getDeaths());
			config.set(key + ".wins", each.getWins());
			config.set(key + ".losses", each.getLosses());
			config.set(key + ".elo", each.getElo());
			
			try {
				config.save(file);
			} catch(Exception e) {
				Bukkit.getLogger().severe("Could not save data for: " + each.getUUID());
				continue;
			}
		}
		
		System.out.println(MainUtils.chatColor("&a&lSuccess! &7&oSaved all player data!"));
	}

}
