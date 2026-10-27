package com.dezsoroland.user;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserFakerDataAccessService implements UserDao {
    private final List<User> users;

    public UserFakerDataAccessService() {
        Faker faker = new Faker();
        List<User> generated = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            generated.add(new User(UUID.randomUUID(), faker.name().fullName()));
        }

        users = List.copyOf(generated);
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(UUID id) {
        return getAllUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
