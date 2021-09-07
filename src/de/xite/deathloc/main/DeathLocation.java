package de.xite.deathloc.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class DeathLocation extends JavaPlugin {
	public DeathLocation pl;
	
	
	@Override
	public void onEnable() {
		pl = this;
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new DeathListener(), this);
	}
	
	@Override
	public void onDisable() {
		
	}
}
