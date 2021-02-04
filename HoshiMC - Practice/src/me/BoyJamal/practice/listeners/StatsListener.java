package me.BoyJamal.practice.listeners;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import me.BoyJamal.practice.utils.DataManagment;
import me.BoyJamal.practice.utils.MainUtils;
import me.BoyJamal.practice.utils.PlayerData;

public class StatsListener implements Listener {
	
	public static HashMap<String,String> offlineStats = new HashMap<>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent evt)
	{
		Player p = evt.getPlayer();
		if (!(DataManagment.activeData.containsKey(p.getUniqueId().toString())))
		{
			PlayerData data = new PlayerData(p.getUniqueId().toString());
			DataManagment.activeData.put(p.getUniqueId().toString(), data);
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent evt)
	{
		if (!(evt.getPlayer() instanceof Player))
		{
			return;
		}
		
		Player p = (Player)evt.getPlayer();
		if (offlineStats.containsKey(p.getUniqueId().toString()))
		{
			offlineStats.remove(p.getUniqueId().toString());
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent evt)
	{
		if (!(evt.getWhoClicked() instanceof Player))
		{
			return;
		}
		
		Player p = (Player)evt.getWhoClicked();
		if (offlineStats.containsKey(p.getUniqueId().toString()))
		{
			String playerName = offlineStats.get(p.getUniqueId().toString());
			if (evt.getInventory().getName().equalsIgnoreCase(MainUtils.chatColor("&f" + playerName + "'s Stats")))
			{
				evt.setCancelled(true);
				ItemStack clicked = evt.getCurrentItem();
				if (clicked != null && clicked.getType() != Material.AIR)
				{
					if (clicked.isSimilar(MainUtils.returnItem()))
					{
						p.closeInventory();
						p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 500, 500);
					}
				}
				return;
			}
		}
		
		if (evt.getInventory().getName().equalsIgnoreCase(MainUtils.chatColor("&f" + p.getName() + "'s Stats")))
		{
			evt.setCancelled(true);
			ItemStack clicked = evt.getCurrentItem();
			if (clicked != null && clicked.getType() != Material.AIR)
			{
				if (clicked.isSimilar(MainUtils.returnItem()))
				{
					p.closeInventory();
					p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 500, 500);
				}
			}
		}
	}

}
