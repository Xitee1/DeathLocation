package de.xite.deathloc.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.xite.deathloc.main.DeathLocation;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Actionbar {
	static DeathLocation instance = DeathLocation.getInstance();
	
    static HashMap<Player, Integer> counter = new HashMap<>();
    static HashMap<Player, String> message = new HashMap<>();
    
    public static void sendActionBar(Player p, String msg) {
    	p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }

    public static void sendActionBar(Player p, String msg, int seconds) {
    	p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    	message.put(p, msg);
    	counter.put(p, seconds);
    }

	public static void removeActionbar(Player p, boolean forceRemoveText) {
		counter.remove(p);
		message.remove(p);
		if(forceRemoveText)
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
	}

    public static void startActionbarService() {
    	instance.getLogger().info("ActionBar manager started.");
    	Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
			ArrayList<Player> toRemove = new ArrayList<>();
			for(Player p : counter.keySet()) {
				int count = counter.get(p) - 1;

				if(count > 0) {
					counter.replace(p, count);
					sendActionBar(p, message.get(p));
				}else {
					toRemove.add(p);
					toRemove.add(p);
				}
			}
			for(Player p : toRemove) {
				counter.remove(p);
				message.remove(p);
			}
		}, 20, 20);
    }
}
