package me.BoyJamal.practice.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.BoyJamal.practice.Main;
import me.BoyJamal.practice.utils.KitUtil;
import me.BoyJamal.practice.utils.MainUtils;
import me.BoyJamal.practice.utils.StorageManager;

public class MainKitListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent evt)
	{
		if (!(evt.getWhoClicked() instanceof Player))
		{
			return;
		}
		
		if (!(ChatColor.stripColor(evt.getInventory().getName()).equalsIgnoreCase(ChatColor.stripColor(StorageManager.getKitGui().getName()))))
		{
			return;
		}
		
		evt.setCancelled(true);
		Player p = (Player)evt.getWhoClicked();
		ItemStack clickedItem = evt.getCurrentItem();
		if (clickedItem == null || clickedItem.getType() == Material.AIR)
		{
			return;
		}
		
		if (clickedItem.isSimilar(MainUtils.returnItem()))
		{
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 500, 500);
			return;
		} else {
			for (KitUtil each : StorageManager.loadedKits)
			{
				if (!(clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()))
				{
					continue;
				} else {
					if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase(each.getName()))
					{
						p.closeInventory();
						new BukkitRunnable()
						{
							public void run()
							{
								p.openInventory(each.getInv());
								p.playSound(p.getLocation(), Sound.NOTE_PLING, 500, 500);
							}
						}.runTaskLater(Main.getInstance(), 1);
						break;
					}
				}
			}
		}
	}

}
