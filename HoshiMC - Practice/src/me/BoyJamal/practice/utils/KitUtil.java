package me.BoyJamal.practice.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitUtil {

	private String name;
	private List<ItemStack> items;
	private ItemStack displayItem;
	private Inventory previewInv;
	
	public KitUtil(String name, List<ItemStack> items)
	{
		this.name = name;
		this.items = items;
		previewInv = Bukkit.createInventory(null, MainUtils.getSlots(items.size()),MainUtils.chatColor("&fKit Viewer:"));
		
		int count = 0;
		for (ItemStack each : items)
		{
			if (each != null)
			{
				previewInv.setItem(count, each);
			}
			count++;
		}
		
		while (count % 9 != 0)
		{
			count++;
		}
		
		for (int i = count; i <=count+3;i++)
		{
			previewInv.setItem(i, MainUtils.glassItem());
		}
		count+=4;
		previewInv.setItem(count, MainUtils.returnItem());
		count++;
		for (int i = count; i <=count+3;i++)
		{
			previewInv.setItem(i, MainUtils.glassItem());
		}
		
	}
	
	public String getName()
	{
		return name;
	}
	
	public List<ItemStack> getItems()
	{
		return items;
	}
	
	public ItemStack getDisplay()
	{
		return displayItem;
	}
	
	public Inventory getInv()
	{
		return previewInv;
	}
	
}
