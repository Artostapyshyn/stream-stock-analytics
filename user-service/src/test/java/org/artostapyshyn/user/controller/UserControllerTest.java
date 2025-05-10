package org.artostapyshyn.user.controller;

import org.artostapyshyn.user.model.User;
import org.artostapyshyn.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                .id("1")
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
        List<User> mockUsers = Arrays.asList(testUser);
        when(userService.findAll()).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testUser.getEmail(), response.getBody().get(0).getEmail());
        verify(userService, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser.getEmail(), response.getBody().getEmail());
        verify(userService, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(userService.save(any(User.class))).thenReturn(testUser);

        ResponseEntity<User> response = userController.save(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser.getEmail(), response.getBody().getEmail());
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        User updatedUser = User.builder()
                .id("1")
                .email("updated@example.com")
                .password("newpassword")
                .role("USER")
                .firstName("John")
                .lastName("Updated")
                .build();

        when(userService.update(eq(1L), any(User.class))).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(1L, testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated@example.com", response.getBody().getEmail());
        assertEquals("Updated", response.getBody().getLastName());
        verify(userService, times(1)).update(eq(1L), any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userService.update(eq(1L), any(User.class)))
                .thenThrow(new RuntimeException("User not found"));

        ResponseEntity<User> response = userController.updateUser(1L, testUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).update(eq(1L), any(User.class));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).delete(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).delete(1L);
    }
}