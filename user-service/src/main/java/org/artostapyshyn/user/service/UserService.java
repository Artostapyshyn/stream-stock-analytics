package org.artostapyshyn.user.service;

import org.artostapyshyn.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> save(User userVO);

    Mono<User> findById(Long id);

    Flux<User> findAll();

    Mono<User> update(Long id, User updatedUser);

    Mono<Void> delete(Long id);
}
