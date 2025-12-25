package net.justempire.discordverificator.repository.implementation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import net.justempire.discordverificator.repository.abstraction.UserRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class JsonUserRepository extends UserRepository {
    private final String pathToJson;

    public JsonUserRepository(String pathToJson){
        this.pathToJson = pathToJson;
        setUp();
    }

    protected void loadUsers() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        File source = new File(pathToJson);

        try { userList = objectMapper.readValue(source, new TypeReference<>() {}); }
        catch (FileNotFoundException e) {
            // Create JSON file if it didn't exist
            if (source.createNewFile()) loadUsers();
        }
    }

    protected void saveUsers() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        try { objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(pathToJson), userList); }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    protected void setUp() {
        try {
            // Trying to load users from JSON
            loadUsers();
        }
        catch (MismatchedInputException e) {
            userList = new ArrayList<>();
            saveUsers();
            try { loadUsers(); }
            catch (IOException ex) { throw new RuntimeException(); }
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

}
