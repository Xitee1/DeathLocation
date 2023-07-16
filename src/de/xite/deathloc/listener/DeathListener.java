package de.xite.deathloc.listener;

import java.util.HashMap;

import de.xite.deathloc.main.DeathLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.xite.deathloc.utils.Actionbar;
import net.md_5.bungee.api.ChatColor;

public class DeathListener implements Listener{
	DeathLocation pl = DeathLocation.pl;
	
	HashMap<Player, String> waitRespawn = new HashMap<>();
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Location loc = p.getLocation();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		
		String message = pl.getConfig().getString("message.message");
		if(message == null) {
			pl.getLogger().severe("Error: No message found (message.message). Please fix your config.");
			return;
		}

		message = message
				.replace("%x", String.valueOf(x))
				.replace("%y", String.valueOf(y))
				.replace("%z", String.valueOf(z))
				.replace("%player", p.getName())
				.replace("%displayname", p.getDisplayName());

		message = ChatColor.translateAlternateColorCodes('&', message);

		// Send message in chat
		if(pl.getConfig().getBoolean("types.chat-message")) {
			if(pl.getConfig().getBoolean("message.append")) {
				e.setDeathMessage(e.getDeathMessage() + message);
			}else {
				if(pl.getConfig().getBoolean("allPlayers")) {
					Bukkit.broadcastMessage(message);
				}else {
					p.sendMessage(message);
				}
			}
		}

		// Send message to actionbar
		if(pl.getConfig().getBoolean("types.actionbar")) {
			if(pl.getConfig().getBoolean("allPlayers")) {
				for(Player all : Bukkit.getOnlinePlayers())
					Actionbar.sendActionBar(all, message, 15);
			}else {
				Actionbar.sendActionBar(p, message, 15);
			}
		}


		if(pl.getConfig().getBoolean("types.title")) {
			waitRespawn.put(p, message);
			// If the player doesn't respawn within 10 minutes, we remove him from the list.
			Bukkit.getScheduler().runTaskLater(pl, () -> waitRespawn.remove(p), 20*60*10);
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if(waitRespawn.containsKey(p)) {
			if(pl.getConfig().getBoolean("allPlayers")) {
				for(Player all : Bukkit.getOnlinePlayers())
					all.sendTitle(waitRespawn.get(p), "", 5, 20, 5);
			}else {
				p.sendTitle(waitRespawn.get(p), "", 5, 20*10, 5);
			}
			
			waitRespawn.remove(p);
		}
	}
}
