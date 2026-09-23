package com.dezsoroland.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class UserArrayDataAccessService implements UserDao {
    @Override
    public User[] getAllUsers() throws IOException {
        String content = Files.readString(Path.of("src/com/dezsoroland/Users.csv"));

        String[] lines = content.split("\\R");

        User[] users = new User[lines.length - 1];

        for (int i = 1; i < lines.length; i++) {
            String[] data = lines[i].split(",");
            UUID id = UUID.fromString(data[0]);
            String name = data[1];

            users[i - 1] = new User(id, name);
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
