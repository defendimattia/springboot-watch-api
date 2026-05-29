package it.defendimattia.backenddemo.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.defendimattia.backenddemo.security.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}