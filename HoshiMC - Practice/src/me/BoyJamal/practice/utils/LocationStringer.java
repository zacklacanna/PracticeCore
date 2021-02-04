package me.BoyJamal.practice.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
 
public class LocationStringer
{
    public static String format = "%world%><%x%><%y%><%z%";
 
    public static String toString(Location loc)
    {
        String location = format
                .replaceAll("%world%", loc.getWorld().getName())
                .replaceAll("%x%", String.valueOf(loc.getX()))
                .replaceAll("%y%", String.valueOf(loc.getY()))
                .replaceAll("%z%", String.valueOf(loc.getZ()));
 
        return location;
    }
 
    public static Location fromString(String loc)
    {
        String[] parts = loc.split("><");
        World world = Bukkit.getWorld(parts[0]);
        double xPos = Double.valueOf(parts[1]);
        double yPos = Double.valueOf(parts[2]);
        double zPos = Double.valueOf(parts[3]);
 
        return new Location(world, xPos, yPos, zPos);
    }
}
 