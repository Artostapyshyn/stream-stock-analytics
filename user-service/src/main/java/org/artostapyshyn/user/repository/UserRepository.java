package org.artostapyshyn.user.repository;

import org.artostapyshyn.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
