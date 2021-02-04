package me.BoyJamal.practice.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import me.BoyJamal.practice.Main;
import me.BoyJamal.practice.utils.DataManagment;
import me.BoyJamal.practice.utils.MainUtils;
import me.BoyJamal.practice.utils.PartyMethods;
import me.BoyJamal.practice.utils.PlayerData;
import me.BoyJamal.practice.utils.QueueUtil;
import me.BoyJamal.practice.utils.ScoreboardUtil;
import me.BoyJamal.practice.utils.StorageManager;

public class ItemListeners implements Listener {
	
	public static HashMap<String,Integer> activeScoreboards = new HashMap<>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent evt)
	{
		Player p = evt.getPlayer();
		if (StorageManager.getSpawnLoc() != null)
		{
			p.teleport(StorageManager.getSpawnLoc());
		} else {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn");
		}
		MainUtils.addDefaultItems(p);
		
		int id = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new ScoreboardUtil(p), 0, 60).getTaskId();
		activeScoreboards.put(p.getUniqueId().toString(), id);
		return;
	}
	
	@EventHandler
	public void onInvMove(InventoryClickEvent evt)
	{
		if (!(evt.getWhoClicked() instanceof Player))
		{
			return;
		}
		
		Player p = (Player)evt.getWhoClicked();
		if (!(p.isOp()))
		{
			if (p.getWorld().getName().equalsIgnoreCase("spawn"))
			{
				evt.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent evt)
	{
		Player p = evt.getPlayer();
		if (evt.getAction() != Action.RIGHT_CLICK_AIR && evt.getAction() != Action.RIGHT_CLICK_BLOCK &&
				evt.getAction() != Action.LEFT_CLICK_AIR && evt.getAction() != Action.LEFT_CLICK_BLOCK)
		{
			return;
		}
		
		ItemStack inHand = p.getInventory().getItemInHand();
		if (inHand == null)
		{
			return;
		}
		
		String type = MainUtils.getItemType(inHand, p);
		if (type != null)
		{
			switch(type.toLowerCase())
			{
				case("partyfeature"):
					//open party gui
				case("createparty"):
					if (PartyMethods.getParty(p) == null)
					{
						PartyMethods.createParty(p);
						p.getInventory().setItem(8, MainUtils.basicPartyItem());
						return;
					} else {
						p.getInventory().setItem(8,MainUtils.basicPartyItem());
						return;
					}
				case("queue"):
					for (QueueUtil queue : QueueUtil.queueList)
					{
						if (queue.getPlayer().getUniqueId().equals(p.getUniqueId()))
						{
							p.getInventory().clear();
							MainUtils.addDefaultItems(p);
							p.sendMessage(MainUtils.chatColor("&a&lSuccess! &7You have left the queue for &f" + queue.getKit().getName()));
							QueueUtil.queueList.remove(queue);
							return;
						}
					}
				case("ranked"):
					if (DataManagment.activeData.containsKey(p.getUniqueId().toString()))
					{
						PlayerData data = DataManagment.activeData.get(p.getUniqueId().toString());
						if (data.getWins() >= 10)
						{
							//open ranked gui
							p.playSound(p.getLocation(), Sound.CHEST_OPEN, 500, 500);
							return;
						} else {
							p.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must have at least 10 wins to use this!"));
							p.playSound(p.getLocation(), Sound.FIZZ, 500, 500);
							return;
						}
					} else {
						p.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must have at least 10 wins to use this!"));
						p.playSound(p.getLocation(), Sound.FIZZ, 500, 500);
						return;
					}
				case("unranked"):
					p.openInventory(StorageManager.getUnrankedArenaSelector());
					p.playSound(p.getLocation(), Sound.CHEST_OPEN, 500, 500);
					return;
				case("kits"):
					if (StorageManager.getKitGui() != null)
					{
						p.openInventory(StorageManager.getKitGui());
						p.playSound(p.getLocation(), Sound.CHEST_OPEN, 500, 500);
						return;
					} else {
						return;
					}
				case("stats"):
					p.openInventory(MainUtils.getStatsInventory(p));
					p.playSound(p.getLocation(), Sound.CHEST_OPEN, 500, 500);
					return;
				default:
					return;
			}
		}
	}
}
