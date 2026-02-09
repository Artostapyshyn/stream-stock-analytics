package org.artostapyshyn.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.artostapyshyn.user.dto.UserRequestDto;
import org.artostapyshyn.user.dto.UserResponseDto;
import org.artostapyshyn.user.mapper.UserMapper;
import org.artostapyshyn.user.model.User;
import org.artostapyshyn.user.repository.UserRepository;
import org.artostapyshyn.user.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<User> save(UserRequestDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setRole("USER");
        return userRepository.save(user);
    }

    @Override
    public Mono<UserResponseDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Flux<UserResponseDto> findAll() {
        return userRepository.findAll()
                .map(userMapper::toResponseDto);
    }

    @Override
    public Mono<UserResponseDto> update(Long id, UserRequestDto userDto) {
        return userRepository.findById(id)
                .flatMap(existing -> {
                    User user = userMapper.toEntity(userDto);
                    user.setId(id);
                    user.setRole(existing.getRole());
                    return userRepository.save(user);
                })
                .map(userMapper::toResponseDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return userRepository.deleteById(id);
    }
}
