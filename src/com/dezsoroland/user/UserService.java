package com.dezsoroland.user;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() throws IOException {
        return userDao.getAllUsers();
    }

    public void listAllUserNames() throws IOException {
        List<User> users = getAllUsers();
        for (User user : users) {
            System.out.println(user.getName());
        }
    }

    public boolean isValidUser(UUID id) throws IOException {
        return userDao.getUserById(id) != null;
    }

    public User getUserById(UUID id) throws IOException {
       return userDao.getUserById(id);
    }
}
