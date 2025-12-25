package net.justempire.discordverificator.repository.abstraction;

import net.justempire.discordverificator.exceptions.MinecraftUsernameAlreadyLinkedException;
import net.justempire.discordverificator.exceptions.NotFoundException;
import net.justempire.discordverificator.exceptions.UserNotFoundException;
import net.justempire.discordverificator.types.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class UserRepository {
    protected List<User> userList = new ArrayList<>();

    protected void addUser(User userToAdd) {
        userList.add(userToAdd);
        saveUsers();
    }

    public User getByDiscordId(String discordId) throws UserNotFoundException {
        for (User user : userList) {
            // Return the user if found
            if (user.getDiscordId().equalsIgnoreCase(discordId))
                return user;
        }

        throw new UserNotFoundException();
    }

    public User getByMinecraftUsername(String minecraftUsername) throws UserNotFoundException {
        for (User user : userList) {
            for (String username : user.linkedMinecraftUsernames) {
                if (minecraftUsername.equalsIgnoreCase(username))
                    return user;
            }
        }

        throw new UserNotFoundException();
    }

    public void updateLastTimeUserReceivedCode(String discordId, String ip) throws UserNotFoundException {
        User user = getByDiscordId(discordId);
        user.updateLastTimeUserReceivedCode(ip);
        saveUsers();
    }

    public void updateIp(String discordId, String newIp) throws UserNotFoundException {
        for (User user : userList) {
            if (!user.getDiscordId().equals(discordId)) continue;

            user.setCurrentAllowedIp(newIp);
            saveUsers();
            return;
        }

        throw new UserNotFoundException();
    }

    public void linkUser(String discordId, String minecraftUsername) throws MinecraftUsernameAlreadyLinkedException {
        for (User user : userList) {
            if (user.isMinecraftUsernameLinked(minecraftUsername)) throw new MinecraftUsernameAlreadyLinkedException();
            if (!user.getDiscordId().equals(discordId)) continue;

            // If user found, link his minecraft account
            user.linkMinecraftUsername(minecraftUsername);

            saveUsers();
            return;
        }

        // If user wasn't found, create one
        User user = new User(discordId, Arrays.asList(minecraftUsername), new ArrayList<>(), "");
        addUser(user);
    }

    public void unlinkUser(String minecraftUsername) throws NotFoundException {
        for (User user : userList) {
            if (!user.isMinecraftUsernameLinked(minecraftUsername)) continue;

            user.unlinkMinecraftUsername(minecraftUsername);

            saveUsers();
            return;
        }

        throw new NotFoundException();
    }

    protected abstract void loadUsers() throws IOException;

    protected abstract void saveUsers();

    protected abstract void setUp();

    public void onShutDown() {
        saveUsers();
    }
}
