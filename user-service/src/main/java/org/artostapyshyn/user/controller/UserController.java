package org.artostapyshyn.user.controller;

import lombok.AllArgsConstructor;
import org.artostapyshyn.user.model.UserVO;
import org.artostapyshyn.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserVO> save(@RequestBody UserVO userVO) {
        return ResponseEntity.ok(userService.save(userVO));
    }

    @GetMapping
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Secured endpoint");
    }
}
