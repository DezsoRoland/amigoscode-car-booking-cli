package com.dezsoroland.user;

import java.util.UUID;

public class UserDao {
    private static final User[] USERS = {
            new User(UUID.fromString("302c7d44-0b58-49fc-82ce-c7bc0e2e47d3"), "Madison Harris"),
            new User(UUID.fromString("0947c247-24e9-48fc-ba6b-dfa28f4064ee"), "Anthony Clark"),
            new User(UUID.fromString("b7a11831-225f-4efe-aac3-206f27c6d6fb"), "Mark Edwards"),
            new User(UUID.fromString("2dd9eed9-e22f-4887-acb1-e7af63c8c4b5"), "Jennifer Wilson"),
            new User(UUID.fromString("b9a8e813-9cd2-412f-8cc9-3fdee463ca4c"), "Brandon Anderson")
    };

    public User[] getAllUsers() {
        return USERS;
    }

    public User getUserById(UUID id) {
        for (User user : USERS) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}
