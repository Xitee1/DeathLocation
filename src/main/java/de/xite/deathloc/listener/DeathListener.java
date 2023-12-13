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
	DeathLocation instance = DeathLocation.getInstance();

	HashMap<Player, String> waitRespawn = new HashMap<>();
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();

		String message = translateMessage(instance.getConfig().getString("message.message"), p);
		String messageOther = translateMessage(instance.getConfig().getString("message.messageOther"), p);
		if(message == null || messageOther == null) {
			instance.getLogger().severe("Error: No message found (message.message). Please fix your config.");
			return;
		}

		// TODO reduce loops
		// TODO do not directly get config data

		// Send message in chat
		if(instance.getConfig().getBoolean("types.chat-message")) {
			if(instance.getConfig().getBoolean("message.append")) {
				e.setDeathMessage(e.getDeathMessage() + messageOther);
			}else {
				if(instance.getConfig().getBoolean("allPlayers")) {
					for(Player all : instance.getServer().getOnlinePlayers()) {
						if(all == p) {
							p.sendMessage(message);
						} else {
							all.sendMessage(messageOther);
						}
					}
				}else {
					p.sendMessage(message);
				}
			}
		}

		// Send message to actionbar
		if(instance.getConfig().getBoolean("types.actionbar")) {

			if(instance.getConfig().getBoolean("allPlayers")) {
				for(Player all : instance.getServer().getOnlinePlayers()) {
					if(all == p) {
						Actionbar.sendActionBar(p, message, instance.getConfig().getInt("actionbar.timeout"));
					} else {
						Actionbar.sendActionBar(all, messageOther, instance.getConfig().getInt("actionbar.timeout"));
					}
				}
			}else {
				Actionbar.sendActionBar(p, message, instance.getConfig().getInt("actionbar.timeout"));
			}
		}


		if(instance.getConfig().getBoolean("types.title")) {
			waitRespawn.put(p, message);
			// If the player doesn't respawn within 10 minutes, we remove him from the list.
			Bukkit.getScheduler().runTaskLater(instance, () -> waitRespawn.remove(p), 20*60*10);
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if(waitRespawn.containsKey(p)) {
			if(instance.getConfig().getBoolean("allPlayers")) {
				for(Player all : Bukkit.getOnlinePlayers())
					all.sendTitle(waitRespawn.get(p), "", 5, 20, 5);
			}else {
				p.sendTitle(waitRespawn.get(p), "", 5, 20*10, 5);
			}
			
			waitRespawn.remove(p);
		}
	}


	private static String translateMessage(String msg, Player p) {
		if(msg == null || p == null)
			return null;

		Location loc = p.getLocation();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		msg = msg
				.replace("%x", String.valueOf(x))
				.replace("%y", String.valueOf(y))
				.replace("%z", String.valueOf(z))
				.replace("%player", p.getName())
				.replace("%displayname", p.getDisplayName());

		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}
}
