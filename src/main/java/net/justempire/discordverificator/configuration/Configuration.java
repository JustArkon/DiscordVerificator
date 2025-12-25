package net.justempire.discordverificator.configuration;

import net.justempire.discordverificator.types.enums.RepositoryType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Configuration extends MessageConfigurationBase {
    private final File dataFolder;
    public Configuration(JavaPlugin plugin) {
        super(plugin, "messages");
        dataFolder = plugin.getDataFolder();
    }

    public RepositoryConfiguration getRepositoryConfiguration() {
        RepositoryConfiguration config = new RepositoryConfiguration();

        config.setType(getEnumValue("storage.type", RepositoryType.class, RepositoryType.JSON));
        config.setFileName(getString("storage.fileName"));
        config.setHost(getString("storage.host"));
        config.setPort(getInt("storage.port"));
        config.setDatabase(getString("storage.database"));
        config.setUsername(getString("storage.username"));
        config.setPassword(getString("storage.password"));

        return config;
    }

    public File getDataFolder(){
        return dataFolder;
    }
}
