package de.xite.deathloc.main;

import de.xite.deathloc.listener.DeathListener;
import de.xite.deathloc.listener.JoinListener;
import de.xite.deathloc.utils.Updater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.xite.deathloc.utils.Actionbar;
import de.xite.deathloc.utils.BStatsMetrics;
import net.md_5.bungee.api.ChatColor;

import java.util.logging.Logger;

public class DeathLocation extends JavaPlugin {
	private static final int pluginID = 96051;
	private static boolean debug = false;

	private static DeathLocation instance;
	private static Updater updater;
	private static Logger logger;

	public static String pr = ChatColor.GRAY+"["+ChatColor.YELLOW+"DeathLocation"+ChatColor.GRAY+"] "; // prefix
	
	@Override
	public void onEnable() {
		instance = this;
		updater = new Updater(pluginID);
		logger = this.getLogger();
		
		// Load config
		instance.getConfig().options().copyDefaults(true);
		instance.saveDefaultConfig();
		instance.reloadConfig();
		
		// Check if debug is enabled
		debug = instance.getConfig().getBoolean("debug");
		
		// Register listeners
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new JoinListener(), this);
		
		// Start the ActionBar Manager
		if(instance.getConfig().getBoolean("types.actionbar"))
			Actionbar.startActionbarService();

		if(updater.infoMessageEnabled() && updater.updateAvailable())
			logger.info("An update is available! Installed version: v"+updater.getCurrentVersion()+", Newest version: "+updater.getLatestVersion());

		initializeBStats();
	}

	public static DeathLocation getInstance() {
		return instance;
	}

	public static Updater getUpdater() {
		return updater;
	}

	public static boolean isDebugEnabled() {
		return debug;
	}


	public void initializeBStats(){
		// BStats analytics
		try {
			int pluginId = 12730;
			BStatsMetrics metrics = new BStatsMetrics(instance, pluginId);
			// Custom charts
			metrics.addCustomChart(new BStatsMetrics.SimplePie("update_auto_update", () -> instance.getConfig().getBoolean("update.autoupdater") ? "Aktiviert" : "Deaktiviert"));
			metrics.addCustomChart(new BStatsMetrics.SimplePie("update_notifications", () -> instance.getConfig().getBoolean("update.notification") ? "Aktiviert" : "Deaktiviert"));

			metrics.addCustomChart(new BStatsMetrics.SimplePie("use_chat_message", () -> instance.getConfig().getBoolean("types.chat-message") ? "Aktiviert" : "Deaktiviert"));
			metrics.addCustomChart(new BStatsMetrics.SimplePie("use_actionbar", () -> instance.getConfig().getBoolean("types.actiobar") ? "Aktiviert" : "Deaktiviert"));
			metrics.addCustomChart(new BStatsMetrics.SimplePie("use_title", () -> instance.getConfig().getBoolean("types.title") ? "Aktiviert" : "Deaktiviert"));
			
			metrics.addCustomChart(new BStatsMetrics.SimplePie("allplayers", () -> instance.getConfig().getBoolean("allPlayers") ? "Aktiviert" : "Deaktiviert"));
			
			metrics.addCustomChart(new BStatsMetrics.SimplePie("message_append", () -> instance.getConfig().getBoolean("message.append") ? "Aktiviert" : "Deaktiviert"));

			if(debug)
				instance.getLogger().info("Analytics sent to BStat.");
		} catch (Exception e) {
			instance.getLogger().warning("Could not send analytics to BStats.");
		}
	}
}