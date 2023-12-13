package de.xite.deathloc.listener;

import de.xite.deathloc.main.DeathLocation;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.xite.deathloc.utils.Updater;


public class JoinListener implements Listener {
	Updater updater = DeathLocation.getUpdater();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(p.isOp() && updater.infoMessageEnabled() && DeathLocation.getUpdater().updateAvailable()) {
			p.sendMessage(DeathLocation.pr+ChatColor.RED+
					"A new update is available ("+ChatColor.AQUA+"v"+updater.getLatestVersion()+ChatColor.RED+")!" +
					"Your version: "+ChatColor.AQUA+updater.getCurrentVersion());
		}
	}
}
