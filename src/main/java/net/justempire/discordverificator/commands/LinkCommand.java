package net.justempire.discordverificator.commands;

import net.justempire.discordverificator.DiscordVerificatorPlugin;
import net.justempire.discordverificator.configuration.Configuration;
import net.justempire.discordverificator.exceptions.MinecraftUsernameAlreadyLinkedException;
import net.justempire.discordverificator.repository.abstraction.UserRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LinkCommand implements CommandExecutor {
    private final UserRepository userRepository;
    private final Configuration config;

    public LinkCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
        config = DiscordVerificatorPlugin.getConfigWrapper();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arguments) {
        if (!commandSender.hasPermission("discordVerificator.link")) {
            commandSender.sendMessage(config.getMessage("not-enough-permissions"));
            return true;
        }

        if (arguments.length != 2) {
            commandSender.sendMessage(config.getMessage("invalid-link-format"));
            return true;
        }

        String playerName = arguments[0];
        String discordUserId = arguments[1];

        if (discordUserId.length() < 17) {
            commandSender.sendMessage(config.getMessage("invalid-user-id-format"));
            return true;
        }

        try {
            userRepository.linkUser(discordUserId, playerName);
            commandSender.sendMessage(config.getMessage("successfully-linked"));
        }
        catch (MinecraftUsernameAlreadyLinkedException e) {
            commandSender.sendMessage(config.getMessage("player-already-linked"));
        }

        return true;
    }
}
