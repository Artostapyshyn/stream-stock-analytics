package org.artostapyshyn.user.service;

import org.artostapyshyn.user.model.User;
import org.artostapyshyn.user.repository.UserRepository;
import org.artostapyshyn.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserServiceImpl service = new UserServiceImpl(userRepository);

    @Test
    void testSave_setsUserRole() {
        User user = new User();
        user.setEmail("test@mail.com");

        when(userRepository.save(user)).thenReturn(user);

        User result = service.save(user);
        assertEquals("USER", result.getRole());
    }

    @Test
    void testFindById_found() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = service.findById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testFindById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertTrue(service.findById(1L).isEmpty());
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(new User()));
        assertEquals(1, service.findAll().size());
    }

    @Test
    void testUpdate_success() {
        User existing = new User();
        existing.setId("1");
        User updated = new User();
        updated.setEmail("new@mail.com");
        updated.setPassword("pass");
        updated.setRole("ADMIN");
        updated.setFirstName("John");
        updated.setLastName("Doe");
        updated.setBirthDate(LocalDate.of(2000, 1, 1));
        updated.setPhone("123456");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        User result = service.update(1L, updated);
        assertEquals("new@mail.com", result.getEmail());
        assertEquals("ADMIN", result.getRole());
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testUpdate_userNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.update(99L, new User()));
    }

    @Test
    void testDelete() {
        doNothing().when(userRepository).deleteById(1L);
        service.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
