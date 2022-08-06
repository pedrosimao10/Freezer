package com.project.arca.repository;

import com.project.arca.model.Food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Food findById(long id);
    Food findByName(String name);
}
