package org.artostapyshyn.user.controller;

import org.artostapyshyn.user.dto.UserRequestDto;
import org.artostapyshyn.user.dto.UserResponseDto;
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
    private UserRequestDto testUserRequestDto;
    private UserResponseDto testUserResponseDto;

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

        testUserRequestDto = UserRequestDto.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phone("1234567890")
                .build();

        testUserResponseDto = UserResponseDto.builder()
                .id(5L)
                .email("test@example.com")
                .role("USER")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phone("1234567890")
                .build();
    }

    @Test
    void testGetAllUsers() {
        when(userService.findAll()).thenReturn(Flux.just(testUserResponseDto));

        List<UserResponseDto> users = userController.getAllUsers().collectList().block();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(testUserResponseDto.getEmail(), users.get(0).getEmail());
        verify(userService, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        when(userService.findById(5L)).thenReturn(Mono.just(testUserResponseDto));

        UserResponseDto user = userController.getUserById(5L).block();
        assertNotNull(user);
        assertEquals(testUserResponseDto.getEmail(), user.getEmail());
        verify(userService, times(1)).findById(5L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.findById(5L)).thenReturn(Mono.empty());

        UserResponseDto user = userController.getUserById(5L).block();
        assertNull(user);
        verify(userService, times(1)).findById(5L);
    }

    @Test
    void testSave() {
        when(userService.save(any(UserRequestDto.class))).thenReturn(Mono.just(testUser));

        Mono<User> response = userController.save(testUserRequestDto);
        assertNotNull(response);
        verify(userService, times(1)).save(any(UserRequestDto.class));
    }

    @Test
    void testUpdateUser() {
        UserResponseDto updatedUserResponse = UserResponseDto.builder()
                .id(5L)
                .email("updated@example.com")
                .role("USER")
                .firstName("John")
                .lastName("Updated")
                .build();

        when(userService.update(eq(5L), any(UserRequestDto.class))).thenReturn(Mono.just(updatedUserResponse));

        Mono<UserResponseDto> response = userController.updateUser(5L, testUserRequestDto);
        assertNotNull(response);
        verify(userService, times(1)).update(eq(5L), any(UserRequestDto.class));
    }
}