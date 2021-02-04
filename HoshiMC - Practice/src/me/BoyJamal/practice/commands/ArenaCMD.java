package me.BoyJamal.practice.commands;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.BoyJamal.practice.Main;
import me.BoyJamal.practice.utils.ArenaUtil;
import me.BoyJamal.practice.utils.LocationUtil;
import me.BoyJamal.practice.utils.MainUtils;
import me.BoyJamal.practice.utils.StorageManager;

public class ArenaCMD implements CommandExecutor {
	
	private HashMap<String,Location> arenaCreation = new HashMap<>();
	public static HashMap<String,LocationUtil> finalCreation = new HashMap<>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			return true;
		}
		
		Player p = (Player)sender;
		if (p.hasPermission("hoshimc.admin"))
		{
			if (args.length == 0)
			{
				if (MainUtils.arenaGui(1) != null)
				{
					p.openInventory(MainUtils.arenaGui(1));
					p.playSound(p.getLocation(), Sound.CHEST_OPEN, 500, 500);
					return true;
				} else {
					p.sendMessage(MainUtils.chatColor("&c&lError! &7&oNo loaded arenas yet!"));
					return true;
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("create"))
				{
					if (arenaCreation.containsKey(p.getUniqueId().toString()))
					{
						Location spawnOne = arenaCreation.get(p.getUniqueId().toString());
						if (spawnOne == null)
						{
							arenaCreation.replace(p.getUniqueId().toString(), p.getLocation());
							p.sendMessage(MainUtils.chatColor("&cArenaCreation! &7&oType \"/arena create\" again to set spawn point two where you are standing!"));
							new BukkitRunnable()
							{
								public void run()
								{
									if (arenaCreation.containsKey(p.getUniqueId().toString()))
									{
										arenaCreation.remove(p.getUniqueId().toString());
										p.sendMessage(MainUtils.chatColor("&cArenaCreation! &7&oYou took too long, progress has been cleared. Type /arena create to restart!"));
									}
								}
							}.runTaskLater(Main.getInstance(),20*100);
							return true;
						} else {
							finalCreation.put(p.getUniqueId().toString(), new LocationUtil(arenaCreation.get(p.getUniqueId().toString()),p.getLocation()));
							arenaCreation.remove(p.getUniqueId().toString());
							p.sendMessage(MainUtils.chatColor("&cArenaCreation! &7&oCongrats! You have saved locations for Arena #" + StorageManager.loadedArenas.size() + ". Type in chat the name you want to call it!"));
							new BukkitRunnable()
							{
								public void run()
								{
									if (finalCreation.containsKey(p.getUniqueId().toString()))
									{
										finalCreation.remove(p.getUniqueId().toString());
										p.sendMessage(MainUtils.chatColor("&cArenaCreation! &7&oYou took too long, progress has been cleared. Type /arena create to restart!"));
									}
								}
							}.runTaskLater(Main.getInstance(),20*100);
							return true;
						}
					} else {
						arenaCreation.put(p.getUniqueId().toString(), null);
						p.sendMessage(MainUtils.chatColor("&cArenaCreation! &7&oType \"/arena create\" again to set spawn point one where you are standing!"));
						new BukkitRunnable()
						{
							public void run()
							{
								if (arenaCreation.containsKey(p.getUniqueId().toString()))
								{
									if(arenaCreation.get(p.getUniqueId().toString()) == null)
									{
										arenaCreation.remove(p.getUniqueId().toString());
										p.sendMessage(MainUtils.chatColor("&cArenaCreation! &7&oYou took too long, progress has been cleared. Type /arena create to restart!"));
									}
								}
							}
						}.runTaskLater(Main.getInstance(),20*60);
						
						return true;
					}
				}
			} else {
				//open arena gui
			}
		} else {
			p.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou don't have permission to use &c&o/" + cmd.getName()));
			return true;
		}
		return true;
	}

}
