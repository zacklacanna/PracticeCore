package me.BoyJamal.practice;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.BoyJamal.practice.commands.ArenaCMD;
import me.BoyJamal.practice.commands.PartyCMD;
import me.BoyJamal.practice.commands.StatsCMD;
import me.BoyJamal.practice.listeners.ArenaGuiListener;
import me.BoyJamal.practice.listeners.ArenaListeners;
import me.BoyJamal.practice.listeners.ArenaSelectorListener;
import me.BoyJamal.practice.listeners.EachKitListener;
import me.BoyJamal.practice.listeners.ItemListeners;
import me.BoyJamal.practice.listeners.MainKitListener;
import me.BoyJamal.practice.listeners.PartyChatListener;
import me.BoyJamal.practice.listeners.StatsListener;
import me.BoyJamal.practice.utils.StorageManager;
import me.BoyJamal.practice.utils.DataManagment;
import me.BoyJamal.practice.utils.EntityHider;
import me.BoyJamal.practice.utils.MainUtils;
import me.BoyJamal.practice.utils.EntityHider.Policy;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private EntityHider hider;
	public static boolean titles;
	
	public void onEnable()
	{
		instance = this;
		hider = new EntityHider(this, Policy.WHITELIST);
		
		StorageManager mang = new StorageManager();
		mang.loadFiles();
		DataManagment.loadData();
		new BukkitRunnable()
		{
			public void run()
			{
				DataManagment.saveData();
				Bukkit.getServer().broadcastMessage(MainUtils.chatColor("&eAutoSave! &7&oAll player data and stats have been saved"));
			}
		}.runTaskTimer(Main.getInstance(), 0, 20*60*15);
		
		if (Bukkit.getPluginManager().getPlugin("TitleAPI").isEnabled())
		{
			titles = true;
		} else {
			titles = false;
		}
		
		registerCommands();
		registerListeners();
	}
	
	public void onDisable()
	{
		DataManagment.saveData();
	}
	
	private void registerCommands()
	{
		getCommand("arena").setExecutor(new ArenaCMD());
		getCommand("party").setExecutor(new PartyCMD());
		getCommand("stats").setExecutor(new StatsCMD());
	}
	
	public EntityHider getHider()
	{
		return hider;
	}
	
	private void registerListeners()
	{
		Bukkit.getPluginManager().registerEvents(new ItemListeners(), this);
		Bukkit.getPluginManager().registerEvents(new ArenaListeners(), this);
		Bukkit.getPluginManager().registerEvents(new EachKitListener(), this);
		Bukkit.getPluginManager().registerEvents(new MainKitListener(), this);
		Bukkit.getPluginManager().registerEvents(new ArenaSelectorListener(), this);
		Bukkit.getPluginManager().registerEvents(new PartyChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new StatsListener(), this);
		Bukkit.getPluginManager().registerEvents(new ArenaGuiListener(), this);
	}
	
	public static Main getInstance()
	{
		return instance;
	}
	
	public boolean hasTitles()
	{
		return titles;
	}

}
