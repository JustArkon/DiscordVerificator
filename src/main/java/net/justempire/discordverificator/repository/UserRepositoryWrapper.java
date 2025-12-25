package net.justempire.discordverificator.repository;

import net.justempire.discordverificator.DiscordVerificatorPlugin;
import net.justempire.discordverificator.configuration.Configuration;
import net.justempire.discordverificator.configuration.RepositoryConfiguration;
import net.justempire.discordverificator.exceptions.MinecraftUsernameAlreadyLinkedException;
import net.justempire.discordverificator.exceptions.NotFoundException;
import net.justempire.discordverificator.exceptions.UserNotFoundException;
import net.justempire.discordverificator.repository.abstraction.UserRepository;
import net.justempire.discordverificator.repository.implementation.JsonUserRepository;
import net.justempire.discordverificator.types.models.User;

import java.io.IOException;

public class UserRepositoryWrapper extends UserRepository {
    private UserRepository activeRepository;

    public UserRepositoryWrapper(RepositoryConfiguration config){
        updateImplementation(config);
    }

    public void updateImplementation(RepositoryConfiguration config){
        Configuration pluginConfig = DiscordVerificatorPlugin.getConfigWrapper();
        if(activeRepository != null){
            activeRepository.onShutDown();
        }
        activeRepository = new JsonUserRepository(String.format("%s/%s.json", pluginConfig.getDataFolder(), config.getFileName()));
    }
    @Override
    public User getByDiscordId(String discordId) throws UserNotFoundException {
        return activeRepository.getByDiscordId(discordId);
    }

    @Override
    public User getByMinecraftUsername(String minecraftUsername) throws UserNotFoundException {
        return activeRepository.getByMinecraftUsername(minecraftUsername);
    }

    @Override
    public void updateLastTimeUserReceivedCode(String discordId, String ip) throws UserNotFoundException {
        activeRepository.updateLastTimeUserReceivedCode(discordId, ip);
    }

    @Override
    public void updateIp(String discordId, String newIp) throws UserNotFoundException {
        activeRepository.updateIp(discordId, newIp);
    }

    @Override
    public void linkUser(String discordId, String minecraftUsername) throws MinecraftUsernameAlreadyLinkedException {
        activeRepository.linkUser(discordId, minecraftUsername);
    }

    @Override
    public void unlinkUser(String minecraftUsername) throws NotFoundException {
        activeRepository.unlinkUser(minecraftUsername);
    }


    @Override
    public void onShutDown() {
        activeRepository.onShutDown();
    }

    @Override protected void loadUsers() throws IOException { /* Delegated */ }
    @Override protected void saveUsers() { /* Delegated */ }
    @Override protected void setUp() { /* Delegated */ }

}
