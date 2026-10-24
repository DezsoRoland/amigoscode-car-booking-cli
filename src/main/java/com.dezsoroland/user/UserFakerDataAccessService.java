package com.dezsoroland.user;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserFakerDataAccessService implements UserDao {
    @Override
    public List<User> getAllUsers() {
        Faker faker = new Faker();
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            users.add(new User(UUID.randomUUID(), faker.name().fullName()));
        }
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
