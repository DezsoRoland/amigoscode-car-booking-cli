package com.dezsoroland.user;

import com.dezsoroland.user.User;
import com.dezsoroland.user.UserArrayDataAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserArrayDataAccessServiceTest {

    private UserArrayDataAccessService userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserArrayDataAccessService();
    }

    @Test
    @DisplayName("Reading users from CSV")
    void getAllUsers() throws IOException {
        List<User> users = userDao.getAllUsers();

        assertThat(users).isNotEmpty();
        assertThat(users).allSatisfy(user -> {
            assertThat(user.getId()).isNotNull();
            assertThat(user.getName()).isNotBlank();
        });
        assertThat(users).extracting(User::getName).doesNotContain("name");
        assertThat(users).extracting(User::getId).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("Finding user by ID")
    void getUserById() throws IOException {
        User user = userDao.getAllUsers().get(0);

        User found = userDao.getUserById(user.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(user.getId());
        assertThat(found.getName()).isEqualTo(user.getName());
    }
}