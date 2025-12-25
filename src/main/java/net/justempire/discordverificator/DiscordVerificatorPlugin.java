package net.justempire.discordverificator;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.justempire.discordverificator.commands.LinkCommand;
import net.justempire.discordverificator.commands.ReloadCommand;
import net.justempire.discordverificator.commands.UnlinkCommand;
import net.justempire.discordverificator.configuration.Configuration;
import net.justempire.discordverificator.discord.DiscordBot;
import net.justempire.discordverificator.listeners.JoinListener;
import net.justempire.discordverificator.repository.UserRepositoryWrapper;
import net.justempire.discordverificator.services.ConfirmationCodeService;
import net.justempire.discordverificator.utils.MessageColorizer;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordVerificatorPlugin extends JavaPlugin {
    private Logger logger;
    private UserRepositoryWrapper userRepository;
    private ConfirmationCodeService confirmationCodeService;
    private DiscordBot discordBot;

    private static Configuration config;

    private JDA currentJDA;

    @Override
    public void onEnable() {
        // Setting up the logger
        logger = getLogger();

        // Setting up services
        saveDefaultConfig();
        config = new Configuration(this);
        userRepository = new UserRepositoryWrapper(config.getRepositoryConfiguration());
        confirmationCodeService = new ConfirmationCodeService();

        // Setting up the bot
        setupBot();

        // Starting the plugin
        try { start(); }
        catch (RuntimeException e) { getLogger().log(Level.SEVERE, e.getMessage()); }

        // Registering listeners
        getServer().getPluginManager().registerEvents(new JoinListener(this, userRepository, confirmationCodeService), this);

        // Registering commands
        Objects.requireNonNull(getCommand("link")).setExecutor(new LinkCommand(userRepository));
        Objects.requireNonNull(getCommand("unlink")).setExecutor(new UnlinkCommand(userRepository));
        Objects.requireNonNull(getCommand("dvreload")).setExecutor(new ReloadCommand(this));

        logger.info("Enabled successfully!");
    }

    @Override
    public void onDisable() {
        userRepository.onShutDown();
        if (currentJDA != null) currentJDA.shutdown();
        logger.info("Shutting down!");
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void start() {
        // Saving the default config
        saveDefaultConfig();

        // Updating the config with missing key-pairs (and removing redundant ones if present)
        File configFile = new File(getDataFolder(), "config.yml");
        try { ConfigUpdater.update(this, "config.yml", configFile, new ArrayList<>()); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public DiscordBot getDiscordBot(){
        return discordBot;
    }
    private void setupBot() {
        String token = getConfig().getString("token");
        this.discordBot = new DiscordBot(logger, userRepository, confirmationCodeService);

        try {
            this.currentJDA = JDABuilder.createLight(token)
                    .addEventListeners(discordBot)
                    .setAutoReconnect(true)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setStatus(OnlineStatus.ONLINE)
                    .build();
        } catch (LoginException e) {
            logger.severe(MessageColorizer.colorize("Wrong discord bot token provided!"));
        }
    }

    public void reload() {
        // Trying to shut down the bot
        try { currentJDA.shutdownNow(); }
        catch (Exception ignored) { }

        // Reloading the config
        reloadConfig();

        // Reloading JSON file where users are stored
        userRepository.updateImplementation(config.getRepositoryConfiguration());

        // Starting the bot
        setupBot();
    }
    public static Configuration getConfigWrapper() { return config; }
}

