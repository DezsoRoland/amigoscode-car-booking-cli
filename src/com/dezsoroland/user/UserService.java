package com.dezsoroland.user;

import java.io.IOException;
import java.util.UUID;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User[] getAllUsers() throws IOException {
        return userDao.getAllUsers();
    }

    public void listAllUserNames() throws IOException {
        User[] users = getAllUsers();
        for (User user : users) {
            System.out.println(user.getName());
        }
    };

    public boolean isValidUser(UUID id) throws IOException {
        User[] users = getAllUsers();
        for (User user : users) {
            if (user.getId().equals(id)) {
                return true;
            }
        }

        return false;
    };

    public User getUserById(UUID id) throws IOException {
       return userDao.getUserById(id);
    }
}
