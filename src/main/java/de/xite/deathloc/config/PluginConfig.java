package de.xite.deathloc.config;

import de.xite.deathloc.main.DeathLocation;
import org.bukkit.configuration.Configuration;

public class PluginConfig {
    private DeathLocation instance;
    private Configuration config;

    public PluginConfig(DeathLocation instance) {
        this.instance = instance;
        loadConfig();
    }

    public void loadConfig() {
        // Load config
        instance.getConfig().options().copyDefaults(true);
        instance.saveDefaultConfig();
        instance.reloadConfig();

        config = instance.getConfig();
    }

    public Configuration getConfig() {
        return this.config;
    }

    public void saveConfig() {
        instance.saveConfig();
    }


    public String getString(String s) {
        return config.getString(s);
    }

    public int getInt(String s) {
        return config.getInt(s);
    }
}
