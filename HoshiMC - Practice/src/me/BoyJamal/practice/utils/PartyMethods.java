package me.BoyJamal.practice.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.BoyJamal.practice.Main;

public class PartyMethods {

	public static List<PartyUtil> activeParties = new ArrayList<>();
	public static HashMap<UUID,Long> inviteCooldown = new HashMap<>();
	public static HashMap<UUID,PartyUtil> pendingInvites = new HashMap<>();
	
	public static PartyUtil getParty(Player p)
	{
		for (PartyUtil each : activeParties)
		{
			for (Player activePlayers : each.getPlayers())
			{
				if (activePlayers.getUniqueId().equals(p.getUniqueId()))
				{
					return each;
				}
			}
		}
		return null;
	}
	
	public static String getDisplayType(PartyUtil util)
	{
		switch(util.getType())
		{
			case("open"):
				return "Open";
			case("invite"):
				return "Invite-Only";
			case("closed"):
				return "Closed";
			case("password"):
				return "Password Protected";
			default:
				return "Invite-Only";
		}
	}
	
	public static void showPlayer(Player p, PartyUtil util)
	{
		for (Player each : util.getPlayers())
		{
			Main.getInstance().getHider().showEntity(each, p);
			Main.getInstance().getHider().showEntity(p, each);
		}
	}
	
	public static void hidePlayer(Player p, PartyUtil util)
	{
		for (Player each : util.getPlayers())
		{
			Main.getInstance().getHider().hideEntity(each, p);
			Main.getInstance().getHider().hideEntity(p, each);
		}
	}
	
	public static void resetVisibility(PartyUtil util)
	{
		for (Player each : util.getPlayers())
		{
			for (Player set : util.getPlayers())
			{
				Main.getInstance().getHider().hideEntity(each, set);
			}
		}
	}
	
	public static void setPartyInventory(Player p)
	{
		p.getInventory().clear();
		
	}
	
	public static void disbandParty(Player p)
	{
		PartyUtil party = getParty(p);
		if (party != null)
		{
			if (party.getLeader().getUniqueId().equals(p.getUniqueId()))
			{
				resetVisibility(party);
				activeParties.remove(party);
				for (Player each : party.getPlayers())
				{
					each.sendMessage(MainUtils.chatColor("&c&lNotice! &7The leader has disbanded your party!"));
				}
			} else {
				p.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be the leader of the party to do this!"));
				return;
			}
		} else {
			p.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be in a party to use this!"));
			return;
		}
	}
	
