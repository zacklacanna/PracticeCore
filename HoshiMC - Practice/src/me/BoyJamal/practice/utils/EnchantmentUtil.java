package me.BoyJamal.practice.utils;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentUtil {

	private Enchantment type;
	private int level;
	
	public EnchantmentUtil(Enchantment type, int level)
	{
		this.type = type;
		this.level = level;
	}
	
	public Enchantment getType()
	{
		return type;
	}
	
	public int getLevel()
	{
		return level;
	}
	
}
