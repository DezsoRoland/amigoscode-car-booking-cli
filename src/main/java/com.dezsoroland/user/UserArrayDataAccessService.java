package com.dezsoroland.user;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserArrayDataAccessService implements UserDao {
    @Override
    public List<User> getAllUsers() throws IOException {
        String content;

        try (InputStream in = getClass().getResourceAsStream("/Users.csv")) {
            if (in == null) {
                System.out.println("Users file not found on classpath: /Users.csv");
                return Collections.emptyList();
            }
            content = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }

        return content.lines()
                .filter(line -> !line.isBlank())
                .filter(line -> !line.startsWith("id,"))
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
