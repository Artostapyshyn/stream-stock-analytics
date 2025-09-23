package org.artostapyshyn.user.controller;

import lombok.RequiredArgsConstructor;
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
    public Mono<User> save(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable Long id) {
        return userService.delete(id);
    }
}
