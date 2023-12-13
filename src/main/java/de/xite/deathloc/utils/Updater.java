package de.xite.deathloc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

import de.xite.deathloc.main.DeathLocation;


public class Updater {
    private static final Logger logger = DeathLocation.getInstance().getLogger();
    final private int pluginID;
    private Date lastUpdated;
	private String latestVersion;
    private final String currentVersion;
    private final boolean infoMessageEnabled;

    public Updater(int pluginID) {
        this.pluginID = pluginID;
        latestVersion = null;
        currentVersion = DeathLocation.getInstance().getDescription().getVersion();
        infoMessageEnabled = DeathLocation.getInstance().getConfig().getBoolean("update.notification");
    }

    private void updateVersion() {
        if(latestVersion == null || new Date().getTime() - lastUpdated.getTime() > 1000*60*60*12) {
            lastUpdated = new Date();
            try(InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + pluginID).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if(scanner.hasNext()) {
                    latestVersion = scanner.next();
                }
            } catch (IOException e) {
                logger.info("Updater -> Cannot look for updates: " + e.getMessage());
            }
        }
    }

    public String getLatestVersion() {
        updateVersion();
        return latestVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }
    
    public boolean updateAvailable() {
		return !getLatestVersion().equals(getCurrentVersion());
	}

    public boolean infoMessageEnabled() {
        return infoMessageEnabled;
    }
}
