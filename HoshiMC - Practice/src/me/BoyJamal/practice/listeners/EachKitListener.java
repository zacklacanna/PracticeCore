package me.BoyJamal.practice.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.BoyJamal.practice.Main;
import me.BoyJamal.practice.utils.MainUtils;
import me.BoyJamal.practice.utils.StorageManager;

public class EachKitListener implements Listener {
	
	@EventHandler
	public void onClose(InventoryCloseEvent evt)
	{
		if (!(evt.getPlayer() instanceof Player))
		{
			return;
		}
		
		Player p = (Player)evt.getPlayer();
		if (evt.getInventory().getName().equalsIgnoreCase(MainUtils.chatColor("&fKit Viewer:")))
		{
			new BukkitRunnable()
			{
				public void run()
				{
					p.openInventory(StorageManager.getKitGui());
					p.playSound(p.getLocation(), Sound.CHEST_OPEN, 500, 500);
				}
			}.runTaskLater(Main.getInstance(), 1);
			return;
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
		if (evt.getInventory().getName().equalsIgnoreCase(MainUtils.chatColor("&fKit Viewer:")))
		{
			evt.setCancelled(true);
			ItemStack clicked = evt.getCurrentItem();
			if (clicked == null || clicked.getType() == Material.AIR)
			{
				return;
			}
			
			if (clicked.isSimilar(MainUtils.returnItem()))
			{
				p.closeInventory();
				new BukkitRunnable()
				{
					public void run()
					{
						p.openInventory(StorageManager.getKitGui());
						p.playSound(p.getLocation(), Sound.CHEST_OPEN, 500, 500);
					}
				}.runTaskLater(Main.getInstance(), 1);
				return;
			} else {
				return;
			}
		}
	}

}
