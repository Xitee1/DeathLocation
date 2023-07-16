package de.xite.deathloc.main;

import de.xite.deathloc.listener.DeathListener;
import de.xite.deathloc.listener.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.xite.deathloc.utils.Actionbar;
import de.xite.deathloc.utils.BStatsMetrics;
import net.md_5.bungee.api.ChatColor;

public class DeathLocation extends JavaPlugin {
	public static DeathLocation pl;
	public static boolean debug = false;
	
	public static String pr = ChatColor.GRAY+"["+ChatColor.YELLOW+"DeathLocation"+ChatColor.GRAY+"] "; // prefix
	
	@Override
	public void onEnable() {
		pl = this;
		
		// Load config
		pl.getConfig().options().copyDefaults(true);
		pl.saveDefaultConfig();
		pl.reloadConfig();
		
		// Check if debug is enabled
		debug = pl.getConfig().getBoolean("debug");
		
		// Register listeners
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new JoinListener(), this);
		
		// Start the ActionBar Manager
		if(pl.getConfig().getBoolean("types.actionbar"))
			Actionbar.startActionbarService();

		BStats();
	}

	public void BStats(){
		// BStats analytics
		try {
			int pluginId = 12730; // <-- Replace with the id of your plugin!
			BStatsMetrics metrics = new BStatsMetrics(pl, pluginId);
			// Custom charts
			metrics.addCustomChart(new BStatsMetrics.SimplePie("update_auto_update", () -> pl.getConfig().getBoolean("update.autoupdater") ? "Aktiviert" : "Deaktiviert"));
			metrics.addCustomChart(new BStatsMetrics.SimplePie("update_notifications", () -> pl.getConfig().getBoolean("update.notification") ? "Aktiviert" : "Deaktiviert"));

			metrics.addCustomChart(new BStatsMetrics.SimplePie("use_chat_message", () -> pl.getConfig().getBoolean("types.chat-message") ? "Aktiviert" : "Deaktiviert"));
			metrics.addCustomChart(new BStatsMetrics.SimplePie("use_actionbar", () -> pl.getConfig().getBoolean("types.actiobar") ? "Aktiviert" : "Deaktiviert"));
			metrics.addCustomChart(new BStatsMetrics.SimplePie("use_title", () -> pl.getConfig().getBoolean("types.title") ? "Aktiviert" : "Deaktiviert"));
			
			metrics.addCustomChart(new BStatsMetrics.SimplePie("allplayers", () -> pl.getConfig().getBoolean("allPlayers") ? "Aktiviert" : "Deaktiviert"));
			
			metrics.addCustomChart(new BStatsMetrics.SimplePie("message_append", () -> pl.getConfig().getBoolean("message.append") ? "Aktiviert" : "Deaktiviert"));

			if(debug)
				pl.getLogger().info("Analytics sent to BStat.");
		} catch (Exception e) {
			pl.getLogger().warning("Could not send analytics to BStats.");
		}
	}
}