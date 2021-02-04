package me.BoyJamal.practice.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PartyUtil {

	private String type;
	private Player leader;
	private List<Player> activePlayers;
	private String password;
	private List<QueueUtil> partyBattles;
	//queues for dif battles
	
	public PartyUtil(Player leader)
	{
		this.type = "invite";
		this.leader = leader;
		this.password = "";
		this.partyBattles = new ArrayList<>();
		
		this.activePlayers = new ArrayList<>();
		activePlayers.add(leader);
	}
	
	public List<QueueUtil> getPartyBattles()
	{
		return partyBattles;
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public Player getLeader()
	{
		return leader;
	}
	
	public Inventory getPartyBattleGui()
	{
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER,MainUtils.chatColor("&f" + leader.getName() + "'s Party"));
		
		
		
		return inv;
	}
	
	public void sendInfo(Player sender)
	{
		sender.sendMessage("");
		sender.sendMessage(MainUtils.chatColor("&c&lHoshi&f&lMC"));
		sender.sendMessage(MainUtils.chatColor(""));
		sender.sendMessage(MainUtils.chatColor("&r  &fLeader: &c" + leader.getName()));
		sender.sendMessage(MainUtils.chatColor("&r  &fMembers &c(" +  activePlayers.size() + ")&f: &7 " +  arrayToPlayers()));
		
		PartyUtil party = PartyMethods.getParty(sender);
		if (party != null)
		{
			if (party.getLeader().getUniqueId().equals(this.getLeader().getUniqueId()))
			{
				if (party.getType().equalsIgnoreCase("password"))
				{
					TextComponent mainComponent = new TextComponent(ChatColor.RESET + "  " + ChatColor.WHITE + "Privacy: "
							+ ChatColor.RED + "Password Protected");
					TextComponent subComponent = new TextComponent(" [Hover to see password]");
					subComponent.setColor(ChatColor.WHITE);
					subComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(password).create()));
					mainComponent.addExtra(subComponent);
					sender.spigot().sendMessage(mainComponent);
				} else {
					sender.sendMessage(MainUtils.chatColor("&r  &fPrivacy: &c" + PartyMethods.getDisplayType(this)));
				}
			} else {
				sender.sendMessage(MainUtils.chatColor("&r  &fPrivacy: &c" + PartyMethods.getDisplayType(this)));
			}
		} else {
			sender.sendMessage(MainUtils.chatColor("&r  &fPrivacy: &c" + PartyMethods.getDisplayType(this)));
		}
		
		
		sender.sendMessage("");
	}
	
	private String arrayToPlayers()
	{
		String players = "";
		int count = 0;
		for (Player each : activePlayers)
		{
			players += each.getName();
			if (!(count +1 == activePlayers.size()))
			{
				players += ", ";
			}
			count++;
		}
		return players;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public void setLeader(Player leader)
	{
		this.leader = leader;
	}
	
	public List<Player> getPlayers()
	{
		return activePlayers;
	}
}
