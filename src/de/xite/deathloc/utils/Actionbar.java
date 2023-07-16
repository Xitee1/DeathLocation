package de.xite.deathloc.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.xite.deathloc.main.DeathLocation;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Actionbar {
	static DeathLocation pl = DeathLocation.pl;
	
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

    public static void start() {
    	pl.getLogger().info("ActionBar manager started.");
    	Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
			@Override
			public void run() {
				for(Player p : counter.keySet()) {
					int count = counter.get(p) - 1;

					if(count <= 0) {
						counter.replace(p, count);
						sendActionBar(p, message.get(p));
					}else {
						counter.remove(p);
						message.remove(p);
					}
				}
			}
		}, 20, 20);
    }
}