	public static void inviteParty(Player leader, String user)
	{
		PartyUtil party = getParty(leader);
		Player invite;
		try {
			invite = Bukkit.getPlayer(user);
		} catch (Exception exc) {
			leader.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + user + "&7&o is not online!"));
			return;
		}
		
		if (leader.getName().equalsIgnoreCase(invite.getName()))
		{
			leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou can't invite yourself!"));
			return;
		}
		
		if (getParty(invite) != null)
		{
			leader.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + invite.getName() + " &7&ois already in a party!"));
			return;
		}
		
		if (party != null)
		{
			if (!(party.getType().equalsIgnoreCase("invite")))
			{
				leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYour party is currently in &f&o" + getDisplayType(party) + "&7&o mode!"));
				return;
			}
			
			if (party.getPlayers().size() >= 6)
			{
				leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYour party is already full!"));
				return;
			}
			
			if (party.getLeader().getUniqueId().equals(leader.getUniqueId()))
			{
				if (pendingInvites.containsKey(invite.getUniqueId()))
				{
					leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou have already invited &f&o" + invite.getName()));
					return;
				} else {
					pendingInvites.put(invite.getUniqueId(), party);
					leader.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYou have invited &f&o" + invite.getName() + "&7&o to the party!"));
					invite.sendMessage(MainUtils.chatColor("&a&lNotice! &7&oYou have been invited to &f&o" + leader.getName() + "'s &7&oParty!"));
					invite.sendMessage(MainUtils.chatColor("&7&oHint: Use &f&o/party accept " + leader.getName() + " &7&oto join! Expires in 1 minute"));
					
					new BukkitRunnable()
					{
						public void run()
						{
							if (pendingInvites.containsKey(invite.getUniqueId()))
							{
								pendingInvites.remove(invite.getUniqueId());
								invite.sendMessage(MainUtils.chatColor("&a&lNotice! &7&oYour invite to &f&o" + leader.getName() + "'s &7&oParty has expired!"));
								return;
							} else {
								return;
							}
						}
					}.runTaskLater(Main.getInstance(), 20*60);
				}
			} else {
				leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be the leader of the party to do this!"));
				return;
			}
		} else {
			leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be in a party to use this!"));
			return;
		}
	}
	
	public static void joinParty(Player user, String target, String password)
	{
		Player targetPlayer;
		try {
			targetPlayer = Bukkit.getPlayer(target);
		} catch (Exception exc) {
			user.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + target + " &7&ois not online!"));
			return;
		}
		
		if (user.getName().equalsIgnoreCase(targetPlayer.getName()))
		{
			user.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou can't join yourself!"));
			return;
		}
		
		PartyUtil userParty = getParty(user);
		PartyUtil targetParty = getParty(targetPlayer);
		
		if (userParty == null)
		{
			if (targetParty != null)
			{
				if (targetParty.getPlayers().size() >= 6)
				{
					user.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + target + "'s &7&oparty is already full!"));
					return;
				}
				
				if (targetParty.getType().equalsIgnoreCase("password"))
				{
					if (targetParty.getPassword().equalsIgnoreCase(password))
					{
						//set inventory
						for (Player  each : targetParty.getPlayers())
						{
							each.sendMessage(MainUtils.chatColor("&a&lNotice! &f&o" + user.getName() + " &7&o has joined your party!"));
						}
						targetParty.getPlayers().add(user);
						showPlayer(user,targetParty);
						user.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYou have joined the party!"));
						return;
					} else {
						user.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou entered an incorrect password!"));
						return;
					}
				} else {
					user.sendMessage(MainUtils.chatColor("&c&lError! &7&oParty is not in password mode!"));
					return;
				}
			} else {
				user.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + target + "&7&o is not in a party!"));
				return;
			}
		} else {
			user.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must leave your party first!"));
			return;
		}
	}
	
	public static void createParty(Player leader)
	{
		if (getParty(leader) != null)
		{
			leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must leave your current party first!"));
			return;
		}
		
		PartyUtil newParty = new PartyUtil(leader);
		activeParties.add(newParty);
		leader.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYou have created a new party!"));
		return;
	}
	
	public static void transferLeadership(Player leader, String user)
	{
		PartyUtil party = getParty(leader);
		Player newLeader;
		try {
			newLeader = Bukkit.getPlayer(user);
		} catch (Exception exc) {
			leader.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + user + "&7&o is not online!"));
			return;
		}
		
		if (newLeader.getName().equalsIgnoreCase(leader.getName()))
		{
			leader.sendMessage(MainUtils.chatColor("&c&lError! &f&oYou are already the party leader!"));
			return;
		}
		
		PartyUtil leaderParty = getParty(newLeader);
		if (party != null)
		{
			if (party.getLeader().getUniqueId().equals(leader.getUniqueId()))
			{
				if (leaderParty == null || (!(leaderParty.getLeader().getUniqueId().equals(party.getLeader().getUniqueId()))))
				{
					leader.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + newLeader.getName() + " &7&ois not in your party!"));
					return;
				}
				
				party.setLeader(newLeader);
				leader.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYou have given &f&o" + newLeader.getName() + "&7&o leadership of the party!"));
				newLeader.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYou have been given leader of party by &f&o" + leader.getName()));
				return;
			} else {
				leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be the leader of the party to do this!"));
				return;
			}
		} else {
			leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be in a party to use this!"));
			return;
		}
	}
	
	public static void getInfo(Player user)
	{
		PartyUtil party = getParty(user);
		if (party != null)
		{
			party.sendInfo(user);
			return;
		} else {
			user.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be in a party!"));
			return;
		}
	}
	
	public static void getInfo(Player user, String check)
	{
		Player checkedUser;
		try {
			checkedUser = Bukkit.getPlayer(check);
		} catch (Exception exc) {
			user.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + check + " &7&ois not online!"));
			return;
		}
		
		PartyUtil party = getParty(checkedUser);
		if (party != null)
		{
			party.sendInfo(user);
			return;
		} else {
			user.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + check + " &7&ois not in a party!"));
			return;
		}
	}
	
	public static void setPassword(Player leader, String password)
	{
		PartyUtil party = getParty(leader);
		if (party != null)
		{
			if (party.getLeader().getUniqueId().equals(leader.getUniqueId()))
			{
				party.setPassword(password);
				leader.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYour party is now password protected!"));
				return;
			}
		}
	}
	
	public static void setPartyType(Player leader, String type)
	{
		PartyUtil party = getParty(leader);
		if (party != null)
		{
			if (party.getLeader().getUniqueId().equals(leader.getUniqueId()))
			{
				if (type.equalsIgnoreCase("closed"))
				{
					party.setType("closed");
					party.setPassword("");
					leader.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYour party is now closed!"));
					return;
				} else if (type.equalsIgnoreCase("open")) {
					party.setType("open");
					party.setPassword("");
					leader.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYour party is now open!"));
					return;
				} else if (type.equalsIgnoreCase("password")) {
					party.setType("password");
					return;
				} else {
					return;
				}
			} else {
				leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be the leader of the party to do this!"));
				return;
			}
		} else {
			leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be in a party to use this!"));
			return;
		}
	}
	
	public static void kickPlayer(Player leader, String user)
	{
		PartyUtil party = getParty(leader);
		Player kicked;
		try {
			kicked = Bukkit.getPlayer(user);
		} catch (Exception exc) {
			leader.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + user + "&7&o is not online!"));
			return;
		}
		
		if (kicked.getName().equalsIgnoreCase(leader.getName()))
		{
			leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou can't kick yourself!"));
			return;
		}
		
		if (getParty(kicked) == null || (!(getParty(kicked).getLeader().getUniqueId().equals(getParty(leader).getLeader().getUniqueId()))))
		{
			leader.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + kicked.getName() + "&7&o is not in your party!"));
			return;
		}
		
		if (party != null)
		{
			if (party.getLeader().getUniqueId().equals(leader.getUniqueId()))
			{
				int count = 0;
				while (count < party.getPlayers().size())
				{
					Player current = party.getPlayers().get(count);
					if (current.getUniqueId().equals(kicked.getUniqueId()))
					{
						party.getPlayers().remove(count);
						hidePlayer(kicked,party);
						leader.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYou have kicked &f&o" + kicked.getName() + "&7&o from the party!"));
						kicked.sendMessage(MainUtils.chatColor("&a&lSorry! &7&oYou have been kicked from your party!"));
						return;
					} else {
						count++;
					}
				}
			} else {
				leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be the leader of the party to do this!"));
				return;
			}
		} else {
			leader.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be in a party to use this!"));
			return;
		}
	}
	
	public static void acceptInvite(Player user, String leader)
	{
		PartyUtil checkParty = getParty(user);
		if (checkParty != null)
		{
			user.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must leave your party to do this!"));
			return;
		}
		
		if (pendingInvites.containsKey(user.getUniqueId()))
		{
			PartyUtil inviteParty = pendingInvites.get(user.getUniqueId());
			pendingInvites.remove(user.getUniqueId());
			Player join;
			try {
				join = Bukkit.getPlayer(leader);
			} catch (Exception exc) {
				user.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + leader + " &7&ois not online!"));
				return;
			}
			
			PartyUtil party = getParty(join);
			if (party != null)
			{
				if (party.getLeader().getUniqueId().equals(inviteParty.getLeader().getUniqueId()))
				{
					//set inventory
					for (Player  each : party.getPlayers())
					{
						each.sendMessage(MainUtils.chatColor("&a&lNotice! &f&o" + user.getName() + " &7&o has joined your party!"));
					}
					party.getPlayers().add(user);
					showPlayer(user,party);
					user.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYou have joined the party!"));
					return;
				} else {
					user.sendMessage(MainUtils.chatColor("&c&lError! &7&oInvalid invite, please try again!"));
					return;
				}
			} else {
				user.sendMessage(MainUtils.chatColor("&c&lError! &f&o" + leader + " &7&ois not in a party!"));
				return;
			}
		} else {
			user.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou have no pending invites!"));
			return;
		}
	}	
	
	
	public static void leaveParty(Player user)
	{
		PartyUtil party = getParty(user);
		if (party != null)
		{
			if (party.getLeader().getUniqueId().equals(user.getUniqueId()))
			{
				user.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must disband party or transfer leadership as owner of the party!"));
				user.sendMessage(MainUtils.chatColor("&f&o(Hint: Use /party disband or /party leader <name>)"));
				return;
			}
			
			int count = 0;
			while (count < party.getPlayers().size())
			{
				Player current = party.getPlayers().get(count);
				if (current.getUniqueId().equals(user.getUniqueId()))
				{
					party.getPlayers().remove(count);
					hidePlayer(user,party);
					user.sendMessage(MainUtils.chatColor("&a&lSuccess! &7&oYou have left your party!"));
					return;
				} else {
					count++;
				}
			}
		} else {
			user.sendMessage(MainUtils.chatColor("&c&lError! &7&oYou must be in a party to use this!"));
			return;
		}
	}
	
}
