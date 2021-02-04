package me.BoyJamal.practice.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.BoyJamal.practice.Main;
import me.BoyJamal.practice.listeners.ArenaListeners;
import me.BoyJamal.practice.listeners.ItemListeners;

public class MainUtils {

	public static String chatColor(String message)
	{
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public static void onPlayerJoin(Player p)
	{
		p.teleport(StorageManager.getSpawnLoc());
		MainUtils.addDefaultItems(p);
		
		int id = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new ScoreboardUtil(p), 0, 60).getTaskId();
		ItemListeners.activeScoreboards.put(p.getUniqueId().toString(), id);
	}
	
	public static void completedMessage(Player winner, Player loser, boolean ranked, int elo)
	{
		winner.sendMessage(MainUtils.chatColor("&a&lCongrats!"));
		winner.sendMessage(MainUtils.chatColor("&fYou have defeated &a" + loser.getName()));
		if (ranked)
		{
			//send elo difference
		}
		winner.sendMessage("");
		winner.sendMessage(MainUtils.chatColor("&7&o(You will be teleported back to spawn momentarily"));
	
		loser.sendMessage(MainUtils.chatColor("&c&lSorry!"));
		loser.sendMessage(MainUtils.chatColor("&fYou have been defeated by &c" + loser.getName()));
		if (ranked)
		{
			//send elo difference
		}
		loser.sendMessage("");
		loser.sendMessage(MainUtils.chatColor("&7&o(You will be teleported back to spawn momentarily"));
	}
	
	public static List<String> listColor(List<String> message)
	{
		List<String> newLore = new ArrayList<>();
		for (String each : message)
		{
			newLore.add(chatColor(each));
		}
		return newLore;
	}
	
	public static void setQueueItems(Player p, KitUtil kit)
	{
		p.getInventory().clear();
		p.getInventory().setItem(4, queueItem(kit));
	}
	
	public static void addDefaultItems(Player p)
	{
		//clear inv
		p.getInventory().clear();
		
		//add default items
		p.getInventory().setItem(0, secondGlassItem());
		if (DataManagment.activeData.containsKey(p.getUniqueId().toString()))
		{
			PlayerData data = DataManagment.activeData.get(p.getUniqueId().toString());
			if (data.getWins() >= 10)
			{
				p.getInventory().setItem(2, getRankedPractice());
			} else {
				p.getInventory().setItem(2, secondGlassItem());
			}
		} else {
			p.getInventory().setItem(2, secondGlassItem());
		}
		p.getInventory().setItem(1, getUnrankedPractice());
		p.getInventory().setItem(3, secondGlassItem());
		p.getInventory().setItem(4, getStats(p));
		p.getInventory().setItem(5, secondGlassItem());
		p.getInventory().setItem(6, getKits());
		p.getInventory().setItem(7, secondGlassItem());
		
		if (PartyMethods.getParty(p) != null)
		{
			p.getInventory().setItem(8, basicPartyItem());
		} else {
			p.getInventory().setItem(8, createPartyItem());
		}
	}
	
