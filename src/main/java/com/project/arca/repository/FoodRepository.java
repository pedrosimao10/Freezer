package com.project.arca.repository;

import com.project.arca.model.Food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Food findById(long id);
    Food findByName(String name);
    Food deleteByQuantity(Integer quantity);
    
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE food SET quantity=:quantity WHERE id=:id")
    void changeAmmountFood(Integer id, Integer quantity);
}
