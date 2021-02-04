package me.BoyJamal.practice.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class GuiManager {

	private String name;
	private InventoryType type = null;
	private int maxSlots = 0;
	private List<GuiItem> items;
	private Inventory inv;
	
	public GuiManager(String name, InventoryType type, List<GuiItem> items) 
	{
		this.name = name;
		this.type = type;
		this.items = items;
		inv = Bukkit.createInventory(null, type,name);
		for (GuiItem each : items)
		{
			inv.setItem(each.getSlot(), each.getItem());
		}
	}
	
	public GuiManager(String name, int maxSlots,List<GuiItem> items)
	{
		this.name = name;
		this.maxSlots = maxSlots;
		this.items = items;
		inv = Bukkit.createInventory(null, maxSlots,name);
		for (GuiItem each : items)
		{
			inv.setItem(each.getSlot(), each.getItem());
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public InventoryType getType()
	{
		return type;
	}
	
	public int getSlots()
	{
		return maxSlots;
	}
	
	public Inventory getInv()
	{
		return inv;
	}
	
}
