package org.artostapyshyn.user.controller;

import org.artostapyshyn.user.model.User;
import org.artostapyshyn.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id(5L)
                .email("test@example.com")
                .password("password123")
                .role("USER")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phone("1234567890")
                .build();
    }

    @Test
    void testGetAllUsers() {
        when(userService.findAll()).thenReturn(Flux.just(testUser));

        List<User> users = userController.getAllUsers().collectList().block();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(testUser.getEmail(), users.get(0).getEmail());
        verify(userService, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        when(userService.findById(5L)).thenReturn(Mono.just(testUser));

        User user = userController.getUserById(5L).block();
        assertNotNull(user);
        assertEquals(testUser.getEmail(), user.getEmail());
        verify(userService, times(1)).findById(5L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.findById(5L)).thenReturn(Mono.empty());

        User user = userController.getUserById(5L).block();
        assertNull(user);
        verify(userService, times(1)).findById(5L);
    }

    @Test
    void testSave() {
        when(userService.save(any(User.class))).thenReturn(Mono.just(testUser));

        Mono<User> response = userController.save(testUser);
        assertNotNull(response);
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        User updatedUser = User.builder()
                .id(5L)
                .email("updated@example.com")
                .password("newpassword")
                .role("USER")
                .firstName("John")
                .lastName("Updated")
                .build();

        when(userService.update(eq(5L), any(User.class))).thenReturn(Mono.just(updatedUser));

        Mono<User> response = userController.updateUser(5L, testUser);
        assertNotNull(response);
        verify(userService, times(1)).update(eq(5L), any(User.class));
    }
}