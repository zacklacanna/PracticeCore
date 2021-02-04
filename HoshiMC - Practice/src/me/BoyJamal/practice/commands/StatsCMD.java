package me.BoyJamal.practice.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.BoyJamal.practice.listeners.StatsListener;
import me.BoyJamal.practice.utils.DataManagment;
import me.BoyJamal.practice.utils.MainUtils;

public class StatsCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
		{
			return true;
		}
		
		Player p = (Player)sender;
		if (args.length == 0)
		{
			p.openInventory(MainUtils.getStatsInventory(p));
			p.playSound(p.getLocation(), Sound.CHEST_OPEN, 500, 500);
			return true;
		} else if (args.length == 1) {
			OfflinePlayer player;
			try { 
				player = Bukkit.getOfflinePlayer(args[0]);
			} catch (Exception exc) {
				return true;
			}
			
			if (DataManagment.activeData.containsKey(player.getUniqueId().toString()))
			{
				p.openInventory(MainUtils.getStatsInventory(player));
				p.playSound(p.getLocation(), Sound.CHEST_OPEN, 500, 500);
				StatsListener.offlineStats.put(p.getUniqueId().toString(), args[0]);
				return true;
			} else {
				p.sendMessage(MainUtils.chatColor("&c&lError! &7&oCould not find stats for &f&o" + args[0]));
				return true;
			}
		} else {
			p.sendMessage(MainUtils.chatColor("&c&lError! &7Usage: &7&o/stats {name}"));
		}
		return true;
	}

}
