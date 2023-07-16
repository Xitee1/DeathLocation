package de.xite.deathloc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;

import de.xite.deathloc.main.DeathLocation;


public class Updater {
	final private static int pluginID = 96051;

	public static String version;

    public static String getVersion() {
        if(version == null) {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + pluginID).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    String d = scanner.next();
                    version = d;
                    return d;
                }
            } catch (IOException e) {
                DeathLocation.pl.getLogger().info("Updater -> Cannot look for updates: " + e.getMessage());
                return "Could not check for updates! You probably restarted your server to often, so SpigotMC blocked your IP. You probably have to wait a few minutes or hours.";
            }

            // Set it to null again after 24h to check again (there might be a new version)
            Bukkit.getScheduler().runTaskLaterAsynchronously(DeathLocation.pl, () -> version = null, 20*60*60*24);
        }
        return version;
    }
    
    public static boolean checkVersion() {
		return !getVersion().equals(DeathLocation.pl.getDescription().getVersion());
	}
}
