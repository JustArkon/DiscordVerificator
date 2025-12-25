package net.justempire.discordverificator.configuration;

import net.justempire.discordverificator.types.enums.RepositoryType;

public class RepositoryConfiguration {
    private RepositoryType type;
    private String fileName;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public RepositoryType getType() { return type; }
    public void setType(RepositoryType type) { this.type = type; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

