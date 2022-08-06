package com.project.arca.repository;

import org.springframework.stereotype.Repository;

import com.project.arca.model.*;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface LoginInputRepository extends JpaRepository<LoginInput, Long> {
    LoginInput findByEmail(String email);
}