	public static ItemStack basicPartyItem()
	{
		ItemStack item = new ItemStack(Material.MAP);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(MainUtils.chatColor("&eParty Features"));
		List<String> lore = new ArrayList<>();
		lore.add(MainUtils.chatColor(""));
		lore.add(MainUtils.chatColor("&7&oClick to open the party features menu!"));
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack createPartyItem()
	{
		ItemStack item = new ItemStack(Material.EMPTY_MAP);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(MainUtils.chatColor("&eCreate Party"));
		List<String> lore = new ArrayList<>();
		lore.add(MainUtils.chatColor(""));
		lore.add(MainUtils.chatColor("&7&oClick to create a party!"));
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static List<EnchantmentUtil> getEnchants(List<String> message)
	{
		List<EnchantmentUtil> list = new ArrayList<>();
		for (String each : message)
		{
			try {
				String[] split = each.split(";");
				EnchantmentUtil enchant = new EnchantmentUtil(Enchantment.getByName(split[0].toUpperCase()),Integer.valueOf(split[1]));
				list.add(enchant);
			} catch (Exception exc) {
				continue;
			}
		}
		return list;
	}
	
	public static String convertTime(int count)
	{
		return String.valueOf(count/60) + ":" + Integer.valueOf(count%60);
	}
	
	public static Scoreboard lobbyScoreboard(Player p)
	{
		ScoreboardManager mang = Bukkit.getScoreboardManager();
		Scoreboard board = mang.getNewScoreboard();
		
		Objective obj = board.registerNewObjective("Title", "Title");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(MainUtils.chatColor("&c&lHoshi&f&lMC"));
		
		Score line1 = obj.getScore(MainUtils.chatColor("&r&f&m----------------"));
		line1.setScore(8);
		
		Score line3 = obj.getScore(MainUtils.chatColor("&cOnline: &f" + Bukkit.getServer().getOnlinePlayers().size()));
		line3.setScore(7);
		
		Score line2 = obj.getScore(MainUtils.chatColor("&cIn Fights: &f" + ArenaListeners.activeBattles.size()*2));
		line2.setScore(6);
		
		Score line4 = obj.getScore(MainUtils.chatColor(""));
		line4.setScore(5);
		
		Score ping = obj.getScore(MainUtils.chatColor("&cPing: &f" + ((CraftPlayer)p).getHandle().ping));
		ping.setScore(4);
		
		Score line5 = obj.getScore(MainUtils.chatColor("&cplay.hoshimc.eu"));
		line5.setScore(2);
		
		Score blank = obj.getScore("");
		blank.setScore(3);
		
		Score lines2 = obj.getScore(MainUtils.chatColor("&f&m----------------"));
		lines2.setScore(1);
		
		return board;
	}
	
	public static ItemStack queueItem(KitUtil kit)
	{
		ItemStack item = new ItemStack(Material.WOOL,1,(short)14);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(chatColor("&cLeave Queue &7&o(" + kit.getName() + ")"));
		List<String> lore = new ArrayList<>();
		lore.add(MainUtils.chatColor("&7&oClick to leave the queue!"));
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack returnItem()
	{
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(chatColor("&c&lReturn"));
		List<String> lore = new ArrayList<>();
		lore.add(MainUtils.chatColor("&7&oClick to return to previous menu!"));
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack whiteGlass()
	{
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)0);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(" ");
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack glassItem()
	{
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(" ");
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack secondGlassItem()
	{
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)8);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(" ");
		item.setItemMeta(im);
		return item;
	}
	
	public static int getSlots(int amount)
	{
		if (amount >= 0 && amount < 9) {
			return 18;
		} else if (amount >= 9 && amount < 18) {
			return 27;
		} else if (amount >= 18 && amount < 27) {
			return 36;
		} else if (amount >= 27 && amount < 36) {
			return 45;
		} else {
			return 54;
		}
	}
	
	public static String getItemType(ItemStack item, Player p)
	{
		if (item.isSimilar(createPartyItem())) {
			return "createparty";
		} else if (item.isSimilar(basicPartyItem())) {
			return "partyfeature";
		} else if (item.isSimilar(getUnrankedPractice())) {
			return "unranked";
		} else if (item.isSimilar(getRankedPractice())) {
			return "ranked";
		} else if (queueItemSimilar(item)) {
			return "queue";
		} else if (item.isSimilar(getKits())) {
			return "kits";
		} else if (item.isSimilar(getStats(p))) {
			return "stats";
		} else {
			return null;
		}
	}
	
	public static ArenaUtil getArena(int id)
	{
		for (ArenaUtil each : StorageManager.loadedArenas)
		{
			if (each.getID() == id)
			{
				return each;
			}
		}
		return null;
	}
	
	public static int getNextOpenArena()
	{
		for (ArenaUtil each : StorageManager.loadedArenas)
		{
			if (!(each.inUse()))
			{
				each.setUse(true);
				return each.getID();
			}
		}
		return -1;
	}
	
	public static List<QueueUtil> checkQueueSize(KitUtil util, String ranked)
	{
		QueueUtil firstFound = null;
		for (QueueUtil each : QueueUtil.queueList)
		{
			if (each.getKit().getName().equalsIgnoreCase(util.getName()) && each.getType().equalsIgnoreCase(ranked))
			{
				if (firstFound != null)
				{
					List<QueueUtil> players = new ArrayList<>();
					players.add(firstFound);
					players.add(each);
					return players;
				} else {
					firstFound = each;
					continue;
				}
			}
		}
		return null;
	}
	
	public static ItemStack getKits()
	{
		ItemStack item = new ItemStack(Material.GOLD_NUGGET);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(chatColor("&eKit Viewer"));
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(chatColor("&7&oRight Click to view active kits!"));
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(im);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		return item;
	}
	
	public static ItemStack getUnrankedPractice()
	{
		ItemStack item = new ItemStack(Material.WOOD_SWORD);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(chatColor("&dUnranked Practice"));
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(chatColor("&7&oRight Click to join queue for practice!"));
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(im);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		return item;
	}
	
	public static ItemStack getRankedPractice()
	{
		ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(chatColor("&dRanked Matches"));
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(chatColor("&7&oRight Click to join queue for ranked matches!"));
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(im);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		return item;
	}
	
	public static boolean queueItemSimilar(ItemStack item)
	{
		if (item.getType() == Material.WOOL)
		{
			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
			{
				 if (item.getItemMeta().getDisplayName().startsWith(MainUtils.chatColor("&cLeave Queue")))
				 {
					 return true;
				 } else {
					 return false;
				 }
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static ItemStack getStats(Player p)
	{
		ItemStack item = new ItemStack(Material.SKULL_ITEM,1,(short)3);
		SkullMeta im = (SkullMeta)item.getItemMeta();
		im.setOwner(p.getName());
		im.setDisplayName(chatColor("&bStatistics"));
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(chatColor("&7&oRight Click to view your stats!"));
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(im);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		return item;
	}
	
	public static Inventory getStatsInventory(OfflinePlayer p)
	{
		Inventory inv = Bukkit.createInventory(null, 45, MainUtils.chatColor("&f" + p.getName() + "'s Stats"));
		PlayerData data = DataManagment.activeData.get(p.getUniqueId().toString());
		
		inv.setItem(0, glassItem());
		inv.setItem(1, whiteGlass());
		inv.setItem(2, glassItem());
		inv.setItem(3, glassItem());
		inv.setItem(4, glassItem());
		inv.setItem(5, glassItem());
		inv.setItem(6, glassItem());
		inv.setItem(7, whiteGlass());
		inv.setItem(8, glassItem());
		
		inv.setItem(9, whiteGlass());
		inv.setItem(10, getKillsItem(data));
		inv.setItem(11, glassItem());
		inv.setItem(12, getDamageItem(data));
		inv.setItem(13, getWinsItem(data));
		inv.setItem(14, getHitsItem(data));
		inv.setItem(15, glassItem());
		inv.setItem(16, getEloItem(data));
		inv.setItem(17, whiteGlass());
		
		inv.setItem(18, glassItem());
		inv.setItem(19, whiteGlass());
		inv.setItem(20, glassItem());
		inv.setItem(21, glassItem());
		inv.setItem(22, glassItem());
		inv.setItem(23, glassItem());
		inv.setItem(24, glassItem());
		inv.setItem(25, whiteGlass());
		inv.setItem(26, glassItem());
	
		inv.setItem(27, glassItem());
		inv.setItem(28, glassItem());
		inv.setItem(29, whiteGlass());
		inv.setItem(30, whiteGlass());
		inv.setItem(31, returnItem());
		inv.setItem(32, whiteGlass());
		inv.setItem(33, whiteGlass());
		inv.setItem(34, glassItem());
		inv.setItem(35, glassItem());
		
		for (int i = 36; i <= 44; i++)
		{
			inv.setItem(i, glassItem());
		}
		return inv;
	}
	
	public static Inventory getStatsInventory(Player p)
	{
		Inventory inv = Bukkit.createInventory(null, 45, MainUtils.chatColor("&f" + p.getName() + "'s Stats"));
		if (!(DataManagment.activeData.containsKey(p.getUniqueId().toString())))
		{
			PlayerData data = new PlayerData(p.getUniqueId().toString());
			DataManagment.activeData.put(p.getUniqueId().toString(), data);
		}
		
		PlayerData data = DataManagment.activeData.get(p.getUniqueId().toString());
		
		inv.setItem(0, glassItem());
		inv.setItem(1, whiteGlass());
		inv.setItem(2, glassItem());
		inv.setItem(3, glassItem());
		inv.setItem(4, glassItem());
		inv.setItem(5, glassItem());
		inv.setItem(6, glassItem());
		inv.setItem(7, whiteGlass());
		inv.setItem(8, glassItem());
		
		inv.setItem(9, whiteGlass());
		inv.setItem(10, getKillsItem(data));
		inv.setItem(11, glassItem());
		inv.setItem(12, getDamageItem(data));
		inv.setItem(13, getWinsItem(data));
		inv.setItem(14, getHitsItem(data));
		inv.setItem(15, glassItem());
		inv.setItem(16, getEloItem(data));
		inv.setItem(17, whiteGlass());
		
		inv.setItem(18, glassItem());
		inv.setItem(19, whiteGlass());
		inv.setItem(20, glassItem());
		inv.setItem(21, glassItem());
		inv.setItem(22, glassItem());
		inv.setItem(23, glassItem());
		inv.setItem(24, glassItem());
		inv.setItem(25, whiteGlass());
		inv.setItem(26, glassItem());
	
		inv.setItem(27, glassItem());
		inv.setItem(28, glassItem());
		inv.setItem(29, whiteGlass());
		inv.setItem(30, whiteGlass());
		inv.setItem(31, returnItem());
		inv.setItem(32, whiteGlass());
		inv.setItem(33, whiteGlass());
		inv.setItem(34, glassItem());
		inv.setItem(35, glassItem());
		
		for (int i = 36; i <= 44; i++)
		{
			if (i == 40)
			{
				inv.setItem(i, whiteGlass());
			} else {
				inv.setItem(i, glassItem());
			}
		}
		return inv;
	}
	
	/*public static ItemStack getPrivateOnes()
	{
		ItemStack item = new ItemStack(Material);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(MainUtils.chatColor("&c1v1 Queue"));
		List<String> lore = new ArrayList<>();
		lore.add(MainUtils.chatColor("&r"));
		lore.add(MainUtils.chatColor("&7&oClick to join 1v1 party queue!"));
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.);
	}
	
	public static ItemStack getPrivateTwos()
	{
		
	}
	
	public static ItemStack getPrivateThrees()
	{
		
	}*/
	
	
	public static ItemStack getKillsItem(PlayerData data)
	{
		ItemStack item = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(MainUtils.chatColor("&c&lCombat Log"));
		List<String> lore = new ArrayList<>();
		
		lore.add(MainUtils.chatColor("&c&l* &fKills: &7" + data.getKills()));
		lore.add(MainUtils.chatColor("&c&l* &fDeaths: &7" + data.getDeaths()));
		lore.add(MainUtils.chatColor("&c&l* &fKill/Death Ratio: &7" + data.getKillDeathRatio()));
		
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_DESTROYS);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack getWinsItem(PlayerData data)
	{
		ItemStack item = new ItemStack(Material.EMPTY_MAP);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(MainUtils.chatColor("&c&lCareer Record"));
		List<String> lore = new ArrayList<>();
		
		lore.add(MainUtils.chatColor("&c&l* &fWins: &7" + data.getWins()));
		lore.add(MainUtils.chatColor("&c&l* &fLosses: &7" + data.getLosses()));
		lore.add(MainUtils.chatColor("&c&l* &fWin/Loss Ratio: &7" + data.getWinLossRatio()));
		
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_DESTROYS);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack nextPage()
	{
		ItemStack item = new ItemStack(Material.SKULL_ITEM,1,(short)3);
		SkullMeta meta = (SkullMeta)item.getItemMeta();
		meta.setOwner("MHF_ArrowRight");
		meta.setDisplayName(MainUtils.chatColor("&a&lNext Page"));
		List<String> lore = new ArrayList<>();
		lore.add(MainUtils.chatColor("&a&l* &7Click to go to next page!"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack lastPage()
	{
		ItemStack item = new ItemStack(Material.SKULL_ITEM,1,(short)3);
		SkullMeta meta = (SkullMeta)item.getItemMeta();
		meta.setOwner("MHF_ArrowLeft");
		meta.setDisplayName(MainUtils.chatColor("&a&lPrevious Page"));
		List<String> lore = new ArrayList<>();
		lore.add(MainUtils.chatColor("&a&l* &7Click to go back a page!"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack lobbyItem()
	{
		ItemStack item = new ItemStack(Material.EYE_OF_ENDER);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(MainUtils.chatColor("&c&lLobby"));
		List<String> lore = new ArrayList<>();
		lore.add(MainUtils.chatColor("&7&oClick to teleport to lobby!"));
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}
	
	public static Inventory arenaGui(int page)
	{
		Inventory inv = Bukkit.createInventory(null, 45,MainUtils.chatColor("&fLoaded Arenas &7&o(Page " + page + ")"));
		
		if (!(StorageManager.loadedArenas.size() > (page-1)*36))
		{
			return null;
		}
		
		for (int i = 1; i<=36;i++)
		{
			if (page > 1)
			{
				if (StorageManager.loadedArenas.size() >= i+(36*page))
				{
					inv.setItem(i-1, StorageManager.loadedArenas.get((i-1)+(36*page)).getDisplay());
				}
			} else {
				if (StorageManager.loadedArenas.size() >= i)
				{
					inv.setItem(i-1, StorageManager.loadedArenas.get(i-1).getDisplay());
				}
			}
		}
		
		if (page > 1)
		{
			inv.setItem(36, lastPage());
		} else {
			inv.setItem(36, glassItem());
		}
		
		for (int i = 37; i<=43;i++)
		{
			if (i == 39)
			{
				inv.setItem(i, lobbyItem());
			} else if (i == 41) {
				inv.setItem(i, returnItem());
			} else {
				inv.setItem(i, glassItem());
			}
		}
		
		if (StorageManager.loadedArenas.size() >= 36*(page+1))
		{
			inv.setItem(44, nextPage());
		} else {
			inv.setItem(44, glassItem());
		}
		return inv;
	}
	
	public static ItemStack getEloItem(PlayerData data)
	{
		ItemStack item = new ItemStack(Material.INK_SACK,1,(short)10);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(MainUtils.chatColor("&c&lCurrent Elo: &f" + data.getElo()));
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_DESTROYS);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack getDamageItem(PlayerData data)
	{
		ItemStack item = new ItemStack(Material.POTION,1,(short)8261);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(MainUtils.chatColor("&c&lDamage Dealt: &f" + data.getDamageDealt()));
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_DESTROYS);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(im);
		return item;
	}
	
	public static ItemStack getHitsItem(PlayerData data)
	{
		ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(MainUtils.chatColor("&c&lHits Landed: &f" + data.getHitsLanded()));
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_DESTROYS);
		item.setItemMeta(im);
		return item;
	}
	
}
