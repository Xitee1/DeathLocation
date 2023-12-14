package de.xite.deathloc.utils;

import de.xite.deathloc.main.DeathLocation;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class DeathHandler {
    private static final DeathLocation instance = DeathLocation.getInstance();

    private HashMap<Player, String> waitRespawn;

    private boolean chatMessageEnabled;
    private boolean actionbarEnabled;
    private boolean titleEnabled;
    private int actionbarTimeout;
    private boolean sendToAllPlayers;
    private boolean appendMessage;
    private String message;
    private String messageOther;
    public DeathHandler() {
        waitRespawn = new HashMap<>();

        Configuration c = DeathLocation.getPluginConfig().getConfig();
        chatMessageEnabled = c.getBoolean("types.chat-message");
        actionbarEnabled = c.getBoolean("types.actionbar");
        titleEnabled = c.getBoolean("types.title");
        actionbarTimeout = c.getInt("actionbar.timeout");
        sendToAllPlayers = c.getBoolean("allPlayers");
        appendMessage = c.getBoolean("message.append");
        message = c.getString("message.message");
        messageOther = c.getString("message.messageOther");

        if(appendMessage && chatMessageEnabled)
            instance.getLogger().warning("You should not enable chat messages and enable appendMessage at the same time!");

        if(!chatMessageEnabled && !actionbarEnabled && !titleEnabled)
            instance.getLogger().warning("You have no modules enabled. You won't make any use of the plugin if you don't enable some.");
    }

    public void registerDeath(Player p) {
        registerDeath(p, null);
    }

    public void registerDeath(Player p, PlayerDeathEvent e) {
        if(p == null)
            return;

        String message = translateMessage(this.message, p);
        String messageOther = translateMessage(this.messageOther, p);
        if(message == null || messageOther == null) {
            instance.getLogger().severe("Error: No message found! Please fix your config.");
            return;
        }

        if(appendMessage && e != null)
            e.setDeathMessage(e.getDeathMessage() + messageOther);

        if(sendToAllPlayers) {
            for(Player all : instance.getServer().getOnlinePlayers()) {
                if(all == p) {
                    sendMessage(all, messageOther);
                } else {
                    sendMessage(p, message);
                }
            }
        }else {
            sendMessage(p, message);
        }

        waitRespawn.put(p, message);
    }

    public void registerRespawn(Player p) {
        String respawnMessage = waitRespawn.get(p);
        if(respawnMessage != null) {
            if(titleEnabled)
                p.sendTitle(respawnMessage, "", 5, 20, 5);
        }

        removeWaitingRespawn(p);
    }

    public void removeWaitingRespawn(Player p) {
        waitRespawn.remove(p);
    }


    private void sendMessage(Player p, String message) {
        if(chatMessageEnabled)
            p.sendMessage(message);

        if(actionbarEnabled)
            Actionbar.sendActionBar(p, message, actionbarTimeout);

        if(titleEnabled)
            p.sendTitle(message, "", 5, 20, 5);
    }

    private String translateMessage(String msg, Player p) {
        if(msg == null)
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
