package com.dezsoroland.user;

import java.io.IOException;
import java.util.UUID;

public interface UserDao {

    User[] getAllUsers() throws IOException;

    User getUserById(UUID id) throws IOException;
}
