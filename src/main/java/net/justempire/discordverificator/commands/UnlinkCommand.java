package net.justempire.discordverificator.commands;

import net.justempire.discordverificator.DiscordVerificatorPlugin;
import net.justempire.discordverificator.configuration.Configuration;
import net.justempire.discordverificator.exceptions.NotFoundException;
import net.justempire.discordverificator.repository.abstraction.UserRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnlinkCommand implements CommandExecutor {
    private final UserRepository userRepository;
    private final Configuration config;

    public UnlinkCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
        config = DiscordVerificatorPlugin.getConfigWrapper();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arguments) {
        if (!commandSender.hasPermission("discordVerificator.unlink")) {
            commandSender.sendMessage(config.getMessage("not-enough-permissions"));
            return true;
        }

        if (arguments.length != 1) {
            commandSender.sendMessage(config.getMessage("invalid-unlink-format"));
            return true;
        }

        try {
            userRepository.unlinkUser(arguments[0]);
            commandSender.sendMessage(config.getMessage("successfully-unlinked"));
            return true;
        }
        catch (NotFoundException e) {
            commandSender.sendMessage(config.getMessage("player-was-not-linked"));
            return true;
        }
    }
}
