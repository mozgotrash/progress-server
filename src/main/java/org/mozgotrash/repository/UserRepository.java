package org.mozgotrash.repository;

import org.mozgotrash.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByTgId(Long tgId);
}
