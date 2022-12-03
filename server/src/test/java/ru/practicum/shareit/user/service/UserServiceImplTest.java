package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.error.UserNotFoundException;
import ru.practicum.shareit.user.repository.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {
    private User user;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository mockRepository;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("test")
                .email("test@test.ru")
                .build();
        when(mockRepository.save(user)).thenReturn(user);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(user));
    }

    @Test
    @DisplayName("Create new user, expected OK")
    void testSave() {
        User savedUser = userService.save(user);
        assertEquals(1L, savedUser.getId());
        assertEquals("test", savedUser.getName());
        assertEquals("test@test.ru", savedUser.getEmail());
        verify(mockRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Find all users, expected OK")
    void testFindAll() {
        List<User> users = List.of(
                user,
                User.builder()
                        .id(2L)
                        .name("test2")
                        .email("test2@test.ru")
                        .build()
        );
        when(mockRepository.findAll()).thenReturn(users);

        List<User> returnedUsers = userService.findAll();
        assertEquals(user, returnedUsers.get(0));
        assertEquals(2L, returnedUsers.get(1).getId());
        assertEquals("test2", returnedUsers.get(1).getName());
        assertEquals("test2@test.ru", returnedUsers.get(1).getEmail());
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find user by Id, expected OK")
    void testFindById() {
        User returnedUser = userService.findById(1L);
        assertEquals(1L, returnedUser.getId());
        assertEquals("test", returnedUser.getName());
        assertEquals("test@test.ru", returnedUser.getEmail());
        verify(mockRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find user by Id, expected User Not Found Exception")
    void testFindByIdNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.findById(2L));
    }

    @Test
    @DisplayName("Update user by Id all fields, expected OK")
    void testUpdateAllFields() {
        User updatedUser = User.builder()
                .name("updated")
                .email("update@test.ru")
                .build();
        User returnedUser = userService.update(1L, updatedUser);
        assertEquals(1L, returnedUser.getId());
        assertEquals("updated", returnedUser.getName());
        assertEquals("update@test.ru", returnedUser.getEmail());
        verify(mockRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Update user by Id part fields, expected OK")
    void testUpdatePartFields() {
        User updatedUser = User.builder()
                .name("updated")
                .build();
        User returnedUser = userService.update(1L, updatedUser);
        assertEquals(1L, returnedUser.getId());
        assertEquals("updated", returnedUser.getName());
        assertEquals("test@test.ru", returnedUser.getEmail());
        verify(mockRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Update user by Id, expected User Not Found Exception")
    void testUpdateByIdNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.update(2L, user));
    }

    @Test
    @DisplayName("Delete user by Id, expected OK")
    void testDeleteById() {
        userService.deleteById(1L);
        verify(mockRepository, times(1)).deleteById(1L);
    }
}