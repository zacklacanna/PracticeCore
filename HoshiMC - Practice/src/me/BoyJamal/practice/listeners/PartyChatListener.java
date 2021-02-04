package me.BoyJamal.practice.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PartyChatListener implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent evt)
	{
		//check if player is in party
		//check if message starts with @
		//if message starts with @ send to online party members
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent evt)
	{
		//check if player is in party
		//if player is leader of party, disband party
		//if player is not leader kick them from party
	}

}
