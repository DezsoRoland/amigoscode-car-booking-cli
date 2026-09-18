package com.dezsoroland.user;

import java.util.UUID;

public class UserService {
    private final UserDao userDAO = new UserDao();


    public User[] getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void listAllUserNames() {
        User[] users = getAllUsers();
        for (User user : users) {
            System.out.println(user.getName());
        }
    };

    public boolean isValidUser(UUID id) {
        User[] users = getAllUsers();
        System.out.println("Searching for: " + id);
        for (User user : users) {
            if (user.getId().equals(id)) {
                return true;
            }
        }

        return false;
    };

    public User getUserById(UUID id) {
       return userDAO.getUserById(id);
    }
}
