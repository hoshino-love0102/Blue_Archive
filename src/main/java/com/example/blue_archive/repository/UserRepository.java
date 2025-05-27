package com.example.blue_archive.repository;

import com.example.blue_archive.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
    User findByUserId(String userId);
}
