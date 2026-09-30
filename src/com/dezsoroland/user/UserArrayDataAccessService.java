package com.dezsoroland.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
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
            return new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Could not read users: " + e.getMessage());
            return new ArrayList<>();
        }

        String[] lines = content.split("\\R");

        List<User> users = new ArrayList<>();

        for (String line : lines) {
            if (line.isBlank() || line.startsWith("id,")) {
                continue;
            }
            String[] data = line.split(",");
            users.add(new User(UUID.fromString(data[0].trim()), data[1].trim()));
        }

        return users;
    }

    @Override
    public User getUserById(UUID id) throws IOException {
        for (User user : getAllUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}
