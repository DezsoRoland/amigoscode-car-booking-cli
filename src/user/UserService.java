package user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.UUID;

public class UserService {

    public User[] getAllUsers() throws IOException {
        String content = Files.readString(Path.of("src/Users.csv"));

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

    public void listAllUserNames() throws IOException {
        User[] users = getAllUsers();
        for (User user : users) {
            System.out.println(user.getName());
        }
    };

    public boolean isValidUser(UUID id) throws IOException {
        User[] users = getAllUsers();
        System.out.println("Searching for: " + id);
        for (User user : users) {
            if (user.getId().equals(id)) {
                return true;
            }
        }

        return false;
    };

    public User getUserById(UUID id) throws IOException {
        for (User user : getAllUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public UUID readUserId(Scanner scanner) throws IOException {
        System.out.println("Please give a user Id:");
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                UUID id = UUID.fromString(input);
                if (isValidUser(id)) {
                    return id;
                }
                System.out.println("No user found with this Id, please try again:");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Id format, please try again:");
            }
        }
    }


}
