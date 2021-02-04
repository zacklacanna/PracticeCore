package me.BoyJamal.practice.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.BoyJamal.practice.utils.MainUtils;
import me.BoyJamal.practice.utils.PartyMethods;

public class PartyCMD implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			return true;
		}
		
		Player p = (Player)sender;
		if (args.length == 0)
		{
			helpMessage(p);
		} else if (args.length == 1) {
			switch(args[0].toLowerCase())
			{
				case("inviteonly"):
					PartyMethods.setPartyType(p, "invite");
					break;
				case("close"):
					PartyMethods.setPartyType(p, "closed");
					break;
				case("info"):
					PartyMethods.getInfo(p);
					break;
				case("create"):
					PartyMethods.createParty(p);
					break;
				case("leave"):
					PartyMethods.leaveParty(p);
					break;
				case("lock"):
					PartyMethods.setPartyType(p, "closed");
					break;
				case("open"):
					PartyMethods.setPartyType(p, "open");
					break;
				case("disband"):
					PartyMethods.disbandParty(p);
					break;
				case("password"):
					PartyMethods.setPartyType(p, "invite");
					break;
				default:
					helpMessage(p);
					break;
			}
		} else if (args.length == 2) {
			switch(args[0].toLowerCase())
			{
				case("invite"):
					PartyMethods.inviteParty(p, args[1]);
					break;
				case("leader"):
					PartyMethods.transferLeadership(p, args[1]);
					break;
				case("password"):
					PartyMethods.setPartyType(p, "password");
					PartyMethods.setPassword(p, args[1]);
					break;
				case("info"):
					PartyMethods.getInfo(p,args[1]);
					break;
				case("accept"):
					PartyMethods.acceptInvite(p, args[1]);
					break;
				case("kick"):
					PartyMethods.kickPlayer(p, args[1]);
					break;
				default:
					helpMessage(p);
					break;
			}
		} else if (args.length == 3) {
			switch(args[0].toLowerCase())
			{
				case("join"):
					PartyMethods.joinParty(p, args[1], args[2]);
				    break;
				default:
					helpMessage(p);
					break;
			}
		} else {
			helpMessage(p);
		}
		return true;
	}
	
	public void helpMessage(Player p)
	{
		p.sendMessage(MainUtils.chatColor(""));
		p.sendMessage(MainUtils.chatColor("&c&lParty Help"));
		p.sendMessage(MainUtils.chatColor(""));
		p.sendMessage(MainUtils.chatColor("&fParty Commands:"));
		p.sendMessage(MainUtils.chatColor("&c/party invite {name} &7&oInvite player to party"));
		p.sendMessage(MainUtils.chatColor("&c/party leave &7&oLeave current party"));
		p.sendMessage(MainUtils.chatColor("&c/party join {name} {password} &7&oJoin a password protected party"));
		p.sendMessage(MainUtils.chatColor("&c/party accept {name} &7&oAccept an invite to a party"));
		p.sendMessage(MainUtils.chatColor("&c/party info {name} &7&oGet info on a party"));
		p.sendMessage(MainUtils.chatColor("&c/party create &7&oCreate a party"));
		p.sendMessage("");
		p.sendMessage(MainUtils.chatColor("&fLeader Commands:"));
		p.sendMessage(MainUtils.chatColor("&c/party kick {name} &7&oKick player from your party"));
		p.sendMessage(MainUtils.chatColor("&c/party leader {name} &7&oTransfer party leadership"));
		p.sendMessage(MainUtils.chatColor("&c/party inviteonly &7&oSet party to invite only"));
		p.sendMessage(MainUtils.chatColor("&c/party disband &7&oDisband your current party"));
		p.sendMessage(MainUtils.chatColor("&c/party lock &7&oLock party from anyone joining"));
		p.sendMessage(MainUtils.chatColor("&c/party open &7&oAllow anyone to join party"));
		p.sendMessage(MainUtils.chatColor("&c/party password &7&oReset party to invite mode"));
		p.sendMessage(MainUtils.chatColor("&c/party password {password} &7&oSet party to password mode with certain password"));
		p.sendMessage("");
		p.sendMessage(MainUtils.chatColor("&fOther Help:"));
		p.sendMessage(MainUtils.chatColor("&f&l* &cTo use &fparty chat&c, start your message in chat with the '&f@&c' sign"));
		p.sendMessage(MainUtils.chatColor("&f&l* &cMax party size is 6 players"));
		p.sendMessage("");
	}

}
