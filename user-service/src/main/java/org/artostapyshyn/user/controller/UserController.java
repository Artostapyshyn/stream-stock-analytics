package org.artostapyshyn.user.controller;

import lombok.RequiredArgsConstructor;
import org.artostapyshyn.user.dto.UserRequestDto;
import org.artostapyshyn.user.dto.UserResponseDto;
import org.artostapyshyn.user.model.User;
import org.artostapyshyn.user.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Mono<User> save(@RequestBody UserRequestDto userDto) {
        return userService.save(userDto);
    }

    @GetMapping
    public Flux<UserResponseDto> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userDto) {
        return userService.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable Long id) {
        return userService.delete(id);
    }
}
