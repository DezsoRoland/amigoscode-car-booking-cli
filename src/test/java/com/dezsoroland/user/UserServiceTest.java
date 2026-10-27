package com.dezsoroland.user;

import com.dezsoroland.user.User;
import com.dezsoroland.user.UserDao;
import com.dezsoroland.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Returns Users from DAO")
    void getAllUsers() throws IOException {
        User alice = new User(UUID.randomUUID(), "Alice Smith");
        User bob = new User(UUID.randomUUID(), "Bob Jones");
        when(userDao.getAllUsers()).thenReturn(List.of(alice, bob));

        List<User> result = userService.getAllUsers();

        assertThat(result).contains(alice, bob);
    }

    @Test
    @DisplayName("Returns True when user exist")
    void isValidUser() throws IOException {
        User alice = new User(UUID.randomUUID(), "Alice Smith");
        when(userDao.getUserById(alice.getId())).thenReturn(alice);

        assertThat(userService.isValidUser(alice.getId())).isTrue();
    }

    @Test
    @DisplayName("Returns user by ID when found")
    void getUserById() throws IOException {
        UUID id = UUID.randomUUID();
        User alice = new User(id, "Alice Smith");
        when(userDao.getUserById(id)).thenReturn(alice);

        assertThat(userService.getUserById(id)).isEqualTo(alice);
    }
}