package org.artostapyshyn.user.service;

import org.artostapyshyn.user.dto.UserRequestDto;
import org.artostapyshyn.user.dto.UserResponseDto;
import org.artostapyshyn.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> save(UserRequestDto userDto);

    Mono<UserResponseDto> findById(Long id);

    Flux<UserResponseDto> findAll();

    Mono<UserResponseDto> update(Long id, UserRequestDto userDto);

    Mono<Void> delete(Long id);
}
