package de.xite.deathloc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;

import de.xite.deathloc.main.DeathLocation;


public class Updater {
	private static DeathLocation pl = DeathLocation.pl;
	final private static int pluginID = 96051;
	public static String version;

    public static String getVersion() {
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + pluginID).openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
            	String d = scanner.next();
            	version = d;
                return d;
            }
        } catch (IOException e) {
            pl.getLogger().info("Updater -> Cannot look for updates: " + e.getMessage());
        }
        return "Could not check for updates! You probably restarted your server to often, so SpigotMC blocked your IP.";
    }
    
    public static boolean checkVersion() {
    	if(version == null) {
    		version = getVersion();
    		// Check again after 24h
    		Bukkit.getScheduler().runTaskLater(pl, () -> version = null, 20*60*60*24);
    	}

		return !version.equals(pl.getDescription().getVersion());
	}
}
