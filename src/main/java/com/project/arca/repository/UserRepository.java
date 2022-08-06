package com.project.arca.repository;

import com.project.arca.model.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(Integer id);
    User findByName(String name);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
