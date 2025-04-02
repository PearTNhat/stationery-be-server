package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail (String username);
    boolean existsByEmail(String email);
}
