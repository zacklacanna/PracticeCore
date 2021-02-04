package me.BoyJamal.practice.listeners;

import java.util.List;

import org.bukkit.Bukkit;
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
import me.BoyJamal.practice.utils.ArenaUtil;
import me.BoyJamal.practice.utils.KitUtil;
import me.BoyJamal.practice.utils.MainUtils;
import me.BoyJamal.practice.utils.QueueUtil;
import me.BoyJamal.practice.utils.StartBattle;
import me.BoyJamal.practice.utils.StorageManager;

public class ArenaSelectorListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent evt)
	{
		if (!(evt.getWhoClicked() instanceof Player))
		{
			return;
		}
		
		Player p = (Player)evt.getWhoClicked();
		if (evt.getInventory().getName().equalsIgnoreCase(MainUtils.chatColor("&fJoin Unranked Queue:")))
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
				p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 500, 500);
				return;
			} else {
				for (KitUtil each : StorageManager.loadedKits)
				{
					if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(each.getName()))
					{
						int openArena = MainUtils.getNextOpenArena();
						p.closeInventory();
						if (openArena == -1)
						{
							p.sendMessage(MainUtils.chatColor("&a&lSuccess! &7You have been added to the &f" + each.getName() + "&7 Queue!"));
							MainUtils.setQueueItems(p, each);
							QueueUtil.queueList.add(new QueueUtil(p,each,"unranked"));
							return;
						}
						
						ArenaUtil arena = MainUtils.getArena(openArena);
						if (arena == null)
						{
							p.sendMessage(MainUtils.chatColor("&a&lSuccess! &7You have been added to the &f" + each.getName() + "&7 Queue!"));
							MainUtils.setQueueItems(p, each);
							QueueUtil.queueList.add(new QueueUtil(p,each,"unranked"));
							return;
						}
						
						if (MainUtils.checkQueueSize(each,"unranked") != null)
						{
							List<QueueUtil> queuedPlayers = MainUtils.checkQueueSize(each,"unranked");
							
							//check if ranked
							
							StartBattle battle = new StartBattle(arena,queuedPlayers.get(0).getPlayer(),queuedPlayers.get(1).getPlayer(),each,false);
							ArenaListeners.activeBattles.add(battle);
							battle.startBattle();
							
							QueueUtil.queueList.remove(queuedPlayers.get(0));
							QueueUtil.queueList.remove(queuedPlayers.get(1));
							QueueUtil.queueList.add(new QueueUtil(p,each,"unranked"));
							return;
						}
						
						for (QueueUtil queue : QueueUtil.queueList)
						{
							if (queue.getKit().getName().equalsIgnoreCase(each.getName()) && queue.getType().equalsIgnoreCase("unranked"))
							{
								StartBattle battle = new StartBattle(arena,p,queue.getPlayer(),each,false);
								ArenaListeners.activeBattles.add(battle);
								battle.startBattle();
								QueueUtil.queueList.remove(queue);
								return;
							}
						}
						
						p.sendMessage(MainUtils.chatColor("&a&lSuccess! &7You have been added to the &f" + each.getName() + "&7 Queue!"));
						MainUtils.setQueueItems(p, each);
						QueueUtil.queueList.add(new QueueUtil(p,each,"unranked"));
						return;
					}
				}
				
			}
		}
	}

}
