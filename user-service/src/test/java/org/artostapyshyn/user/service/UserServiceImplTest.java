package org.artostapyshyn.user.service;

import org.artostapyshyn.user.dto.UserRequestDto;
import org.artostapyshyn.user.dto.UserResponseDto;
import org.artostapyshyn.user.mapper.UserMapper;
import org.artostapyshyn.user.model.User;
import org.artostapyshyn.user.repository.UserRepository;
import org.artostapyshyn.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserMapper userMapper = new UserMapper();
    private final UserServiceImpl service = new UserServiceImpl(userRepository, userMapper);

    private UserRequestDto testUserRequestDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUserRequestDto = UserRequestDto.builder()
                .email("test@mail.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phone("1234567890")
                .build();

        testUser = User.builder()
                .id(5L)
                .email("test@mail.com")
                .password("password123")
                .role("USER")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phone("1234567890")
                .build();
    }

    @Test
    void testSave_setsUserRole() {
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(testUser));

        User result = service.save(testUserRequestDto).block();
        assertNotNull(result);
        assertEquals("USER", result.getRole());
    }

    @Test
    void testFindById_found() {
        when(userRepository.findById(5L)).thenReturn(Mono.just(testUser));

        UserResponseDto result = service.findById(5L).block();
        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void testFindById_notFound() {
        when(userRepository.findById(5L)).thenReturn(Mono.empty());
        assertTrue(service.findById(5L).blockOptional().isEmpty());
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(Flux.just(testUser));
        List<UserResponseDto> users = service.findAll().collectList().block();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("test@mail.com", users.getFirst().getEmail());
    }

    @Test
    void testDelete() {
        when(userRepository.deleteById(5L)).thenReturn(Mono.empty());
        assertDoesNotThrow(() -> service.delete(5L).block());
    }

    @Test
    void testUpdate() {
        when(userRepository.findById(5L)).thenReturn(Mono.just(testUser));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(testUser));

        UserResponseDto result = service.update(5L, testUserRequestDto).block();
        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
    }
}
