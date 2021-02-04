package me.BoyJamal.practice.utils;

import org.bukkit.inventory.ItemStack;

public class GuiItem {

	private int slot;
	private ItemStack item;
	
	public GuiItem(int slot, ItemStack item)
	{
		this.slot = slot;
		this.item = item;
	}
	
	public int getSlot()
	{
		return slot;
	}
	
	public ItemStack getItem()
	{
		return item;
	}
	
}
