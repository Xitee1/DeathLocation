package de.xite.deathloc.main;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.xite.deathloc.utils.Updater;


public class JoinListener implements Listener {
	DeathLocation pl = DeathLocation.pl;
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("deathlocation.update") || p.isOp()) {
			if(Updater.checkVersion()) {
				if(pl.getConfig().getBoolean("update.notification")) {
					p.sendMessage(DeathLocation.pr+ChatColor.RED+"A new version is available ("+ChatColor.AQUA+"v"+Updater.version+ChatColor.RED+")! Your version: "+ChatColor.AQUA+pl.getDescription().getVersion());
					if(pl.getConfig().getBoolean("update.autoupdater")) {
						p.sendMessage(DeathLocation.pr+ChatColor.GREEN+"The plugin will be updated automatically after a server restart.");
					}else {
						p.sendMessage(DeathLocation.pr+ChatColor.RED+"The auto-updater is disabled in your config.yml. Please manually update the plugin or enable the auto-updater.");
					}
				}
			}
		}
	}
}
