package com.dezsoroland.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserArrayDataAccessService implements UserDao {
    @Override
    public List<User> getAllUsers() throws IOException {
        Path path = Path.of("src/com/dezsoroland/Users.csv");
        String content;

        try {
            content = Files.readString(path);
        } catch (NoSuchFileException e) {
            System.out.println("Users file not found: " + path.toAbsolutePath());
            return Collections.emptyList();
        } catch (IOException e) {
            System.out.println("Could not read users: " + e.getMessage());
            return Collections.emptyList();
        }

        return content.lines()
                .filter(line-> !line.isBlank())
                .filter(line-> !line.startsWith("id,"))
                .map(line -> line.split(","))
                .map(data -> new User(UUID.fromString(data[0].trim()), data[1].trim()))
                .toList();
    }

    @Override
    public User getUserById(UUID id) throws IOException {
       return getAllUsers().stream()
               .filter(user -> user.getId().equals(id))
               .findFirst()
               .orElse(null);
    }
}
