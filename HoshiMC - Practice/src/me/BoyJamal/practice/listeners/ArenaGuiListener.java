package me.BoyJamal.practice.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.BoyJamal.practice.Main;
import me.BoyJamal.practice.utils.ArenaUtil;
import me.BoyJamal.practice.utils.MainUtils;
import me.BoyJamal.practice.utils.StorageManager;
import net.md_5.bungee.api.ChatColor;

public class ArenaGuiListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent evt)
	{
		if (!(evt.getWhoClicked() instanceof Player))
		{
			return;
		}
		
		Player p = (Player)evt.getWhoClicked();
		if (evt.getInventory().getName().startsWith(MainUtils.chatColor("&fLoaded Arenas")))
		{
			evt.setCancelled(true);
			String[] each = ChatColor.stripColor(evt.getInventory().getName()).split(" ");
			int page;
			try {
				page = Integer.parseInt(ChatColor.stripColor(each[3]).replaceAll("\\)", ""));
			} catch (Exception exc) {
				return;
			}
			
			ItemStack item = evt.getCurrentItem();
			if (item == null || item.getType() == Material.AIR)
			{
				return;
			}
			
			if (item.isSimilar(MainUtils.returnItem()))
			{
				p.closeInventory();
				p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 500, 500);
				return;
			} else if (item.isSimilar(MainUtils.nextPage())) {
				p.closeInventory();
				new BukkitRunnable()
				{
					public void run()
					{
						p.openInventory(MainUtils.arenaGui(page+1));
					}
				}.runTaskLater(Main.getInstance(), 2);
			} else if (item.isSimilar(MainUtils.lobbyItem())) {
				p.closeInventory();
				p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 500, 500);
				p.teleport(StorageManager.getSpawnLoc());
				return;
			} else if (item.isSimilar(MainUtils.lastPage())) {
				p.closeInventory();
				new BukkitRunnable()
				{
					public void run()
					{
						p.openInventory(MainUtils.arenaGui(page-1));
					}
				}.runTaskLater(Main.getInstance(), 2);
			} else {
				if (item.getType() == Material.PISTON_BASE)
				{
					if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
					{
						String[] spaces = item.getItemMeta().getDisplayName().split(" ");
						int arenaID;
						try {
							arenaID = Integer.parseInt(ChatColor.stripColor(spaces[1].replaceAll("#", "")));
						} catch (Exception exc) {
							return;
						}
						
						for (ArenaUtil arena : StorageManager.loadedArenas)
						{
							if (arena.getID() == arenaID)
							{
								p.closeInventory();
								p.teleport(arena.getLocations().getPlayerOneSpawn());
								p.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYou have been teleported to Arena #" + arena.getID()));
								return;
							}
						}
						
						p.sendMessage(MainUtils.chatColor("&c&lError! &7&oCould not teleport you to arena. Message an admin!"));
						return;
					}
				}
			}
		}
		
	}

}
