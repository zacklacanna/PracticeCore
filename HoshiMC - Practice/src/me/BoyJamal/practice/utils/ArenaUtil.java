package me.BoyJamal.practice.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArenaUtil {
	
	private LocationUtil loc;
	private String name;
	private int id;
	private boolean inUse;
	
	public ArenaUtil(LocationUtil loc, String name, int id)
	{
		this.loc = loc;
		this.name = name;
		this.id = id;
		this.inUse = false;
	}
	
	public LocationUtil getLocations()
	{
		return loc;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getID()
	{
		return id;
	}
	
	public boolean inUse()
	{
		return inUse;
	}
	
	public void setUse(boolean val)
	{
		this.inUse = val;
	}
	
	public ItemStack getDisplay()
	{
		ItemStack item = new ItemStack(Material.PISTON_BASE);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(MainUtils.chatColor("&a&lArena #" + id));
		List<String> lore = new ArrayList<>();
		lore.add(MainUtils.chatColor("&a&l* &7Name: &f" + name));
		lore.add(MainUtils.chatColor("&a&l* &7ID: &f" + id));
		lore.add(MainUtils.chatColor("&a&l* &7In Use: &f" + inUse));
		lore.add("");
		lore.add(MainUtils.chatColor("&a&l* &7&nClick to teleport!"));
		im.setLore(lore);
		item.setItemMeta(im);
		return item;
	}

}
