package org.artostapyshyn.user.service;

import org.artostapyshyn.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User userVO);

    Optional<User> findById(Long id);

    List<User> findAll();

    User update(Long id, User updatedUser);

    void delete(Long id);
}
