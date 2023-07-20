package de.xite.deathloc.listener;

import de.xite.deathloc.main.DeathLocation;
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
		if((pl.getConfig().getBoolean("update.notification") || p.isOp()) && Updater.checkVersion()) {
			p.sendMessage(DeathLocation.pr+ChatColor.RED+"A new update is available ("+ChatColor.AQUA+"v"+Updater.version+ChatColor.RED+")! Your version: "+ChatColor.AQUA+pl.getDescription().getVersion());
		}
	}
}
