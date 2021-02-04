package me.BoyJamal.practice.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class QueueUtil {

	public static List<QueueUtil> queueList = new ArrayList<>();
	
	private Player p;
	private KitUtil kit;
	private String type;
	
	public QueueUtil(Player p, KitUtil kit, String type)
	{
		this.p = p;
		this.kit = kit;
		this.type = type;
	}
	
	public String getType()
	{
		return type;
	}
	
	public Player getPlayer()
	{
		return p;
	}
	
	public KitUtil getKit()
	{
		return kit;
	}
	
}
