package com.app.chatApp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.chatApp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
    User findByUsername(String username);

    boolean existsByUsername(String username);
    
}
