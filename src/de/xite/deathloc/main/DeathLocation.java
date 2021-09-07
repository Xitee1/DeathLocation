package de.xite.deathloc.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.xite.deathloc.utils.Actionbar;
import de.xite.deathloc.utils.Updater;

public class DeathLocation extends JavaPlugin {
	public static DeathLocation pl;
	
	
	@Override
	public void onEnable() {
		pl = this;
		
		// Load config
		pl.getConfig().options().copyDefaults(true);
		pl.saveDefaultConfig();
		pl.reloadConfig();
		
		// Register listeners
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new DeathListener(), this);
		
		// Start the ActionBar Manager
		if(pl.getConfig().getBoolean("types.actionbar"))
			Actionbar.start();
	}
	
	@Override
	public void onDisable() {
		if(pl.getConfig().getBoolean("update.autoupdater"))
			if(Updater.checkVersion())
				Updater.downloadFile();
	}
}
