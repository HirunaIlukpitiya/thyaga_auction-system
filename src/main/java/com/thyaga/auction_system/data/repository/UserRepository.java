package com.thyaga.auction_system.data.repository;

import com.thyaga.auction_system.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByUsernameOrEmail(String username, String email);

    Optional<User> getUserById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> getUserByEmail(String username);
}
