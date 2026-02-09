package org.artostapyshyn.user.service;

import org.artostapyshyn.user.model.User;
import org.artostapyshyn.user.repository.UserRepository;
import org.artostapyshyn.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserServiceImpl service = new UserServiceImpl(userRepository);

    @Test
    void testSave_setsUserRole() {
        User user = new User();
        user.setEmail("test@mail.com");

        when(userRepository.save(user)).thenReturn(Mono.just(user));

        User result = service.save(user).block();
        assertNotNull(result);
        assertEquals("USER", result.getRole());
    }

    @Test
    void testFindById_found() {
        User user = new User();
        when(userRepository.findById(5L)).thenReturn(Mono.just(user));

        Mono<User> result = service.findById(5L);
        assertNotNull(result.block());
    }

    @Test
    void testFindById_notFound() {
        when(userRepository.findById(5L)).thenReturn(Mono.empty());
        assertTrue(service.findById(5L).blockOptional().isEmpty());
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(Flux.just(new User()));
        List<User> users = service.findAll().collectList().block();
        assertNotNull(users);
        assertEquals(1, users.size());
    }
}
