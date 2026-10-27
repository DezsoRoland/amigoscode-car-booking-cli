package com.dezsoroland.user;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserDao {

    List<User> getAllUsers() throws IOException;

    User getUserById(UUID id) throws IOException;
}
