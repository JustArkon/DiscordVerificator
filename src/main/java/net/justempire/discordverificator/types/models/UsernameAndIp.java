package net.justempire.discordverificator.types.models;

// Used to store username and IP
public class UsernameAndIp {
    private final String username;
    private final String ipAddress;

    public UsernameAndIp(String username, String ipAddress) {
        this.username = username;
        this.ipAddress = ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
