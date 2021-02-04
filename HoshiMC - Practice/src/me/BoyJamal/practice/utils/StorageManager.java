package me.BoyJamal.practice.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.BoyJamal.practice.Main;
import me.BoyJamal.practice.listeners.ArenaListeners;

public class StorageManager {

	public static List<KitUtil> loadedKits = new ArrayList<>();
	public static List<ArenaUtil> loadedArenas = new ArrayList<>();
	
	private File kitsFile = new File(Main.getInstance().getDataFolder()+"/kits.yml");
	private YamlConfiguration kitsYML = null;
	private static Inventory kitMang = null;
	
	private static File arenaFile = new File(Main.getInstance().getDataFolder()+"/arena.yml");
	private static YamlConfiguration arenaYML = null;
	private static Inventory arenaMang = null;
	
	private static File configFile = new File(Main.getInstance().getDataFolder() + "/config.yml");
	private static FileConfiguration config = null;
	
	private static Location spawnLoc = null;
	
	private static void copy(InputStream in, File file)
    {
    	try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void loadFiles()
	{
		//load config
		if (!(configFile.exists()))
		{
			if (!(configFile.getParentFile().exists()))
			{
				configFile.getParentFile().mkdirs();
			}
			
			try {
				configFile.createNewFile();
				copy(Main.getInstance().getResource("config.yml"),configFile);
				config = YamlConfiguration.loadConfiguration(configFile);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		} else {
			try {
				config = YamlConfiguration.loadConfiguration(configFile);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
		try {
			if (config == null)
			{
				System.out.println(MainUtils.chatColor("&c&lError! &7&oConfig is currently null!"));
				return;
			}
			
			ConfigurationSection spawnSection = config.getConfigurationSection("spawnLocation");
			if (spawnSection != null)
			{
				this.spawnLoc = new Location(Bukkit.getWorld(spawnSection.getString("worldName")), 
							            spawnSection.getDouble("xVal"), 
							            spawnSection.getDouble("yVal"),
							            spawnSection.getDouble("zVal"));  
				spawnLoc.setPitch(spawnSection.getInt("pitch"));
				spawnLoc.setYaw(spawnSection.getInt("yaw"));
			}
		} catch (Exception exc) {exc.printStackTrace(); return;}
		
		//load Kits
		if (!(kitsFile.exists()))
		{
			if (!(kitsFile.getParentFile().exists()))
			{
				kitsFile.getParentFile().mkdirs();
			}
			
			try {
				kitsFile.createNewFile();
				copy(Main.getInstance().getResource("kits.yml"),kitsFile);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
		try {
			kitsYML = YamlConfiguration.loadConfiguration(kitsFile);
			loadKits();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		//load Arenas
		if (!(arenaFile.exists()))
		{
			if (!(arenaFile.getParentFile().exists()))
			{
				arenaFile.getParentFile().mkdirs();
			}
			
			try {
				arenaFile.createNewFile();
				copy(Main.getInstance().getResource("arena.yml"),arenaFile);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
		try {
			arenaYML = YamlConfiguration.loadConfiguration(arenaFile);
			loadArenas();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	public void loadKits()
	{
		ConfigurationSection kits = kitsYML.getConfigurationSection("kits");
		if (kits != null)
		{
			for (String kitName : kits.getKeys(false))
			{
				ConfigurationSection items = kits.getConfigurationSection(kitName + ".items");
				if (items == null)
				{
					return;
				}
				
				List<ItemStack> kitItems = new ArrayList<>();
				for (String id : items.getKeys(false))
				{
					ItemStack item;
					try {
						//get values
						int idItem = items.getInt(id + ".id");
						int amount = items.getInt(id + ".amount");
						short data = (short)items.getInt(id + ".data");
						String name = MainUtils.chatColor(items.getString(id + ".name"));
						List<String> lore = MainUtils.listColor(items.getStringList(id + ".lore"));
						List<EnchantmentUtil> enchants = MainUtils.getEnchants(items.getStringList(id + ".enchants"));
						
						//set values
						item = new ItemStack(Material.getMaterial(idItem),amount,data);
						ItemMeta im = item.getItemMeta();
						if (!(name.equalsIgnoreCase("")))
						{
							im.setDisplayName(name);
						}
						
						if (!(lore.get(0).equalsIgnoreCase("")))
						{
							im.setLore(lore);
						}
						item.setItemMeta(im);
						if (enchants.size() > 0)
						{
							for (EnchantmentUtil enchant : enchants)
							{
								if (!(item.containsEnchantment(enchant.getType())))
								{
									item.addUnsafeEnchantment(enchant.getType(), enchant.getLevel());
								}
							}
						}
						
						kitItems.add(item);
					}  catch (Exception exc) {
						continue;
					}
				}
				
				loadedKits.add(new KitUtil(kitName,kitItems));
			}
		}
		
		//load mainKitGui
		
		Inventory mainGui = Bukkit.createInventory(null, MainUtils.getSlots(loadedKits.size()),MainUtils.chatColor("&fSelect Kit:"));
		int count = 0;
		
		for (KitUtil each : loadedKits)
		{
			ItemStack display;
			if (each.getItems().get(0) != null)
			{
				display = each.getItems().get(0);
				ItemMeta im = display.getItemMeta();
				im.setDisplayName(MainUtils.chatColor("&a&l" + each.getName()));
				List<String> lore = new ArrayList<>();
				lore.add("");
				lore.add(MainUtils.chatColor("&f&nClick to select kit!"));
				im.setLore(lore);
				im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				display.setItemMeta(im);
			} else {
				display = new ItemStack(Material.BARRIER);
				ItemMeta im = display.getItemMeta();
				im.setDisplayName(MainUtils.chatColor("&a&l" + each.getName()));
				List<String> lore = new ArrayList<>();
				lore.add("");
				lore.add(MainUtils.chatColor("&f&nClick to select kit!"));
				im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				im.setLore(lore);
				display.setItemMeta(im);
			}
			
			mainGui.setItem(count, display);
			count++;
		}
		
		while (count % 9 != 0)
		{
			count++;
		}
		
		for (int i = count; i <=count+3;i++)
		{
			mainGui.setItem(i, MainUtils.glassItem());
		}
		count+=4;
		mainGui.setItem(count, MainUtils.returnItem());
		count++;
		for (int i = count; i <=count+3;i++)
		{
			mainGui.setItem(i, MainUtils.glassItem());
		}
		
		kitMang = mainGui;
	}
	
	public static void saveArenas()
	{
		for (ArenaUtil each : loadedArenas)
		{
			if (arenaYML.getConfigurationSection("arenas." + each.getID()) != null)
			{
				arenaYML.set("arenas." + each.getID(), null);
			}
			
			arenaYML.set("arenas." + each.getID() + ".player1", LocationStringer.toString(each.getLocations().getPlayerOneSpawn()));
			arenaYML.set("arenas." + each.getID() + ".player2", LocationStringer.toString(each.getLocations().getPlayerTwoSpawn()));
			arenaYML.set("arenas." + each.getID() + ".name", each.getName());
			
			try {
				arenaYML.save(arenaFile);
			} catch(Exception e) {
				Bukkit.getLogger().severe("Could not save data for: " + each.getID());
				continue;
			}
		} 
	}
	
	public void loadArenas()
	{
		ConfigurationSection arenas = arenaYML.getConfigurationSection("arenas");
		if (arenas != null)
		{
			for (String id : arenas.getKeys(false))
			{
				Location spawnOne;
				Location spawnTwo;
				try {
					spawnOne = LocationStringer.fromString(arenas.getString(id + ".player1"));
				} catch (Exception exc) {
					Bukkit.getLogger().severe("Error! Could not load spawn location 1 in Arena " + id);
					exc.printStackTrace();
					continue;
				}
				
				try {
					spawnTwo = LocationStringer.fromString(arenas.getString(id + ".player2"));
				} catch (Exception exc) {
					Bukkit.getLogger().severe("Error! Could not load spawn location 2 in Arena " + id);
					continue;
				}
				String name = arenas.getString(id + ".name");
				loadedArenas.add(new ArenaUtil(new LocationUtil(spawnOne,spawnTwo),name,Integer.valueOf(id)));
			}
		}
	}
	
	public static Inventory getUnrankedArenaSelector()
	{
		Inventory mainGui = Bukkit.createInventory(null, MainUtils.getSlots(loadedKits.size()),MainUtils.chatColor("&fJoin Unranked Queue:"));
		int count = 0;
		
		for (KitUtil each : loadedKits)
		{
			int activeBattles = 0;
			int inQueue = 0;
			for (StartBattle active : ArenaListeners.activeBattles)
			{
				if (active.getKit().getName().equalsIgnoreCase(each.getName()))
				{
					activeBattles+=2;
				}
			}
			
			for (QueueUtil queue : QueueUtil.queueList)
			{
				if (queue.getKit().getName().equalsIgnoreCase(each.getName())  && queue.getType().equalsIgnoreCase("unranked"))
				{
					inQueue++;
				}
			}
			
			ItemStack display;
			if (each.getItems().get(0) != null)
			{
				display = each.getItems().get(0);
				ItemMeta im = display.getItemMeta();
				im.setDisplayName(MainUtils.chatColor("&a&l" + each.getName()));
				List<String> lore = new ArrayList<>();
				lore.add("");
				lore.add(MainUtils.chatColor("&bStatistics: "));
				lore.add(MainUtils.chatColor("&b&l* &f" + activeBattles+ " &7In fights"));
				lore.add(MainUtils.chatColor("&b&l* &f" + inQueue + " &7Queued"));
				lore.add(MainUtils.chatColor(""));
				lore.add(MainUtils.chatColor("&f&nClick to select " + each.getName() + "!"));
				im.setLore(lore);
				im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				display.setItemMeta(im);
			} else {
				display = new ItemStack(Material.BARRIER);
				ItemMeta im = display.getItemMeta();
				im.setDisplayName(MainUtils.chatColor("&a&l" + each.getName()));
				List<String> lore = new ArrayList<>();
				lore.add("");
				lore.add(MainUtils.chatColor("&bStatistics: "));
				lore.add("");
				lore.add(MainUtils.chatColor("&b&l* &f" + activeBattles+ " &7In fights"));
				lore.add(MainUtils.chatColor("&b&l* &f" + inQueue + " &7Queued"));
				lore.add(MainUtils.chatColor(""));
				lore.add(MainUtils.chatColor("&f&nClick to select " + each.getName() + "!"));
				im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				im.setLore(lore);
				display.setItemMeta(im);
			}
			
			mainGui.setItem(count, display);
			count++;
		}
		
		while (count % 9 != 0)
		{
			count++;
		}
		
		for (int i = count; i <=count+3;i++)
		{
			mainGui.setItem(i, MainUtils.glassItem());
		}
		count+=4;
		mainGui.setItem(count, MainUtils.returnItem());
		count++;
		for (int i = count; i <=count+3;i++)
		{
			mainGui.setItem(i, MainUtils.glassItem());
		}
		
		return mainGui;
	}
	
	public static Inventory getKitGui()
	{
		return kitMang;
	}
	
	public static Location getSpawnLoc()
	{
		return spawnLoc;
	}
	
}
