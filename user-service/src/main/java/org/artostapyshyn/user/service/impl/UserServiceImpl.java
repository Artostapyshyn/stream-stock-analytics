package org.artostapyshyn.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.artostapyshyn.user.model.User;
import org.artostapyshyn.user.repository.UserRepository;
import org.artostapyshyn.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> save(User user) {
        user.setRole("USER");
        return userRepository.save(user);
    }

    @Override
    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Flux<User> findAll() {

        return userRepository.findAll();
    }

    @Override
    public Mono<User> update(Long id, User user) {
        return userRepository.findById(id)
                .flatMap(existing -> {
                    user.setId(String.valueOf(id));
                    return userRepository.save(user);
                });
    }

    @Override
    public Mono<ResponseEntity<Void>> delete(Long id) {
        userRepository.deleteById(id);
        return null;
    }
}
